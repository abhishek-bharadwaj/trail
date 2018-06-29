package com.abhishek.trail.data

import android.location.Location
import android.util.Log
import com.abhishek.trail.Constant
import com.abhishek.trail.api.LocationDataApi
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
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data insertion failed $e")
                }
            })
    }

    fun postUnSyncedLocationDataAndPost() {
        Single.defer {
            return@defer Single.just(LocationDatabase.getInstance().locationDataDao().getUnSyncedData())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<LocationData>> {
                override fun onSuccess(t: List<LocationData>) {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(Constant.DEBUG_TAG, "Data insertion failed $e")
                }
            })
    }
}