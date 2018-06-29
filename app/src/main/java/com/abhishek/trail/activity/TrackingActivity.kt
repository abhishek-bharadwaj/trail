package com.abhishek.trail.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.abhishek.trail.Constant
import com.abhishek.trail.LocationUpdateService
import com.abhishek.trail.R
import com.abhishek.trail.Utils
import com.abhishek.trail.data.Pref
import kotlinx.android.synthetic.main.activity_tracking.*

class TrackingActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 34142
    }

    private var permissionDialog: AlertDialog? = null
    private var trackerService: LocationUpdateService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        setBtnText(Pref.isTracking())
        btn_track.setOnClickListener {
            val isTracking = Pref.isTracking()
            if (!isTracking) {
                checkForPermissionAndStartTracking()
            } else {
                stopTracking()
            }
        }
    }

    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }

    private fun setBtnText(isTracking: Boolean) {
        btn_track.text = if (isTracking) "Stop Tracking" else "Start tracking"
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
        val intent = Intent(this, LocationUpdateService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Pref.setIsTracking(true)
        setBtnText(true)
    }

    private fun stopTracking() {
        trackerService?.stopLocationUpdates();
        Pref.setIsTracking(false)
        setBtnText(false)
    }

    private var connection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            try {
                val b = binder as LocationUpdateService.MyBinder
                trackerService = b.getService()
            } catch (e: Exception) {
                Log.e(Constant.DEBUG_TAG, e.toString())
            }

        }

        override fun onServiceDisconnected(className: ComponentName) {
            trackerService = null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}