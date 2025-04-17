package com.example.contactsapp

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.contactsapp.model.Contact
import com.example.contactsapp.model.PhoneNumber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddContactDialog(private val onContactAdded: () -> Unit) : DialogFragment() {

    private lateinit var phoneNumberContainer: LinearLayout
    private lateinit var countryCodeSpinner: Spinner
    private val countryCodes = CountryCodeUtils.getAllCountryCodes()
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_add_contact, null)

        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val phoneEditText = view.findViewById<EditText>(R.id.editTextPhone)
        val noteEditText = view.findViewById<EditText>(R.id.editTextNote)
        val addButton = view.findViewById<Button>(R.id.buttonAddNumber)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveContact)
        phoneNumberContainer = view.findViewById(R.id.phoneNumberContainer)
        countryCodeSpinner = view.findViewById(R.id.countryCodeSpinner)

        val adapter = CountryCodeAdapter(requireContext(), countryCodes)
        countryCodeSpinner.adapter = adapter

        val phoneEntries = mutableListOf<Pair<String, String>>() // Pair<Phone, Note>

        addButton.setOnClickListener {
            val phone = phoneEditText.text.toString().trim()
            val note = noteEditText.text.toString().trim()
            if (phone.isNotEmpty()) {
                phoneEntries.add(Pair(phone, note))

                val entryView = TextView(context).apply {
                    text = "$phone ${if (note.isNotEmpty()) " - $note" else ""}"
                    setPadding(8, 4, 8, 4)
                }
                phoneNumberContainer.addView(entryView)
                phoneEditText.setText("")
                noteEditText.setText("")
            } else {
                Toast.makeText(context, "Enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            if (name.isEmpty() || phoneEntries.isEmpty()) {
                Toast.makeText(context, "Name and at least one phone number required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val countryCode = countryCodes[countryCodeSpinner.selectedItemPosition].code
            val numbers = phoneEntries.associate {
                "${countryCode}${it.first}" to it.second
            }
            val contact = Contact(name = name, phoneNumbers = numbers)

            ContactManager.addContact(contact)
            Log.d("AddContactDialog", "Contact added locally: $contact")
            Log.d("AddContactDialog", "All local contacts: ${ContactManager.getContacts()}")
            Toast.makeText(requireContext(), "Saved locally: ${contact.name}", Toast.LENGTH_SHORT).show()

            // Get the current authenticated user from Firebase Auth
// Safely access the context based on where the code is running (e.g., fragment or activity)
            val safeContext = context ?: requireContext() // This works for fragment, for activity use context

            val firebaseUser = FirebaseAuth.getInstance().currentUser

            if (firebaseUser != null) {
                // Save contact to Firestore if user is authenticated
                db.collection("contacts")
                    .document(firebaseUser.uid)
                    .collection("userContacts")
                    .add(contact)
                    .addOnSuccessListener {
                        // Ensure context is valid before showing Toast
                        Toast.makeText(safeContext, "Saved to cloud!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Ensure context is valid before showing Toast
                        Toast.makeText(safeContext, "Cloud save failed: ${e.message}", Toast.LENGTH_LONG).show()
                        android.util.Log.e("AddContactDialog", "Firestore save failed", e)
                    }
            } else {
                // Handle unauthenticated user case
                Toast.makeText(safeContext, "User not authenticated", Toast.LENGTH_SHORT).show()
                android.util.Log.e("AddContactDialog", "User not authenticated")
            }

            onContactAdded() // 👈 refresh contact list on save
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}
