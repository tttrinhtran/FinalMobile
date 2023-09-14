package com.example.finalproject

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

class Position:Serializable{
     private lateinit var geoPoint : GeoPoint
     private lateinit var geohash : String

    constructor() {
    }

    constructor(geoPoint: GeoPoint) {
        this.geoPoint = geoPoint
    }

    constructor(geoPoint: GeoPoint, geohash:String) {
        this.geoPoint = geoPoint
        this.geohash = geohash
    }



    fun setGeopoint (geoPoint: GeoPoint){
        this.geoPoint = geoPoint
    }

    fun getGeopoint () : GeoPoint{
        return this.geoPoint
    }


    fun isEqual(pos:Position?) : Boolean{
        if(this.isInitGeopoint()) {
            if (pos != null && pos.isInitGeopoint()) {
                return this.geoPoint == pos.geoPoint
            } else {
                return false
            }
        } else {
            return false
        }
    }

    fun isInitGeopoint() : Boolean{
        return ::geoPoint.isInitialized
    }

}