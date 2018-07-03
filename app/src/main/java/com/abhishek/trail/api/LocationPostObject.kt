package com.abhishek.trail.api

import com.google.gson.annotations.SerializedName

class LocationPostObject(@SerializedName("location") val location: LocationObj)

class LocationObj(@SerializedName("lat") val latitude: Double?,
                  @SerializedName("lng") val longitude: Double?)