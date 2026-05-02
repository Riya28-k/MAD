package com.example.smarthelpapp.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthelpapp.R;
import com.example.smarthelpapp.models.Contact;

import java.util.List;

/**
 * Adapter for RecyclerView - displays contacts in a list
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private OnContactClickListener listener;

    // Interface for button click callbacks
    public interface OnContactClickListener {
        void onEditClick(Contact contact);
        void onDeleteClick(int id);
    }

    // Constructor
    public ContactAdapter(List<Contact> contactList, OnContactClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    /**
     * Create new ViewHolder (called for each item)
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    /**
     * Bind data to ViewHolder (connect data with UI)
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        // Set contact name and phone
        holder.nameText.setText(contact.getName());
        holder.phoneText.setText(contact.getPhone());

        // Edit button click
        holder.editBtn.setOnClickListener(v -> listener.onEditClick(contact));

        // Delete button click
        holder.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(contact.getId()));
    }

    /**
     * Return number of items in list
     */
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    /**
     * ViewHolder class - holds references to UI elements
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText;
        Button editBtn, deleteBtn;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.contactName);
            phoneText = itemView.findViewById(R.id.contactPhone);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}