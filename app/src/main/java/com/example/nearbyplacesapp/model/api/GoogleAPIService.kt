package com.example.nearbyplacesapp.model.api

import android.location.Location
import com.example.nearbyplacesapp.di.DaggerAPIComponent
import com.example.nearbyplacesapp.model.places.GooglePlacesResponse
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val API_KEY = "USE YOUR GOOGLE API KEY"

class GoogleAPIService {

    @Inject
    lateinit var googleAPI: GoogleAPI

    init {
        DaggerAPIComponent.create().inject(this)
    }


    fun getPlaces(location: Location): Single<List<GooglePlacesResponse>> {
        val observable1: Single<GooglePlacesResponse> =
            googleAPI.getPlaces(
                "restaurant",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )

        val observable2: Single<GooglePlacesResponse> =
            googleAPI.getPlaces(
                "cafe",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )

        val observable3: Single<GooglePlacesResponse> =
            googleAPI.getPlaces(
                "bar",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )


        return Single.zip(observable1.subscribeOn(Schedulers.newThread()),
            observable2.subscribeOn(Schedulers.io()),
            observable3.subscribeOn(Schedulers.io()),
            Function3<GooglePlacesResponse, GooglePlacesResponse, GooglePlacesResponse, List<GooglePlacesResponse>>
            { restaurants: GooglePlacesResponse, cafe: GooglePlacesResponse, bar: GooglePlacesResponse ->
                getPlacesList(
                    restaurants,
                    cafe,
                    bar
                )
            })
    }

    private fun getPlacesList(
        restaurants: GooglePlacesResponse,
        bar: GooglePlacesResponse,
        cafe: GooglePlacesResponse
    ): List<GooglePlacesResponse> {
        return arrayListOf(restaurants, bar, cafe)
    }

}
