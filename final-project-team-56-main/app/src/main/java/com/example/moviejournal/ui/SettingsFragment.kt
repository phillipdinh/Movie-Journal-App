package com.example.moviejournal.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.moviejournal.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}