package com.abhishek.trail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class TrackingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        checkForPermissionAndStartTracking()
    }

    private fun checkForPermissionAndStartTracking() {
        if (!Utils.isLocationPermissionIsGiven(this)) {
            Toast.makeText(this, "Please give location permission..", Toast.LENGTH_LONG).show()
            return
        }
        startService(Intent(this, LocationUpdateService::class.java))
    }
}