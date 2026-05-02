package com.example.smarthelpapp.models;

/**
 * Contact Model Class
 * Represents an emergency contact with ID, name, and phone number
 */
public class Contact {

    private int id;           // Unique identifier
    private String name;      // Contact name
    private String phone;     // Phone number

    // Constructor 1: With ID (used when retrieving from database)
    public Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Constructor 2: Without ID (used when creating new contact)
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Setter for Name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for Phone
    public String getPhone() {
        return phone;
    }

    // Setter for Phone
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // toString method (useful for debugging)
    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}