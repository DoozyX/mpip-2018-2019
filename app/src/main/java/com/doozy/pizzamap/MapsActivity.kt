package com.doozy.pizzamap

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.doozy.pizzamap.models.GooglePlacesApiResponse
import com.doozy.pizzamap.models.Place
import com.doozy.pizzamap.repository.GooglePlacesApiInterface
import com.doozy.pizzamap.viewmodel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val REQUEST_ACCESS_FINE_LOCATION = 1
    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager
    private val mLocationViewModel: LocationViewModel = LocationViewModel(MutableLiveData())
    private var mGooglePlacesAliInterface: GooglePlacesApiInterface? = null
    private var firstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationViewModel.places.observe(this, Observer { places: List<Place>? ->
            run {
                places?.forEach { place: Place ->
                    run {
                        //mMap.clear()
                        mMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    place.geometry.location.lat,
                                    place.geometry.location.lng
                                )
                            ).title(place.name)
                        )
                    }
                }
            }
        })

        mGooglePlacesAliInterface = GoogleMapsApiClient.getRetrofit().create(GooglePlacesApiInterface::class.java)
    }

    override fun onStart() {
        super.onStart()
        startGPSUpdating()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocationIfPermitted()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startGPSUpdating()
    }

    private fun startGPSUpdating() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }

        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                if(firstTime) {
                    firstTime = false
                    val latlong = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
                }
                mGooglePlacesAliInterface?.listPlaces(
                    location.latitude.toString() + "," + location.longitude.toString(),
                    "AIzaSyD1qNrBFoW5ZZzxDb6i0Mm5sr-cxYynbXI"
                )?.enqueue(object : Callback<GooglePlacesApiResponse> {
                    override fun onResponse(
                        call: Call<GooglePlacesApiResponse>,
                        response: Response<GooglePlacesApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("RESPONSE::", response.toString())

                            if (response.body()!!.results != null) {
                                mLocationViewModel.places.postValue(response.body()!!.results)
                            } else {
                                Toast.makeText(this@MapsActivity, "FAILED SEARCH", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<GooglePlacesApiResponse>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "failed req", Toast.LENGTH_LONG).show()
                    }
                })
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

    }

    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        }
    }
}
