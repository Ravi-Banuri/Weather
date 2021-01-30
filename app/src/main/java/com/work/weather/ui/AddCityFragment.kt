package com.work.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.work.weather.R
import com.work.weather.db.City
import com.work.weather.db.CityDataBase
import com.work.weather.util.Coroutines
import kotlinx.android.synthetic.main.fragment_add_city.*
import java.util.*


class AddCityFragment : Fragment(), OnMapReadyCallback {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map.onCreate(savedInstanceState)
        map.onResume()
        map.getMapAsync(this)

        btn_save_city.setOnClickListener {
            saveCity()
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
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

        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ){
            requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        } else {
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


}