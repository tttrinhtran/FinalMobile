package com.example.finalproject

import android.widget.Toast
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

    fun QueryForLocationFireStore (pos: Position, distance: Double) : MutableList<DocumentSnapshot> {
        var ListNearByUserName : MutableList<DocumentSnapshot> = ArrayList()
        // Find cities within 50km of London
        val center = GeoLocation(pos.getGeopoint().latitude, pos.getGeopoint().longitude)
        val radiusInM = distance * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q = db.collection("cities")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                ListNearByUserName.clear()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val lat = doc.getDouble("lat")!!
                        val lng = doc.getDouble("lng")!!

                        // We have to filter out a few false positives due to GeoHash
                        // accuracy, but most will match
                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            ListNearByUserName.add(doc)
                        }
                    }

                }
            }
        return ListNearByUserName
    }
}