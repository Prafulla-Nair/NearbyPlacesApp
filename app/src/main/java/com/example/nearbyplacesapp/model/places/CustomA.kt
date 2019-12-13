package com.example.nearbyplacesapp.model.places


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomA  (
    @Expose
    @SerializedName("geometry")
    var geometry: Geometry,
    @Expose
    @SerializedName("vicinity")
    var vicinity: String,
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("rating")
    var rating: String,
    @Expose
    @SerializedName("opening_hours")
    var opening_hours: OpeningHours
)