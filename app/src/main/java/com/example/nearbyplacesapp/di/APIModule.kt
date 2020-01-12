package com.example.nearbyplacesapp.di

import com.example.nearbyplacesapp.model.api.GoogleAPI
import com.example.nearbyplacesapp.model.api.GoogleAPIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://maps.googleapis.com/maps/api/"

@Module
class APIModule {

    @Provides
    fun provideGooglePlacesAPI(): GoogleAPI {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


        return retrofit.create(GoogleAPI::class.java)
    }

    @Provides
    fun provideGoogleAPIService(): GoogleAPIService {
        return GoogleAPIService()
    }

}