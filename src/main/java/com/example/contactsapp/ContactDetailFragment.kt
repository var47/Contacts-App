/*package com.example.contactsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.contactsapp.R
import com.example.contactsapp.model.Contact

class ContactDetailFragment : Fragment() {

    companion object {
        const val ARG_CONTACT = "arg_contact"
        fun newInstance(contact: Contact): ContactDetailFragment {
            val fragment = ContactDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CONTACT, contact)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contact = requireArguments().getParcelable(ARG_CONTACT)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        val phoneContainer = view.findViewById<LinearLayout>(R.id.phoneNumberContainer)
        val nameText = view.findViewById<TextView>(R.id.contactName)
        nameText.text = contact.name

        contact.phoneNumbers.forEach {
            val numberTextView = TextView(requireContext())
            numberTextView.text = if (it.note.isNotEmpty())
                "${it.number} (${it.note})"
            else
                it.number
            numberTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Medium)
            numberTextView.setPadding(16, 8, 16, 8)
            phoneContainer.addView(numberTextView)
        }

        return view
    }
}
*/