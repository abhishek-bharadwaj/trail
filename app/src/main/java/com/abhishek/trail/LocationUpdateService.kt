package com.abhishek.trail

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import com.google.android.gms.location.*

class LocationUpdateService : Service() {

    companion object {

        private const val TAG = "LocationUpdateService"

        private const val DISPLACEMENT = 1 // 10 meters
        private const val FATEST_INTERVAL = 5 * 1000 // 5 sec
        private const val UPDATE_INTERVAL = 10 * 1000
    }

    private val mBinder = MyBinder()
    private lateinit var locationRequests: LocationRequest
    private lateinit var locationProvider: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called..")
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        startLocationUpdates()
    }

    override fun onBind(intent: Intent?) = mBinder

    private fun createLocationRequest() {
        locationRequests = LocationRequest()
        locationRequests.interval = UPDATE_INTERVAL.toLong()
        locationRequests.fastestInterval = FATEST_INTERVAL.toLong()
        locationRequests.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequests.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    private fun startLocationUpdates() {
        locationProvider.requestLocationUpdates(locationRequests, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            Log.d(TAG, "location updates received..")
        }
    }

    inner class MyBinder : Binder()
}