/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.finalproject

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.finalproject.Constants.LOCATION_UPDATE_STATUS
import com.example.finalproject.Constants.SHARED_PREFERENCE_LAST_LOCATION_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import java.util.concurrent.TimeUnit

/**
 * Service tracks location when requested and updates Activity via binding. If Activity is
 * stopped/unbinds and tracking is enabled, the service promotes itself to a foreground service to
 * insure location updates aren't interrupted.
 *
 * For apps running in the background on O+ devices, location is computed much less than previous
 * versions. Please reference documentation for details.
 */
class LocationUpdatePeriodicallyService : Service() {
    /*
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private var configurationChange = false

    private var serviceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var user:User

    private lateinit var sharedPreferenceLocationUpdateStatus : SharedPreferenceManager<Boolean>
    private lateinit var sharedPrefUser: SharedPreferenceManager<User>
    private lateinit var sharedPrefPosition: SharedPreferenceManager<Position>
    private lateinit var firebaseLocationUpdate: FirestoreGeoHashQueries

    private lateinit var notificationManager: NotificationManager

    // TODO: Step 1.1, Review variables (no changes).
    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private var currentLocation: Location? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate()")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // TODO: Step 1.2, Review the FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        sharedPrefUser = SharedPreferenceManager(User::class.java, this)
        user = sharedPrefUser.retrieveSerializableObjectFromSharedPreference(Constants.KEY_SHARED_PREFERENCE_USERS)

        sharedPrefPosition = SharedPreferenceManager(Position::class.java, this)

        firebaseLocationUpdate = FirestoreGeoHashQueries()

        sharedPreferenceLocationUpdateStatus = SharedPreferenceManager<Boolean>(Boolean::class.java, this)


        // TODO: Step 1.3, Create a LocationRequest.
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = TimeUnit.SECONDS.toMillis(1)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(1)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // TODO: Step 1.4, Initialize the LocationCallback.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).
                currentLocation = locationResult.lastLocation

                // Notify our Activity that a new location was added. Again, if this was a
                // production app, the Activity would be listening for changes to a database
                // with new locations, but we are simplifying things a bit to focus on just
                // learning the location side of things.
                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                intent.putExtra(EXTRA_LOCATION, currentLocation)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                // Updates notification content if this service is running as a foreground
                // service.
                val prev_pos = sharedPrefPosition.retrieveSerializableObjectFromSharedPreference(SHARED_PREFERENCE_LAST_LOCATION_KEY)
                val pos = currentLocation?.let { Position(GeoPoint( it.latitude, it.longitude)) }

                if (pos != null) {
                    if(!pos.isEqual(prev_pos)){
                        sharedPrefPosition.storeSerializableObjectToSharedPreference(pos, SHARED_PREFERENCE_LAST_LOCATION_KEY)
                        firebaseLocationUpdate.UpdateLocationFirestore(user,pos)
                    }
                    sharedPreferenceLocationUpdateStatus.storeSerializableObjectToSharedPreference(true,
                        LOCATION_UPDATE_STATUS
                    )
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")

        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }

        try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper())
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.

        sharedPrefPosition.clearObject(SHARED_PREFERENCE_LAST_LOCATION_KEY)
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Log.d(TAG, "onRebind()")

        // MainActivity (client) returns to the foreground and rebinds to service, so the service
        // can become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")

        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label.
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            Log.d(TAG, "Start foreground service")
            serviceRunningInForeground = true
        }

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")

        SharedPreferenceUtil.saveLocationTrackingPref(this, true)

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, LocationUpdatePeriodicallyService::class.java))
    }

    fun unsubscribeToLocationUpdates() {
        Log.d(TAG, "unsubscribeToLocationUpdates()")

        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: LocationUpdatePeriodicallyService
            get() = this@LocationUpdatePeriodicallyService
    }

    companion object {
        private const val TAG = "LocationUpdatePeriodicallyService"

        private const val PACKAGE_NAME = "com.example.android.whileinuselocation"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

    }
}
