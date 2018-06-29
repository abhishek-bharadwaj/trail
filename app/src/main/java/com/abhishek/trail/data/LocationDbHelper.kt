package com.abhishek.trail.data

import android.location.Location
import android.util.Log
import com.abhishek.trail.Constant
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object LocationDbHelper {

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
}