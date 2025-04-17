package com.example.contactsapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.adapter.ContactAdapter
import com.example.contactsapp.model.Contact

class ContactListFragment : Fragment() {

    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var listener: ContactClickListener
    private val contactList = mutableListOf<Contact>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ContactClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        contactsRecyclerView = view.findViewById(R.id.ContactRecyclerView)
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter = ContactAdapter(
            contactList,
            onContactClicked = { contact -> listener.onContactSelected(contact) }, // Pass contact click here
            onContactLongPressed = { contact, isToggling -> listener.onContactLongPressed(contact, isToggling) }
        )

        contactsRecyclerView.adapter = contactAdapter

        return view
    }

    fun updateContacts(newContacts: List<Contact>) {
        android.util.Log.d("ContactListFragment", "Updating contacts with ${newContacts.size} items")
        contactAdapter.updateList(newContacts)
    }


    // Interface for contact click handling
    interface ContactClickListener {
        fun onContactSelected(contact: Contact)
        fun onContactLongPressed(contact: Contact, isToggling: Boolean)
    }
}
