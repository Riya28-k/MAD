package com.example.smarthelpapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.smarthelpapp.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Helper Class for SQLite Database
 * Handles all database operations: Create, Insert, Update, Delete, Retrieve
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "smart_help.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_CONTACTS = "contacts";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";

    // Tag for logging
    private static final String TAG = "DatabaseHelper";

    /**
     * Constructor
     * @param context - Activity context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL command to create contacts table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_PHONE + " TEXT NOT NULL" +
                ")";

        db.execSQL(CREATE_TABLE);
        Log.d(TAG, "Database created successfully");
    }

    /**
     * Called when database version is upgraded
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create new table
        onCreate(db);
        Log.d(TAG, "Database upgraded");
    }

    /**
     * INSERT: Add a new contact to the database
     * @param contact - Contact object to add
     * @return - ID of inserted contact (-1 if failed)
     */
    public long insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues (like a Map for column-value pairs)
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());

        // Insert and get the ID
        long result = db.insert(TABLE_CONTACTS, null, values);
        db.close();

        if (result != -1) {
            Log.d(TAG, "Contact inserted: " + contact.getName());
        } else {
            Log.e(TAG, "Failed to insert contact");
        }

        return result;
    }

    /**
     * RETRIEVE: Get all contacts from database
     * @return - List of all Contact objects
     */
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all rows
        String SELECT_ALL = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);

                Contact contact = new Contact(id, name, phone);
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Retrieved " + contactList.size() + " contacts");
        return contactList;
    }

    /**
     * RETRIEVE: Get a single contact by ID
     * @param id - Contact ID
     * @return - Contact object or null if not found
     */
    public Contact getContactById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_BY_ID = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(SELECT_BY_ID, null);

        Contact contact = null;
        if (cursor.moveToFirst()) {
            int contactId = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);

            contact = new Contact(contactId, name, phone);
        }

        cursor.close();
        db.close();

        return contact;
    }

    /**
     * UPDATE: Update an existing contact
     * @param contact - Contact object with updated data
     * @return - Number of rows updated
     */
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());

        // Update by ID
        int result = db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

        db.close();

        if (result > 0) {
            Log.d(TAG, "Contact updated: " + contact.getName());
        } else {
            Log.e(TAG, "Failed to update contact");
        }

        return result;
    }

    /**
     * DELETE: Delete a contact by ID
     * @param id - Contact ID to delete
     * @return - Number of rows deleted
     */
    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();

        if (result > 0) {
            Log.d(TAG, "Contact deleted with ID: " + id);
        } else {
            Log.e(TAG, "Failed to delete contact");
        }

        return result;
    }

    /**
     * Get total number of contacts
     * @return - Count of contacts
     */
    public int getContactCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String COUNT_QUERY = "SELECT COUNT(*) FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(COUNT_QUERY, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }
}