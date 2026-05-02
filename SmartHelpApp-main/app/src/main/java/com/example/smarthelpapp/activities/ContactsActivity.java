package com.example.smarthelpapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthelpapp.MainActivity;
import com.example.smarthelpapp.R;
import com.example.smarthelpapp.database.DatabaseHelper;
import com.example.smarthelpapp.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Contact> contactList;
    private FloatingActionButton addContactBtn;
    private LinearLayout emptyStateLayout;
    private TextView contactCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        recyclerView = findViewById(R.id.contactsRecyclerView);
        addContactBtn = findViewById(R.id.addContactBtn);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        contactCountText = findViewById(R.id.contactCountText);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load and display contacts
        loadContacts();

        // Add Contact Button
        addContactBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
            startActivity(intent);
        });

        // Navigation Buttons
        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.mapBtn).setOnClickListener(v -> {
            Toast.makeText(this, "Map feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Load contacts from database and update RecyclerView
     */
    private void loadContacts() {
        contactList = dbHelper.getAllContacts();

        if (contactList.isEmpty()) {
            // Show empty state
            emptyStateLayout.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(android.view.View.GONE);
            contactCountText.setText("0 contacts");
        } else {
            // Show list
            emptyStateLayout.setVisibility(android.view.View.GONE);
            recyclerView.setVisibility(android.view.View.VISIBLE);
            contactCountText.setText(contactList.size() + " contacts");

            // Create adapter with click listeners
            adapter = new ContactAdapter(contactList, new ContactAdapter.OnContactClickListener() {
                @Override
                public void onEditClick(Contact contact) {
                    Toast.makeText(ContactsActivity.this, "Edit: " + contact.getName(), Toast.LENGTH_SHORT).show();
                    // TODO: Open edit activity
                }

                @Override
                public void onDeleteClick(int id) {
                    dbHelper.deleteContact(id);
                    Toast.makeText(ContactsActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    loadContacts(); // Refresh list
                }
            });

            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Called when activity is resumed (refreshes contact list)
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadContacts(); // Refresh when returning to this activity
    }
}