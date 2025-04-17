package com.example.contactsapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.LayoutInflater

class CountryCodeAdapter(
    context: Context,
    private val countries: List<CountryCode>
) : ArrayAdapter<CountryCode>(context, android.R.layout.simple_spinner_item, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val country = countries[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${country.flag} ${country.name} (${country.code})"

        return view
    }
}
