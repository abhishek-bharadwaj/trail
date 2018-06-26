package com.abhishek.trail.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.abhishek.trail.Constant

@Entity(tableName = Constant.LocationTable.TABLE_NAME)
class LocationData(@ColumnInfo(name = Constant.LocationTable.COLUMN_LAT)
                   var latitude: Long? = null,
                   @ColumnInfo(name = Constant.LocationTable.COLUMN_LONG)
                   var longitude: Long? = null,
                   @ColumnInfo(name = Constant.LocationTable.COLUMN_TIMESTAMP)
                   @PrimaryKey
                   var timestamp: Long? = null,
                   @ColumnInfo(name = Constant.LocationTable.COLUMN_IS_SYNCED)
                   var isSynced: Boolean = false)