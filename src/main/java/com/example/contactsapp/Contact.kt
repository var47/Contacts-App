package com.example.contactsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val phoneNumbers: Map<String, String> = emptyMap() // Map<PhoneNumber, Note>
) : Parcelable
