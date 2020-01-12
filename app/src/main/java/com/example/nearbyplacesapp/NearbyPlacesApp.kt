package com.example.nearbyplacesapp

import android.app.Application
import com.example.nearbyplacesapp.di.AppComponent
import com.example.nearbyplacesapp.di.AppModule
import com.example.nearbyplacesapp.di.DaggerAppComponent

class NearbyPlacesApp : Application() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        //need to run once to generate it
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}