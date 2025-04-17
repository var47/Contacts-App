package com.example.contactsapp.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R
import com.example.contactsapp.ui.AuthSelectorActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var registerButton: Button
    private lateinit var loginRedirectButton: Button
    private lateinit var passwordStrengthView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_register)

        auth = FirebaseAuth.getInstance()

        emailField = findViewById(R.id.emailEditText)
        passwordField = findViewById(R.id.passwordEditText)
        confirmPasswordField = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginRedirectButton = findViewById(R.id.loginRedirectButton)
        passwordStrengthView = findViewById(R.id.passwordStrengthTextView)

        passwordField.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun afterTextChanged(s: Editable?) {
                updatePasswordStrengthView(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        goToMain()
                    } else {
                        Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginRedirectButton.setOnClickListener {
            startActivity(Intent(this, AuthSelectorActivity::class.java))
            finish()
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updatePasswordStrengthView(password: String) {
        val strength = calculatePasswordStrength(password)
        passwordStrengthView.text = strength.first
        passwordStrengthView.setTextColor(strength.second)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun calculatePasswordStrength(password: String): Pair<String, Int> {
        var strength = 0
        if (password.length >= 8) strength++
        if (password.any { it.isDigit() }) strength++
        if (password.any { it.isUpperCase() }) strength++
        if (password.any { it.isLowerCase() }) strength++
        if (password.any { !it.isLetterOrDigit() }) strength++

        return when (strength) {
            in 0..1 -> "Very Weak" to resources.getColor(R.color.red, null)
            2 -> "Weak" to resources.getColor(R.color.orange, null)
            3 -> "Medium" to resources.getColor(R.color.yellow, null)
            4 -> "Strong" to resources.getColor(R.color.light_green, null)
            else -> "Very Strong" to resources.getColor(R.color.green, null)
        }
    }
}
