package com.example.nearbyplacesapp.di

import com.example.nearbyplacesapp.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}