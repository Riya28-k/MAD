package com.example.smarthelpapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.example.smarthelpapp.activities.ContactsActivity;
import com.example.smarthelpapp.utils.SOSService;

public class MainActivity extends AppCompatActivity {

    private Button sosButton, contactsBtn, mapBtn;
    private SOSService sosService;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        sosButton = findViewById(R.id.sosButton);
        contactsBtn = findViewById(R.id.contactsBtn);
        mapBtn = findViewById(R.id.mapBtn);

        // Request permissions
        requestPermissions();

        // Initialize SOS Service
        sosService = new SOSService(this, new SOSService.SOSCallback() {
            @Override
            public void onSOSStarted() {
                Toast.makeText(MainActivity.this, "🚨 SOS ACTIVATED!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationFound(Location location) {
                Toast.makeText(MainActivity.this,
                        "📍 Location found: " + location.getLatitude() + ", " + location.getLongitude(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSMSSent() {
                Toast.makeText(MainActivity.this, "✅ SOS alerts sent to all contacts!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "❌ Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // SOS Button Click Listener
        sosButton.setOnClickListener(v -> {
            sosService.activateSOS();
        });

        // Contacts Button Click Listener
        contactsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent);
            finish();
        });

        // Map Button Click Listener
        mapBtn.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Map feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Request runtime permissions for SMS and Location
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Handle permission results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "✅ Permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Permissions are required for SOS feature", Toast.LENGTH_SHORT).show();
            }
        }
    }
}