package com.abhishek.trail

import android.app.Application

class MyApplication : Application() {

    companion object {
        lateinit var mContext: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    fun getContext(): MyApplication? {
        return mContext
    }
}