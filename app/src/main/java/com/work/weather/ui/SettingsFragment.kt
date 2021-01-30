package com.work.weather.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.work.weather.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_prefernce, rootKey)
    }
}