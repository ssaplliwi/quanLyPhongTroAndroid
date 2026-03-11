package com.example.myapplication.models;

import java.io.Serializable;

/**
 * Model class đại diện cho một phòng trọ.
 * Triển khai Serializable để có thể truyền qua Intent.
 */
public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isRented;
    private String tenantName;
    private String phoneNumber;

    public Room(String id, String name, double price, boolean isRented, String tenantName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isRented = isRented;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isRented() { return isRented; }
    public void setRented(boolean rented) { isRented = rented; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
