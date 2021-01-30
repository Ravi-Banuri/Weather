package com.work.weather.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.work.weather.R
import com.work.weather.databinding.FragmentWeatherDetailsBinding
import com.work.weather.databinding.FragmentWeatherDetailsBindingImpl
import com.work.weather.db.City
import com.work.weather.network.WeatherApi
import com.work.weather.repository.WeatherRepository
import com.work.weather.util.WeatherVmFactoty
import kotlinx.android.synthetic.main.fragment_weather_details.*


class WeatherDetailsFragment : Fragment() {

    private lateinit var databinding: FragmentWeatherDetailsBinding
    private lateinit var vmFactoty: WeatherVmFactoty
    private lateinit var viewModel: WeatherDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_weather_details, container, false
        )
        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repository = WeatherRepository(WeatherApi())

        var city: City? = null
        arguments?.let {
            city = WeatherDetailsFragmentArgs.fromBundle(it).city
            toolbar_layout.title = city?.city
        }

        vmFactoty = WeatherVmFactoty(this.requireActivity().application, repository, city!!)
        viewModel = ViewModelProviders.of(this, vmFactoty).get(WeatherDetailViewModel::class.java)
        databinding.weather = viewModel
        databinding.lifecycleOwner = viewLifecycleOwner
    }
}