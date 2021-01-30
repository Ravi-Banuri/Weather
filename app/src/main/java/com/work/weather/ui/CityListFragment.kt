package com.work.weather.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.work.weather.R
import com.work.weather.db.City
import com.work.weather.db.CityDataBase
import com.work.weather.util.CityListVmFactory
import kotlinx.android.synthetic.main.fragment_city_list.*


class CityListFragment : Fragment(), CityAdapter.CityDeleteClickListener {

    private lateinit var vmFactory: CityListVmFactory
    private lateinit var viewModel: CityListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_city_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> {
                    val action = CityListFragmentDirections.showHelpData()
                    Navigation.findNavController(requireView()).navigate(action)
                }
                R.id.settings -> {
                    val action = CityListFragmentDirections.showSettings()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        city_list_view.setHasFixedSize(true)
        city_list_view.layoutManager = LinearLayoutManager(activity)

        val dataBase = CityDataBase(requireActivity())
        vmFactory = CityListVmFactory(dataBase)
        viewModel = ViewModelProviders.of(this, vmFactory).get(CityListViewModel::class.java)
        viewModel.cityList.observe(viewLifecycleOwner, Observer { cityList ->
            city_list_view.also {
                it.setHasFixedSize(true)
                it.layoutManager = LinearLayoutManager(activity)
                it.adapter = CityAdapter(cityList, this)
            }
        })
        viewModel.deletedItems.observe(viewLifecycleOwner, Observer {
            try {
                Log.e("Weather", "deletedItems")
                val city = it.remove()
                city?.let {
                    Snackbar.make(requireView(), "Deleted ${city.city}", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            } catch (e:Exception) {

            }

        })
        btn_add_city.setOnClickListener {
            val action = CityListFragmentDirections.actionAddCity()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onDeleteClick(city: City) {
        viewModel.deleteCity(city)
    }
}