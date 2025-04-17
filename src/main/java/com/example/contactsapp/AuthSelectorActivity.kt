package com.example.contactsapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsapp.auth.LoginActivity
import com.example.contactsapp.auth.RegisterActivity
import com.example.contactsapp.databinding.ActivityAuthSelectorBinding

class AuthSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
