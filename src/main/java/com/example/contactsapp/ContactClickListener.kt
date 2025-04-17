package com.example.contactsapp

import com.example.contactsapp.model.Contact

interface ContactClickListener {
    fun onContactClick(contact: Contact)
}
