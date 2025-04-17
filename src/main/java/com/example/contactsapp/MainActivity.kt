package com.example.contactsapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.contactsapp.auth.LoginActivity
import com.example.contactsapp.model.Contact
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ContactListFragment.ContactClickListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Contacts" // 👈 You can change this title as needed

        if (FirebaseAuth.getInstance().currentUser == null) {
            navigateToLogin()
        } else {
            Toast.makeText(this, "Welcome back, ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
        }

        drawerLayout = findViewById(R.id.drawer_menu)
        navView = findViewById(R.id.nav_view)
        fab = findViewById(R.id.fab_add_contact)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, // 👈 Pass your toolbar here
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // Load default fragment
        if (auth.currentUser != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactListFragment())
                .commit()
        }

        fab.setOnClickListener {
            val dialog = AddContactDialog(onContactAdded = {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is ContactListFragment) {
                    val updatedContacts = ContactManager.getContacts()
                    fragment.updateContacts(updatedContacts)
                    android.util.Log.d("MainActivity", "Contact list updated with ${updatedContacts.size} items")
                }
            })
            dialog.show(supportFragmentManager, "AddContactDialog")
        }



    }


    private fun navigateToLogin() {
        // Redirect to LoginActivity if user is not authenticated
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_contacts -> {
                Toast.makeText(this, "Contacts Clicked", Toast.LENGTH_SHORT).show()
                // Future: Navigate to main contact list or scroll to top
            }
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show()
                // TODO: Open Profile Fragment
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()
                // TODO: Open Settings Fragment
            }
            R.id.nav_switch_user -> {
                auth.signOut()
                navigateToLogin() // Sign out and redirect to login
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onContactSelected(contact: Contact) {
        // Handle contact click here (e.g., navigate to contact details)
        Toast.makeText(this, "Selected Contact: ${contact.name}", Toast.LENGTH_SHORT).show()
        // You can replace this with logic to open a detailed view of the contact
    }

    override fun onContactLongPressed(contact: Contact, isToggling: Boolean) {
        // Handle contact long press here (e.g., toggle selection for bulk actions)
        Toast.makeText(this, "Long Pressed Contact: ${contact.name}, Toggling: $isToggling", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
