package com.example.finalproject

import com.example.finalproject.Constants.FIRESTORE_LOCATION_KEY
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreGeoHashQueries {
    private val db = FirebaseFirestore.getInstance()

    constructor()

    fun UpdateLocationFirestore(user: User, pos: Position){
        val _latitude = pos.getGeopoint().latitude
        val _longtitude = pos.getGeopoint().longitude
        // Compute the GeoHash for a lat/lng point
        val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(_latitude, _longtitude))

        // Add the hash and the lat/lng to the document. We will use the hash
        // for queries and the lat/lng for distance comparisons.
        val updates: MutableMap<String, Any> = mutableMapOf(
            "geohash" to hash,
            "geoPoint" to pos.getGeopoint(),
        )
        db.collection(FIRESTORE_LOCATION_KEY).document(user.get_UserName()).set(updates)
    }

    fun  QueryForLocationFireStore (user:User, pos: Position, distance: Double, container : ArrayList<User>) {
        var ListNearByUserName : MutableList<DocumentSnapshot> = ArrayList()
        // Find cities within 50km of London
        val center = GeoLocation(pos.getGeopoint().latitude, pos.getGeopoint().longitude)
        val radiusInM = distance * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q = db.collection(FIRESTORE_LOCATION_KEY)
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val geopoint = doc.getGeoPoint("geoPoint")

                        // We have to filter out a few false positives due to GeoHash
                        // accuracy, but most will match
                        val docLocation = geopoint?.let { it1 -> GeoLocation(it1.latitude, geopoint.longitude) }
                        val distanceInM =
                            docLocation?.let { it1 -> GeoFireUtils.getDistanceBetween(it1, center) }
                        if (distanceInM != null) {
                            if (distanceInM <= radiusInM) {
                                ListNearByUserName.add(doc)
                            }
                        }
                    }

                }

                val userFirebaseController = FirebaseFirestoreController<User>(User::class.java)

                for (d in ListNearByUserName) {
                    if(d.id.equals(user.get_UserName())) continue
                    else {
                        val user_temp: User = userFirebaseController.retrieveObjectsFirestoreByID(
                            Constants.KEY_COLLECTION_USERS,
                            d.id
                        )
                        container.add(user_temp)
                    }
                }
            }
    }
}