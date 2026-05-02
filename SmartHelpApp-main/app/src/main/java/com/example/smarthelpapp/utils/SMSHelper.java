package com.example.smarthelpapp.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to send SMS messages
 */
public class SMSHelper {

    private static final String TAG = "SMSHelper";
    private Context context;

    /**
     * Constructor
     */
    public SMSHelper(Context context) {
        this.context = context;
    }

    /**
     * Send SMS to a single number
     * @param phoneNumber - Recipient phone number
     * @param message - Message text
     */
    public void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            // Split message if longer than 160 characters
            if (message.length() > 160) {
                List<String> messageParts = smsManager.divideMessage(message);
                // Convert List to ArrayList
                ArrayList<String> messagePartsArrayList = new ArrayList<>(messageParts);
                smsManager.sendMultipartTextMessage(phoneNumber, null, messagePartsArrayList, null, null);
                Log.d(TAG, "Multipart SMS sent to " + phoneNumber);
            } else {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Log.d(TAG, "SMS sent to " + phoneNumber);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error sending SMS", e);
        }
    }

    /**
     * Send SMS to multiple contacts
     * @param phoneNumbers - List of phone numbers
     * @param message - Message text
     */
    public void sendSMSToMultiple(List<String> phoneNumbers, String message) {
        for (String phoneNumber : phoneNumbers) {
            sendSMS(phoneNumber, message);
        }
        Log.d(TAG, "SMS sent to " + phoneNumbers.size() + " contacts");
    }

    /**
     * Create SOS message with location link
     * @param mapsLink - Google Maps link
     * @return - Formatted SOS message
     */
    public static String createSOSMessage(String mapsLink) {
        return "🚨 EMERGENCY ALERT! 🚨\n\n" +
                "I need urgent help!\n\n" +
                "My current location:\n" +
                mapsLink + "\n\n" +
                "Please call me immediately.\n" +
                "This is an automated message from SmartHelpApp.";
    }
}