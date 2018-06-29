package com.abhishek.trail.data

import android.arch.persistence.room.*
import com.abhishek.trail.Constant

@Dao
interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationData: LocationData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: List<LocationData>)

    @Query("select * from ${Constant.LocationTable.TABLE_NAME} WHERE ${Constant.LocationTable.COLUMN_IS_SYNCED} = 0")
    fun getUnSyncedData(): List<LocationData>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun markAsSync(locationData: LocationData)

    @Query("delete from ${Constant.LocationTable.TABLE_NAME}")
    fun deleteAll()
}