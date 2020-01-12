package com.example.nearbyplacesapp.model.api

import com.example.nearbyplacesapp.model.places.GooglePlacesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleAPI {
    @GET("place/nearbysearch/json?")
    fun getPlaces(
        @Query(
            value = "type",
            encoded = true
        ) type: String?,
        @Query(
            value = "location",
            encoded = true
        ) location: String?,
        @Query(
            value = "opennow",
            encoded = true
        ) opennow: Boolean,
        @Query(
            value = "rankby",
            encoded = true
        ) rankby: String?,
        @Query(
            value = "key",
            encoded = true
        ) key: String?
    ): Single<GooglePlacesResponse>


}
