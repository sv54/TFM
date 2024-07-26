package com.example.tfm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.tfm.R
import com.example.tfm.fragments.DestinoFragment
import com.example.tfm.fragments.ProfileFragment

class DestinoActivity : AppCompatActivity() {

    private val destinoIdExtra = "DESTINOID"
    private val destinoTituloExtra = "DESTINOTITULO"
    private var destinoId = -1
    private var destinoTitulo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destino)

        destinoId = intent.getIntExtra(destinoIdExtra, -1)
        destinoTitulo = intent.getStringExtra(destinoTituloExtra)

        if(destinoId == -1 || destinoTitulo == null){
            onBackPressedDispatcher.onBackPressed()
        }

        setSupportActionBar(findViewById(R.id.my_toolbar_destino))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = destinoTitulo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = DestinoFragment()

            val bundle = Bundle()
            bundle.putInt("destino_id", destinoId)

            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_destino, fragment)
                .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el evento de clic del botón de la toolbar
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}