package com.abhishek.trail.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.abhishek.trail.MyApplication

@Database(entities = [LocationData::class], version = 1)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDataDao(): LocationDAO

    private object HOLDER {
        val INSTANCE = Room.databaseBuilder(MyApplication.mContext,
            LocationDatabase::class.java, "location.db").build()
    }

    companion object {

        private val INSTANCE: LocationDatabase by lazy { HOLDER.INSTANCE }

        @Synchronized
        fun getInstance() = INSTANCE

        fun clearDatabase() = getInstance().locationDataDao().deleteAll()
    }
}