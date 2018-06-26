package com.abhishek.trail

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationUpdateService : Service() {

    private val TAG = "LocationUpdateService"
    private val displacement = 10.toFloat()
    private val fastestInterval = TimeUnit.SECONDS.toMillis(5)
    private val updateInterval = TimeUnit.SECONDS.toMillis(10)

    private val mBinder = MyBinder()
    private lateinit var locationRequests: LocationRequest
    private lateinit var locationProvider: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called..")
        if (!Utils.isLocationPermissionIsGiven(this)) return
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        startLocationUpdates()
    }

    override fun onBind(intent: Intent?) = mBinder

    private fun createLocationRequest() {
        locationRequests = LocationRequest()
        locationRequests.interval = updateInterval
        locationRequests.fastestInterval = fastestInterval
        locationRequests.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequests.smallestDisplacement = displacement
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (!Utils.isLocationPermissionIsGiven(this)) return
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