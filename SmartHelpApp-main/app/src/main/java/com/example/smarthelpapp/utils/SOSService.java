package com.example.smarthelpapp.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.smarthelpapp.database.DatabaseHelper;
import com.example.smarthelpapp.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to handle SOS alert - combines location and SMS
 */
public class SOSService {

    private static final String TAG = "SOSService";
    private Context context;
    private DatabaseHelper dbHelper;
    private SMSHelper smsHelper;
    private LocationHelper locationHelper;
    private SOSCallback callback;

    // Interface for SOS callbacks
    public interface SOSCallback {
        void onSOSStarted();
        void onLocationFound(Location location);
        void onSMSSent();
        void onError(String error);
    }

    /**
     * Constructor
     */
    public SOSService(Context context, SOSCallback callback) {
        this.context = context;
        this.callback = callback;
        this.dbHelper = new DatabaseHelper(context);
        this.smsHelper = new SMSHelper(context);
    }

    /**
     * Activate SOS alert - get location and send SMS to all contacts
     */
    public void activateSOS() {
        Log.d(TAG, "SOS Activated!");
        callback.onSOSStarted();

        // Check if contacts exist
        List<Contact> contactList = dbHelper.getAllContacts();
        if (contactList.isEmpty()) {
            callback.onError("No emergency contacts added. Please add contacts first.");
            return;
        }

        // Start getting location
        locationHelper = new LocationHelper(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                Log.d(TAG, "Location received: " + location.getLatitude() + ", " + location.getLongitude());
                callback.onLocationFound(location);

                // Create Google Maps link
                String mapsLink = LocationHelper.getGoogleMapsLink(location.getLatitude(), location.getLongitude());

                // Create SOS message
                String sosMessage = SMSHelper.createSOSMessage(mapsLink);

                // Get all phone numbers from contacts
                List<String> phoneNumbers = new ArrayList<>();
                for (Contact contact : contactList) {
                    phoneNumbers.add(contact.getPhone());
                }

                // Send SMS to all contacts
                smsHelper.sendSMSToMultiple(phoneNumbers, sosMessage);
                Log.d(TAG, "SMS sent to " + phoneNumbers.size() + " contacts");
                callback.onSMSSent();

                // Stop location updates
                locationHelper.stopLocationUpdates();
            }

            @Override
            public void onLocationError(String error) {
                Log.e(TAG, "Location error: " + error);
                callback.onError(error);
            }
        });

        // Start getting location
        locationHelper.startLocationUpdates();
    }

    /**
     * Stop SOS (cancel location updates)
     */
    public void stopSOS() {
        if (locationHelper != null) {
            locationHelper.stopLocationUpdates();
        }
        Log.d(TAG, "SOS Stopped");
    }
}