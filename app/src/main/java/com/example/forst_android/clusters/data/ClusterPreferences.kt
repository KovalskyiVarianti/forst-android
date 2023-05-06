package com.example.forst_android.clusters.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClusterPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CLUSTER_PREFERENCES = "CLUSTER_PREFERENCES"
        private const val SELECTED_CLUSTER_ID_KEY = "SELECTED_CLUSTER_ID_KEY"
    }

    private val Context.preferences by preferencesDataStore(name = CLUSTER_PREFERENCES)

    private val selectedClusterIdKey = stringPreferencesKey(SELECTED_CLUSTER_ID_KEY)

    suspend fun selectCluster(id: String) {
        context.preferences.edit { pref ->
            pref[selectedClusterIdKey] = id
        }
    }

    fun getSelectedClusterId() = context.preferences.data.map { pref ->
        pref[selectedClusterIdKey]
    }
}