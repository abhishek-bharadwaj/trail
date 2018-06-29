package com.abhishek.trail.data

import android.content.Context
import com.abhishek.trail.MyApplication

object Pref {

    private const val PREF_NAME = "trail_pref"
    private const val KEY_IS_TRACKING = "is_tracking"

    private val pref = MyApplication.mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setIsTracking(isTracking: Boolean) =
        pref.edit().putBoolean(KEY_IS_TRACKING, isTracking).apply()

    fun isTracking() = pref.getBoolean(KEY_IS_TRACKING, false)
}