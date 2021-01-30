package com.work.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.work.weather.R
import com.work.weather.db.City
import com.work.weather.db.CityDataBase
import com.work.weather.util.Coroutines
import kotlinx.android.synthetic.main.fragment_add_city.*
import java.util.*


class AddCityFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var mMap: GoogleMap
    private  var cityName:String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onPause() {
        super.onPause()
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map.onCreate(savedInstanceState)
        map.onResume()
        map.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        btn_save_city.setOnClickListener {
            saveCity()
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        checkPermissionsAndShowLocation()
        mMap.setOnMapClickListener {
            var markerOptions= MarkerOptions()
            markerOptions.position(it)
            markerOptions.title("${it.latitude}")

            mMap.clear()
            mMap.addMarker(markerOptions)
            val geocoder = Geocoder(activity, Locale.getDefault())
            var address :List<Address>? = null
            var lat = it.latitude
            var longi = it.longitude
            address = geocoder.getFromLocation(lat, longi, 1)

            address?.let {
                cityName = it.get(0).locality
                latitude = lat
                longitude = longi
            }

        }
    }

    private fun saveCity() {
        cityName?.let {
            val city = City(cityName!!, latitude.toString(), longitude.toString())
            Coroutines.doWorkInIo(
                { CityDataBase(requireActivity()).getCityDao().addCity(city) },
                {
                    val action = AddCityFragmentDirections.actionSavedCity()
                    Navigation.findNavController(requireView()).navigate(action)
                })
            return
        }
        Snackbar.make(requireView(), "Select a city", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

    fun checkPermissionsAndShowLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
                requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
            } else {

                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100) {
            checkPermissionsAndShowLocation()
        }
    }


    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }

                val latLng = LatLng(location.latitude, location.longitude)

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
            }
        }
    }



}