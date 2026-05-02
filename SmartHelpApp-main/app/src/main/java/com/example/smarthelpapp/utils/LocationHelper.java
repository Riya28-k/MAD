package com.example.smarthelpapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

/**
 * Helper class to get device location using GPS
 */
public class LocationHelper implements LocationListener {

    private static final String TAG = "LocationHelper";
    private Context context;
    private LocationManager locationManager;
    private Location currentLocation;
    private LocationCallback callback;

    // Interface for location callbacks
    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onLocationError(String error);
    }

    /**
     * Constructor
     */
    public LocationHelper(Context context, LocationCallback callback) {
        this.context = context;
        this.callback = callback;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Start getting location updates
     */
    public void startLocationUpdates() {
        try {
            // Check if we have permission
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                callback.onLocationError("Location permission not granted");
                return;
            }

            // Try to get location from GPS first
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,      // minTime
                    0,      // minDistance
                    this
            );

            // Also try network provider as backup
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    this
            );

            Log.d(TAG, "Location updates started");

        } catch (Exception e) {
            callback.onLocationError("Error: " + e.getMessage());
            Log.e(TAG, "Error starting location updates", e);
        }
    }

    /**
     * Stop getting location updates
     */
    public void stopLocationUpdates() {
        try {
            locationManager.removeUpdates(this);
            Log.d(TAG, "Location updates stopped");
        } catch (Exception e) {
            Log.e(TAG, "Error stopping location updates", e);
        }
    }

    /**
     * Get current location
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Called when location changes
     */
    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
        Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
        callback.onLocationReceived(location);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "Provider enabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "Provider disabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Status changed: " + provider);
    }

    /**
     * Convert location to Google Maps URL
     */
    public static String getGoogleMapsLink(double latitude, double longitude) {
        return "https://maps.google.com/?q=" + latitude + "," + longitude;
    }
}