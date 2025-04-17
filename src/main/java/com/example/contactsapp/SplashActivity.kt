package com.example.contactsapp.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R
import com.example.contactsapp.auth.LoginActivity
import com.example.contactsapp.ui.AuthSelectorActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUser = FirebaseAuth.getInstance().currentUser
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                // If user is logged in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // If user is not logged in, go to LoginActivity
                startActivity(Intent(this, AuthSelectorActivity::class.java))
            }
            finish() // Close splash screen
        }, 2000) // Delay splash screen for 2 seconds
    }
}
