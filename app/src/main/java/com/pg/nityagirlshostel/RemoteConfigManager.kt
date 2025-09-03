package com.pg.nityagirlshostel

import android.annotation.SuppressLint
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object RemoteConfigManager {

    @SuppressLint("StaticFieldLeak")
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        remoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2200) // set sec
                .build()
        )

        remoteConfig.setDefaultsAsync(
            mapOf(
                "supabase_url" to "zsappqupcgctlxquncsz.supabase.co",
                "supabase_key" to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpzYXBwcXVwY2djdGx4cXVuY3N6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA0MDUxMjgsImV4cCI6MjA2NTk4MTEyOH0.lqYSIICdf_vHALlCvvx6C_TpcvaJEFgCB-pFymtyaRs",
                "supabase_version" to "1.0"
                )
        )
    }



    suspend fun fetchRemoteValues(): Boolean = withContext(Dispatchers.IO) {
        try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            false
        }
    }

    fun getSupabaseUrl(): String = remoteConfig.getString("supabase_url")
    fun getSupabaseKey(): String = remoteConfig.getString("supabase_key")

    fun getSupabaseVersion(): String = remoteConfig.getString("supabase_version")
}
