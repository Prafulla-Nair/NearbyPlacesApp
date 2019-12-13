package com.example.nearbyplacesapp.model.places

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @Expose
    @SerializedName("open_now")
    var openNow: Boolean
)