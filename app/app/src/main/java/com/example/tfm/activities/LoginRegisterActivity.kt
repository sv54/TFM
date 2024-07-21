package com.example.tfm.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.fragments.LoginFragment

class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        val sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        //sharedPreferences.edit().clear().apply()
        if(sharedPreferences.getInt("UserId", -1) > 0){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.LoginRegisterFragment, LoginFragment())
                .commit()
        }
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations( R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            .replace(R.id.LoginRegisterFragment, fragment)
            .addToBackStack(null)
            .commit()


    }
}