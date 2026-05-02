package com.example.smarthelpapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthelpapp.R;
import com.example.smarthelpapp.database.DatabaseHelper;
import com.example.smarthelpapp.models.Contact;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput;
    private Button saveBtn, cancelBtn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        // Save Button Click
        saveBtn.setOnClickListener(v -> saveContact());

        // Cancel Button Click
        cancelBtn.setOnClickListener(v -> finish());
    }

    /**
     * Validate and save contact to database
     */
    private void saveContact() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() < 10) {
            Toast.makeText(this, "Phone number must be at least 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create contact and insert to database
        Contact contact = new Contact(name, phone);
        long result = dbHelper.insertContact(contact);

        if (result != -1) {
            Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to contacts list
        } else {
            Toast.makeText(this, "Failed to save contact", Toast.LENGTH_SHORT).show();
        }
    }
}