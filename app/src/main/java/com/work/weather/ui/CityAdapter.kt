package com.work.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.work.weather.R
import com.work.weather.databinding.CityListItemBinding
import com.work.weather.db.City

class CityAdapter(
    val cityList : List<City>,
    val deleteClickListener: CityDeleteClickListener) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun getItemCount(): Int = cityList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.city_list_item, parent,
                false)
        )

    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityListItemBinding.city = cityList[position]
        holder.cityListItemBinding.root.setOnClickListener{
            val action = CityListFragmentDirections.showWeatherDetails()
            action.city = cityList[position]
            Navigation.findNavController(it).navigate(action)
        }
        holder.cityListItemBinding.btnDelete.setOnClickListener{
            deleteClickListener.onDeleteClick(cityList[position])
        }

    }

    class CityViewHolder(val cityListItemBinding: CityListItemBinding ) : RecyclerView.ViewHolder(cityListItemBinding.root)

    interface CityDeleteClickListener {
        fun onDeleteClick(city: City)
    }
}