package com.example.nearbyplacesapp.model.api

import android.location.Location
import com.example.nearbyplacesapp.di.DaggerAPIComponent
import com.example.nearbyplacesapp.model.places.GooglePlaces
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


    fun getPlaces(location: Location): Single<List<GooglePlaces>> {
        val observable1: Single<GooglePlaces> =
            googleAPI.getPlaces(
                "restaurant",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )

        val observable2: Single<GooglePlaces> =
            googleAPI.getPlaces(
                "cafe",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )

        val observable3: Single<GooglePlaces> =
            googleAPI.getPlaces(
                "bar",
                "" + location.latitude + "," + location.longitude,
                true,
                "distance",
                API_KEY
            )


        return Single.zip(observable1.subscribeOn(Schedulers.newThread()),
            observable2.subscribeOn(Schedulers.newThread()),
            observable3.subscribeOn(Schedulers.newThread()),
            Function3<GooglePlaces, GooglePlaces, GooglePlaces, List<GooglePlaces>>
            { restaurants: GooglePlaces, cafe: GooglePlaces, bar: GooglePlaces ->
                getPlacesList(
                    restaurants,
                    cafe,
                    bar
                )
            })
    }

    private fun getPlacesList(
        restaurants: GooglePlaces,
        bar: GooglePlaces,
        cafe: GooglePlaces
    ): List<GooglePlaces> {
        val list: ArrayList<GooglePlaces> = ArrayList()
        list.add(restaurants)
        list.add(bar)
        list.add(cafe)
        return list
    }

}
