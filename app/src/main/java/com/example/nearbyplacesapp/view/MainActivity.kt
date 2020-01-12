package com.example.nearbyplacesapp.view

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbyplacesapp.NearbyPlacesApp
import com.example.nearbyplacesapp.R
import com.example.nearbyplacesapp.locationhelper.LocationProvider
import com.example.nearbyplacesapp.model.places.GooglePlaceResult
import com.example.nearbyplacesapp.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*


private const val LOCATION_PERMISSION = 22

class MainActivity : AppCompatActivity() {

    private var location: Location? = null

    private lateinit var locationProvider: LocationProvider

    private lateinit var viewModel: ListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as NearbyPlacesApp).appComponent!!.inject(this)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        locationProvider = LocationProvider(this)

        locationProvider.getLastLocation()

        restaurantList.layoutManager = LinearLayoutManager(this)
        addDivider(restaurantList)
        cafeList.layoutManager = LinearLayoutManager(this)
        addDivider(cafeList)
        barList.layoutManager = LinearLayoutManager(this)
        addDivider(barList)

        locationProvider.getLastLocation()!!.observe(this, Observer<Location?> {
            location = it
            viewModel.refresh(location!!)

        })

        swipeRefreshLayout.setOnRefreshListener {
            if (viewModel != null && location != null) {
                viewModel.refresh(location!!)
                swipeRefreshLayout.isRefreshing = false
            }

        }

        observerViewModel()

    }

    //Add divider to recycler view
    private fun addDivider(recyclerView: RecyclerView) {
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context, RecyclerView.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    //Subscribe the UI elements to viewmodel to update when data is available
    private fun observerViewModel() {

        viewModel.loading.observe(this,
            Observer<Boolean> {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    restaurantContainer.visibility = View.GONE
                    cafeContainer.visibility = View.GONE
                    barContainer.visibility = View.GONE

                } else {
                    progressBar.visibility = View.GONE
                    restaurantContainer.visibility = View.VISIBLE
                    cafeContainer.visibility = View.VISIBLE
                    barContainer.visibility = View.VISIBLE

                }
            })

        viewModel.restaurants.observe(this,
            Observer<List<GooglePlaceResult>> { t ->
                if (t.isNotEmpty()) {
                    restaurantList.adapter = RecyclerViewAdapter(t)
                }
            })

        viewModel.cafe.observe(this,
            Observer<List<GooglePlaceResult>> { t ->
                if (t.isNotEmpty()) {
                    cafeList.adapter = RecyclerViewAdapter(t)
                }
            })

        viewModel.bar.observe(this,
            Observer<List<GooglePlaceResult>> { t ->
                if (t.isNotEmpty()) {
                    barList.adapter = RecyclerViewAdapter(t)
                }
            })

        viewModel.placesLoadError.observe(this, Observer {
            if (it) {
                placesError.visibility = View.VISIBLE
                restaurantContainer.visibility = View.GONE
                cafeContainer.visibility = View.GONE
                barContainer.visibility = View.GONE
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    locationProvider.requestNewLocationData()
                }
                return
            }

        }
    }
}
