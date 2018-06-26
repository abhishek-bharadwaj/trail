package com.abhishek.trail.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.abhishek.trail.Constant

@Dao
interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationData: LocationData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: List<LocationData>)

    @Query("select * from ${Constant.LocationTable.TABLE_NAME} WHERE ${Constant.LocationTable.COLUMN_IS_SYNCED} = 0")
    fun getUnSyncedData(): List<LocationData>

    @Query("delete from ${Constant.LocationTable.TABLE_NAME}")
    fun deleteAll()
}