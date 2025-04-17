package com.example.contactsapp

import com.example.contactsapp.model.Contact

object ContactManager {

    private val contactList = mutableListOf<Contact>()

    fun addContact(contact: Contact) {
        contactList.add(contact)
    }

    fun getContacts(): List<Contact> {
        return contactList
    }

    fun updateContact(index: Int, updatedContact: Contact) {
        if (index in contactList.indices) {
            contactList[index] = updatedContact
        }
    }

    fun deleteContact(index: Int) {
        if (index in contactList.indices) {
            contactList.removeAt(index)
        }
    }

    fun clearAllContacts() {
        contactList.clear()
    }

    fun setContactsFromCloud(contacts: List<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)
    }
}
