package com.example.nearbyplacesapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbyplacesapp.NearbyPlacesApp
import com.example.nearbyplacesapp.R
import com.example.nearbyplacesapp.locationhelper.LocationProvider
import com.example.nearbyplacesapp.model.places.CustomA
import com.example.nearbyplacesapp.viewmodel.ListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


private const val LOCATION_PERMISSION = 22

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var location: Location? = null

    private var locationProvider: LocationProvider? = null

    private lateinit var restaurantAdapter: RecyclerViewAdapter

    private lateinit var cafeAdapter: RecyclerViewAdapter

    private lateinit var barAdapter: RecyclerViewAdapter

    private var viewModel: ListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as NearbyPlacesApp).component!!.inject(this)

        locationProvider = LocationProvider(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)


        restaurantList!!.layoutManager = LinearLayoutManager(this)
        addDivider(restaurantList)
        cafeList!!.layoutManager = LinearLayoutManager(this)
        addDivider(cafeList)
        barList!!.layoutManager = LinearLayoutManager(this)
        addDivider(barList)

        swipeRefreshLayout!!.setOnRefreshListener {
            if (viewModel != null) {
                viewModel!!.refresh(location!!)
                swipeRefreshLayout!!.isRefreshing = false
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

        viewModel!!.loading.observe(this,
            Observer<Boolean> {
                if (it) {
                    progressBar!!.visibility = View.VISIBLE
                    restaurantContainer.visibility = View.GONE
                    cafeContainer.visibility = View.GONE
                    barContainer.visibility = View.GONE
                } else {
                    progressBar!!.visibility = View.GONE
                    restaurantContainer.visibility = View.VISIBLE
                    cafeContainer.visibility = View.VISIBLE
                    barContainer.visibility = View.VISIBLE
                }
            })

        viewModel!!.restaurants.observe(this,
            Observer<List<CustomA>> { t ->
                if (t.isNotEmpty()) {
                    restaurantAdapter = RecyclerViewAdapter(t)
                    restaurantList!!.adapter = restaurantAdapter
                }
            })

        viewModel!!.cafe.observe(this,
            Observer<List<CustomA>> { t ->
                if (t.isNotEmpty()) {
                    cafeAdapter = RecyclerViewAdapter(t)
                    cafeList!!.adapter = cafeAdapter
                }
            })

        viewModel!!.bar.observe(this,
            Observer<List<CustomA>> { t ->
                if (t.isNotEmpty()) {
                    barAdapter = RecyclerViewAdapter(t)
                    barList!!.adapter = barAdapter
                }
            })

        viewModel!!.placesLoadError.observe(this, Observer {
            if (it) {
                placesError!!.visibility = View.VISIBLE
                restaurantContainer.visibility = View.GONE
                cafeContainer.visibility = View.GONE
                barContainer.visibility = View.GONE
            } else {
                placesError!!.visibility = View.GONE
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (locationProvider!!.checkPermissions()) {
            if (locationProvider!!.isLocationEnabled()) {

                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        locationProvider!!.requestNewLocationData()
                    }

                    this.location = location

                    if (location != null) {
                        viewModel!!.refresh(location)
                    }

                }
            } else {
                Toast.makeText(this, getString(R.string.location_off), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION
            )

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    locationProvider!!.requestNewLocationData()
                }
                return
            }

        }
    }
}
