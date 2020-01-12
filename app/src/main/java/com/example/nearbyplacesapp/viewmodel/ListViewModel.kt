package com.example.nearbyplacesapp.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nearbyplacesapp.di.DaggerAPIComponent
import com.example.nearbyplacesapp.model.api.GoogleAPIService
import com.example.nearbyplacesapp.model.places.GooglePlaceResult
import com.example.nearbyplacesapp.model.places.GooglePlacesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class ListViewModel : ViewModel() {
    var restaurants = MutableLiveData<List<GooglePlaceResult>>()
    var cafe = MutableLiveData<List<GooglePlaceResult>>()
    var bar = MutableLiveData<List<GooglePlaceResult>>()
    var placesLoadError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()

    @Inject
    lateinit var googleAPIService: GoogleAPIService

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun refresh(location: Location) {
        fetchPlaces(location)
    }

    init {
        DaggerAPIComponent.create().inject(this)
    }

    private fun fetchPlaces(location: Location) {
        loading.value = true

        googleAPIService.getPlaces(location)

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(object :
                DisposableSingleObserver<List<GooglePlacesResponse>>() {

                override fun onSuccess(t: List<GooglePlacesResponse>) {
                    restaurants.postValue(t[0].googlePlaceResult)
                    cafe.postValue(t[1].googlePlaceResult)
                    bar.postValue(t[2].googlePlaceResult)
                    placesLoadError.value = false
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    placesLoadError.value = true
                    loading.value = false
                    e.printStackTrace()
                }

            })

    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}