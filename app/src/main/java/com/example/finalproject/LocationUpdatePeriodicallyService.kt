package com.example.finalproject

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS
import com.example.finalproject.Constants.SHARED_PREFERENCE_LAST_LOCATION_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationUpdatePeriodicallyService : Service(){

    private var serviceRunningInForeground = false
    private var configurationChange = false
    private var isUpdating = false

    private lateinit var user: User
    private lateinit var sharedPreferenceUser: SharedPreferenceManager<User>
    private lateinit var sharedPreferenceLocation: SharedPreferenceManager<Position>

    // declare a global variable FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    private val localBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferenceUser =  SharedPreferenceManager(User::class.java, this)
        user = sharedPreferenceUser.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS)
        sharedPreferenceLocation =  SharedPreferenceManager(Position::class.java, this)
        getLocationUpdates()
    }


    /**
     * call this method in onCreate
     * onLocationResult call when location is changed
     */
    private fun getLocationUpdates()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if (p0.locations.isNotEmpty()) {
                    // get latest location
                    val location = p0.lastLocation
                    val position: Position? = location?.let { Position(location.latitude, it.longitude) }

                    val firestoreGeoHashQueries = FirestoreGeoHashQueries()

                    val lastPos = sharedPreferenceLocation.retrieveSerializableObjectFromSharedPreference(SHARED_PREFERENCE_LAST_LOCATION_KEY)

                    if (position != null) {
                        if(!position.isEqual(lastPos)) {
                            firestoreGeoHashQueries.UpdateLocationFirestore(user, position)
                            sharedPreferenceLocation.storeSerializableObjectToSharedPreference(position, SHARED_PREFERENCE_LAST_LOCATION_KEY)
                        }
                    }
                }

            }
        }
    }

/*    fun subscribestToLocationUpdates() {


        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, LocationUpdatePeriodicallyService::class.java))

        try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        } catch (unlikely: SecurityException) {
        }
    }

    fun unsubscribeToLocationUpdates() {

        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stopSelf()
                } else {
                }
            }

        } catch (unlikely: SecurityException) {
        }
    }*/

    /*override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }*/

    //start location updates
    private fun startLocationUpdates() {
/*        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }*/


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper())
            return
        }
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(p0: Intent?): IBinder {
        stopForeground(true)

        startLocationUpdates()

        serviceRunningInForeground = false
        configurationChange = false

        return localBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopForeground(true)

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        isUpdating = false
        stopLocationUpdates()
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

        serviceRunningInForeground = false
        configurationChange = false
        isUpdating = true

        return super.onRebind(intent)
    }

    inner class LocalBinder : Binder() {
        internal val service: LocationUpdatePeriodicallyService
            get() = this@LocationUpdatePeriodicallyService
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        configurationChange = true
    }
}

