package com.example.contactsapp.adapter

import android.os.Build
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.R
import com.example.contactsapp.model.Contact

class ContactAdapter(
    private var contactList: List<Contact>,
    private val onContactClicked: (Contact) -> Unit,
    private val onContactLongPressed: (Contact, Boolean) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val selectedContacts = mutableSetOf<Contact>()
    private var expandedPosition = RecyclerView.NO_POSITION

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val numberText: TextView = itemView.findViewById(R.id.contact_number)
        val container: View = itemView.findViewById(R.id.contact_container) // <- Changed from LinearLayout to View
        val checkBox: CheckBox = itemView.findViewById(R.id.contact_checkbox)
        val expandedSection: LinearLayout = itemView.findViewById(R.id.expanded_section)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount() = contactList.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.nameText.text = contact.name

        val number = contact.phoneNumbers.entries.firstOrNull()
        holder.numberText.text = number?.let { (phone, note) ->
            if (note.isNotEmpty()) "$phone ($note)" else phone
        } ?: "No Number"

        holder.checkBox.visibility = if (selectedContacts.isNotEmpty()) View.VISIBLE else View.GONE
        holder.checkBox.isChecked = selectedContacts.contains(contact)

        // Expand/Collapse logic
        val isExpanded = position == expandedPosition
        holder.expandedSection.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.expandedSection.removeAllViews()

        if (isExpanded) {
            for ((phone, note) in contact.phoneNumbers) {
                val tv = TextView(holder.itemView.context)
                tv.text = if (note.isNotEmpty()) "$phone ($note)" else phone
                tv.setTextAppearance(android.R.style.TextAppearance_Medium)
                tv.setPadding(0, 4, 0, 4)
                holder.expandedSection.addView(tv)
            }

            // Apply fade-in animation
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 300
            holder.expandedSection.startAnimation(fadeIn)
        }

        holder.itemView.setOnClickListener {
            if (selectedContacts.isNotEmpty()) {
                toggleSelection(contact)
                onContactLongPressed(contact, true)
                notifyItemChanged(position)
            } else {
                val previousExpanded = expandedPosition
                expandedPosition = if (isExpanded) RecyclerView.NO_POSITION else position
                notifyItemChanged(previousExpanded)
                notifyItemChanged(expandedPosition)
                onContactClicked(contact)
            }
        }

        holder.itemView.setOnLongClickListener {
            toggleSelection(contact)
            onContactLongPressed(contact, false)
            notifyItemChanged(position)
            true
        }
    }

    private fun toggleSelection(contact: Contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact)
        } else {
            selectedContacts.add(contact)
        }
    }

    fun getSelectedContacts(): List<Contact> = selectedContacts.toList()

    fun clearSelection() {
        selectedContacts.clear()
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Contact>) {
        contactList = newList
        notifyDataSetChanged()
    }
}
