package com.example.nearbyplacesapp

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nearbyplacesapp.model.api.GoogleAPIService
import com.example.nearbyplacesapp.model.places.*
import com.example.nearbyplacesapp.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import java.util.concurrent.Executor


class ListViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var googleAPIService: GoogleAPIService

    @InjectMocks
    lateinit var listViewModel: ListViewModel

    private lateinit var testSingle: Single<List<GooglePlaces>>

    @Before
    fun setup() {
        initMocks(this)
    }

    @Test
    fun getPlacesSuccess() {

        val locationA = LocationA("40.7463956", "-73.9852992")
        val openingHours = OpeningHours(true)
        val geometry = Geometry(locationA)

        val customA1 =
            CustomA(geometry, "401 Park Ave S, New York", "McDonald's", "3.6", openingHours)
        val customA2 =
            CustomA(geometry, "402 Park Ave S, New York", "McDonald's", "3.7", openingHours)
        val customA3 =
            CustomA(geometry, "403 Park Ave S, New York", "McDonald's", "3.8", openingHours)

        val customAPlacesList: ArrayList<CustomA> = ArrayList()

        customAPlacesList.add(customA1)
        customAPlacesList.add(customA2)
        customAPlacesList.add(customA3)

        val googlePlaces1 = GooglePlaces(customAPlacesList)
        val googlePlaces2 = GooglePlaces(customAPlacesList)
        val googlePlaces3 = GooglePlaces(customAPlacesList)

        val googlePlacesList: ArrayList<GooglePlaces> = ArrayList()
        googlePlacesList.add(googlePlaces1)
        googlePlacesList.add(googlePlaces2)
        googlePlacesList.add(googlePlaces3)

        testSingle = Single.just(googlePlacesList)

        val location = mock(Location::class.java)
        Mockito.`when`(googleAPIService.getPlaces(location)).thenReturn(testSingle)


        listViewModel.refresh(location)

        Assert.assertEquals(3, listViewModel.restaurants.value!!.size)
        Assert.assertEquals(false, listViewModel.placesLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)


    }

    @Test
    fun getPlacesFail() {
        val location = mock(Location::class.java)
        testSingle =
            Single.error<List<GooglePlaces>>(Throwable())
        Mockito.`when`<Any>(googleAPIService.getPlaces(location)).thenReturn(testSingle)
        listViewModel.refresh(location)
        Assert.assertEquals(true, listViewModel.placesLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate: Scheduler = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorWorker(
                    Executor { runnable: Runnable -> runnable.run() },
                    true
                )
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }
}