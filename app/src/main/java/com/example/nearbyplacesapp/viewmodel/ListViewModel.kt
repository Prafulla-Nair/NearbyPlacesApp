package com.example.nearbyplacesapp.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nearbyplacesapp.di.DaggerAPIComponent
import com.example.nearbyplacesapp.model.api.GoogleAPIService
import com.example.nearbyplacesapp.model.places.CustomA
import com.example.nearbyplacesapp.model.places.GooglePlaces
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class ListViewModel : ViewModel() {
    var restaurants = MutableLiveData<List<CustomA>>()
    var cafe = MutableLiveData<List<CustomA>>()
    var bar = MutableLiveData<List<CustomA>>()
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
                DisposableSingleObserver<List<GooglePlaces>>() {

                override fun onSuccess(t: List<GooglePlaces>) {
                    restaurants.postValue(t[0].customA)
                    cafe.postValue(t[1].customA)
                    bar.postValue(t[2].customA)
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
        super.onCleared()
        disposable.clear()
    }

}