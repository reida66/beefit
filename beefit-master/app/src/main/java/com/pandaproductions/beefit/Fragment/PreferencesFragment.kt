package com.pandaproductions.beefit.Fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.pandaproductions.beefit.R

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
