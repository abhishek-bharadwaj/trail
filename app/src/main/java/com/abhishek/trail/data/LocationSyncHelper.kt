package com.abhishek.trail.data

import android.location.Location
import android.util.Log
import com.abhishek.trail.Constant
import com.abhishek.trail.MyApplication
import com.abhishek.trail.Utils
import com.abhishek.trail.api.LocationDataApi
import com.abhishek.trail.api.LocationObj
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
        if (!Utils.isNetworkAvailable(MyApplication.mContext)) return
        Single.defer {
            return@defer Single.just(LocationDatabase.getInstance().locationDataDao().getUnSyncedData())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<LocationData>> {
                override fun onSuccess(t: List<LocationData>) {
                    t.forEach { postData(it) }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data insertion failed $e")
                }
            })
    }

    fun postData(locationData: LocationData) {
        if (!Utils.isNetworkAvailable(MyApplication.mContext)) return
        val postObj = LocationPostObject(LocationObj(locationData.latitude, locationData.longitude))
        LocationDataApi.postLocation(postObj)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d(Constant.DEBUG_TAG, "Location posted successfully")
                    markLocationSynced(locationData)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun markLocationSynced(locationData: LocationData) {
        Completable.defer {
            val dao = LocationDatabase.getInstance().locationDataDao()
            dao.markAsSync(locationData)
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