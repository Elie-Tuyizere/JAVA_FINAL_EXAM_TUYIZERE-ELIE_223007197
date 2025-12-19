package model;

import java.time.LocalDateTime;

public class Field {
    private int fieldID;
    private int farmerID;
    private String name;
    private String address;
    private double capacity;
    private String manager;
    private String contact;
    private double area;
    private LocalDateTime createdAt;
    
    public Field() {}
    
    public Field(int farmerID, String name, String address, double capacity, String manager, String contact, double area) {
        this.farmerID = farmerID;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.manager = manager;
        this.contact = contact;
        this.area = area;
    }
    
    // Getters and Setters
    public int getFieldID() { return fieldID; }
    public void setFieldID(int fieldID) { this.fieldID = fieldID; }
    
    public int getFarmerID() { return farmerID; }
    public void setFarmerID(int farmerID) { this.farmerID = farmerID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }
    
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Field{fieldID=" + fieldID + ", name='" + name + "', area=" + area + "}";
    }
}