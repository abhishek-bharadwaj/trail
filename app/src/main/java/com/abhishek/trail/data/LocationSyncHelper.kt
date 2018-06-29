package com.abhishek.trail.data

import android.location.Location
import android.util.Log
import com.abhishek.trail.Constant
import com.abhishek.trail.api.LocationDataApi
import com.abhishek.trail.api.LocationPostObject
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object LocationSyncHelper {

    fun saveLocationData(location: Location, timeStamp: Long) {
        Completable.defer {
            LocationDatabase.getInstance().locationDataDao()
                .insert(LocationData(location.latitude, location.longitude, timeStamp, false))
            Completable.complete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d(Constant.DEBUG_TAG, "Data inserted successfully")
                    postUnSyncedLocationData()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data insertion failed $e")
                }
            })
    }

    fun postUnSyncedLocationData() {
        Single.defer {
            return@defer Single.just(LocationDatabase.getInstance().locationDataDao().getUnSyncedData())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<LocationData>> {
                override fun onSuccess(t: List<LocationData>) {
                    postData(t)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data insertion failed $e")
                }
            })
    }

    fun postData(locationData: List<LocationData>) {
        val locationPostData: MutableList<LocationPostObject> = mutableListOf()
        locationData.forEach {
            locationPostData.add(LocationPostObject(it.latitude,
                it.longitude))
        }
        LocationDataApi.postLocation(locationPostData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Void> {
                override fun onSuccess(t: Void) {
                    Log.d(Constant.DEBUG_TAG, "Location posted successfully")
                    markLocationSynced(locationData)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d(Constant.DEBUG_TAG, "Location posting failed $e")
                }
            })
    }

    fun markLocationSynced(locationData: List<LocationData>) {
        Completable.defer {
            val dao = LocationDatabase.getInstance().locationDataDao()
            locationData.forEach {
                dao.markAsSync(it)
            }
            Completable.complete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d(Constant.DEBUG_TAG, "All data marked as synced")
                    postUnSyncedLocationData()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data marking sync failed $e")
                }
            })
    }
}