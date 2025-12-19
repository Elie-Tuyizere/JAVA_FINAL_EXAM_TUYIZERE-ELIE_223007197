package model;

import java.time.LocalDateTime;

public class Farmer {
    private int farmerID;
    private int userID;
    private String fullName;
    private String location;
    private String contact;
    private int experience;
    private LocalDateTime createdAt;
    
    public Farmer() {}
    
    public Farmer(int userID, String fullName, String location, String contact, int experience) {
        this.userID = userID;
        this.fullName = fullName;
        this.location = location;
        this.contact = contact;
        this.experience = experience;
    }
    
    // Getters and Setters
    public int getFarmerID() { return farmerID; }
    public void setFarmerID(int farmerID) { this.farmerID = farmerID; }
    
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Farmer{farmerID=" + farmerID + ", fullName='" + fullName + "', location='" + location + "'}";
    }
}