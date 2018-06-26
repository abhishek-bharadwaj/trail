package com.abhishek.trail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class TrackingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        checkForPermissionAndStartTracking()
    }

    private fun checkForPermissionAndStartTracking() {
        if (!isLocationPermissionIsGiven()) {
            Toast.makeText(this, "Please give location permission..", Toast.LENGTH_LONG).show()
            return
        }
        startService(Intent(this, LocationUpdateService::class.java))
    }

    private fun isLocationPermissionIsGiven() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}