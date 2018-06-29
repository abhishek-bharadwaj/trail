package com.abhishek.trail

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import com.abhishek.trail.data.LocationSyncHelper
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationUpdateService : Service() {

    private val displacement = 100.toFloat()
    private val fastestInterval = TimeUnit.MINUTES.toMillis(1)
    private val updateInterval = TimeUnit.MINUTES.toMillis(2)
    private val binder = MyBinder()

    private lateinit var locationRequests: LocationRequest
    private lateinit var locationProvider: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        Log.d(Constant.DEBUG_TAG, "onCreate() got called..")
        if (!Utils.isLocationPermissionIsGiven(this)) return
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        startLocationUpdates()
    }

    override fun onBind(intent: Intent?) = binder

    private fun createLocationRequest() {
        locationRequests = LocationRequest()
        locationRequests.interval = updateInterval
        locationRequests.fastestInterval = fastestInterval
        locationRequests.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequests.smallestDisplacement = displacement
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (!Utils.isLocationPermissionIsGiven(this)) return
        locationProvider.requestLocationUpdates(locationRequests, locationCallback, null)
    }

    fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
        stopSelf()
        Log.d(Constant.DEBUG_TAG, "location updates stopped!")
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            val timeStamp = System.currentTimeMillis()
            Log.d(Constant.DEBUG_TAG, "location updates received.. $timeStamp")
            val location = result?.locations?.get(0) ?: return
            // Insert location data to DB
            LocationSyncHelper.saveLocationData(location, timeStamp)
        }
    }

    inner class MyBinder : Binder() {
        fun getService() = this@LocationUpdateService
    }
}