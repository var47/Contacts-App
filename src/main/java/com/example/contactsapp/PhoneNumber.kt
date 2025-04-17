package com.example.contactsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneNumber(
    val number: String,
    val note: String = ""
) : Parcelable
