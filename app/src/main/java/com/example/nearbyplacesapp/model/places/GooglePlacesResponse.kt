package com.example.nearbyplacesapp.model.places

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GooglePlacesResponse(
    @Expose
    @SerializedName("results")
    var googlePlaceResult: List<GooglePlaceResult>
)









