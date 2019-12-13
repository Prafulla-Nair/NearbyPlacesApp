package com.example.nearbyplacesapp.di


import com.example.nearbyplacesapp.model.api.GoogleAPIService
import com.example.nearbyplacesapp.viewmodel.ListViewModel
import dagger.Component


@Component(modules = [APIModule::class])
 interface APIComponent{
     fun inject(GoogleAPIService: GoogleAPIService)
     fun inject(viewModel: ListViewModel)

}