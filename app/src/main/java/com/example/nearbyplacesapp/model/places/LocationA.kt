package com.example.nearbyplacesapp.model.places

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationA(
    @Expose
    @SerializedName("lat")
    var lat: String,
    @Expose
    @SerializedName("lng")
    var lng: String
)