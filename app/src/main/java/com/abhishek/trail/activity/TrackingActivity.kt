package com.abhishek.trail.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.abhishek.trail.LocationUpdateService
import com.abhishek.trail.R
import com.abhishek.trail.Utils

class TrackingActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 34142
    }

    private var permissionDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
    }

    override fun onResume() {
        super.onResume()
        checkForPermissionAndStartTracking()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTracking()
                } else {
                    showToast("Please give location permission from settings!")
                    finish()
                    return
                }
            }
        }
    }

    private fun checkForPermissionAndStartTracking() {
        if (!Utils.isLocationPermissionIsGiven(this)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionRequestDialog()
                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_LOCATION)
                }
            }
            return
        }
        startTracking()
    }

    private fun showPermissionRequestDialog() {
        if (permissionDialog != null) {
            permissionDialog?.show()
            return
        }
        val alertBuilder = AlertDialog.Builder(this).apply {
            setCancelable(false)
            setMessage("Location permission is required to track your data.")
        }
        alertBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        }
        permissionDialog = alertBuilder.create()
        permissionDialog?.show()
    }

    private fun startTracking() {
        startService(Intent(this, LocationUpdateService::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}