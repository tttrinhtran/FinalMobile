package com.example.finalproject

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class Position:Serializable{
     private var _latitude:Double
     private var _longitude: Double

    constructor() {
        this._latitude = 0.toDouble()
        this._longitude = 0.toDouble()
    }

    constructor(_latitude: Double, _longtitude: Double) {
        this._latitude = _latitude
        this._longitude = _longtitude
    }

    override fun toString(): String {
        return "Position(_latitude='$_latitude', _longtitude='$_longitude')"
    }

    fun set_latitude (latitude: Double){
        this._latitude = latitude
    }

    fun getLatitude () : Double{
        return this._latitude
    }

    fun setLongitude (longtitude: Double){
        this._latitude = longtitude
    }

    fun getLongitude () : Double{
        return this._longitude
    }

    fun isEqual(pos:Position?) : Boolean{
        if (pos != null) {
            return _latitude != pos.getLatitude() || _longitude != pos.getLongitude()
        }else return false
    }
}