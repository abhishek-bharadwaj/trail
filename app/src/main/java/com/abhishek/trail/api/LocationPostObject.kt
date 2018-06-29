package com.abhishek.trail.api

import com.google.gson.annotations.SerializedName

class LocationPostObject(@SerializedName("latitude") val latitude: Double?,
                         @SerializedName("longitude") val longitude: Double?)