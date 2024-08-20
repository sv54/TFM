package com.example.tfm.fragments

import ApiService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tfm.R
import com.example.tfm.activities.LoginRegisterActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        val idiomaActual = sharedPreferences.getString("PreferencesLanguage", getSystemLanguage(requireContext()))
        val temaActual = sharedPreferences.getString("PreferencesTheme", "system")

        Log.i("tagg", "idioma actual = " + idiomaActual)
        Log.i("tagg", "idioma configuracin actual = " + getSystemLanguage(requireContext()))
        Log.i("tagg", "tema actual = " + temaActual)

        if(getSystemLanguage(requireContext()) != idiomaActual){
            setLocale(idiomaActual!!)
        }
//        if(sharedPreferences.getString("PreferencesLanguage", "es")!! != "es")
//        setLocale(sharedPreferences.getString("PreferencesLanguage", "es")!!)
//        setTheme(sharedPreferences.getString("PreferencesTheme", "system")!!)
        val languagePreference: ListPreference? = findPreference("language_preference")
        val themePreference: ListPreference? = findPreference("theme_preference")
        val deleteAccount: Preference? = findPreference("delete_account")

        languagePreference!!.setDefaultValue(sharedPreferences.getString("PreferencesLanguage", "es"))

        deleteAccount?.setOnPreferenceClickListener {
            showDeleteAccountDialog()
            true
        }

        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val newLocale = newValue as String
            setLocale(newLocale)
            true
        }

        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val newTheme = newValue as String
            setTheme(newTheme)
            true
        }
    }

    fun getSystemLanguage(context: Context): String {
        val configuration = context.resources.configuration
        val locale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            configuration.locale
        }
        return locale.language
    }


    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_account_title)
            .setMessage(R.string.delete_account_summary)
            .setPositiveButton(R.string.delete) { dialog, which ->
                // Handle account deletion here
                // e.g., show a toast or proceed with deletion
                deleteUser()
                clearAllPreferences()
                val intent = Intent(context, LoginRegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNeutralButton(""){dialog, which ->

            }
            .setNegativeButton(R.string.cancel) { dialog, which ->
                // Handle cancellation
                dialog.dismiss()
            }
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    private fun deleteUser(){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.deleteUser(sharedPreferences.getInt("UserId", -1))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // Manejar la respuesta exitosa
                    Log.i("tagg", "Usuario eliminado correctamente: ${response.body()}")
                } else {
                    // Manejar errores en la respuesta
                    Log.e("tagg", "Error al eliminar usuario: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Manejar errores en la solicitud
                Log.e("tagg", "Error de conexión o servidor: ${t.message}")
            }
        })
    }
    private fun clearAllPreferences() {
        sharedPreferences.edit()?.clear()?.apply()
    }
    private fun setLocale(localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        val editor = sharedPreferences.edit()
        editor.putString("PreferencesLanguage", localeCode)
        editor.apply()
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        requireActivity().recreate()
    }

    private fun setTheme(theme: String) {
        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        val editor = sharedPreferences.edit()
        editor.putString("PreferencesTheme", theme)
        editor.apply()
    }
}