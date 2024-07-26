package com.example.tfm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tfm.R
import com.example.tfm.fragments.CommentFragment
import com.example.tfm.fragments.PostCommentFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommentActivity : AppCompatActivity() {

    val destinoIdExtra = "DESTINOID"

    private lateinit var drawerLayout: DrawerLayout
    private var destinoId: Int = -1
    private lateinit var fabPost: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comnent)

        setSupportActionBar(findViewById(R.id.my_toolbar_comment))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        destinoId = intent.getIntExtra(destinoIdExtra, -1)

        fabPost = findViewById(R.id.fabPostComment)

        fabPost.setOnClickListener {
            val fragment = PostCommentFragment().apply {
                arguments = Bundle().apply {
                    putInt("destinoId", destinoId)
                }
            }
            fragment.show(supportFragmentManager, fragment.tag)
        }


        if(destinoId == -1)
            onBackPressedDispatcher.onBackPressed()

        val bundle = Bundle()
        bundle.putInt("destinoId", destinoId)

        val fragment = CommentFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit()

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