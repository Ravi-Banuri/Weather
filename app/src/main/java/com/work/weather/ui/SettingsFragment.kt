package com.work.weather.ui

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.work.weather.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_prefernce, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setting_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}