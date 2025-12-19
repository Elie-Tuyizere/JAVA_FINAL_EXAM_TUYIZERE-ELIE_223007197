package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Harvest {
    private int harvestID;
    private int cropID;
    private LocalDate harvestDate;
    private double quantity;
    private String quality;
    private double price;
    private String notes;
    private LocalDateTime createdAt;
    
    public Harvest() {}
    
    public Harvest(int cropID, LocalDate harvestDate, double quantity, String quality, double price, String notes) {
        this.cropID = cropID;
        this.harvestDate = harvestDate;
        this.quantity = quantity;
        this.quality = quality;
        this.price = price;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getHarvestID() { return harvestID; }
    public void setHarvestID(int harvestID) { this.harvestID = harvestID; }
    
    public int getCropID() { return cropID; }
    public void setCropID(int cropID) { this.cropID = cropID; }
    
    public LocalDate getHarvestDate() { return harvestDate; }
    public void setHarvestDate(LocalDate harvestDate) { this.harvestDate = harvestDate; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Harvest{harvestID=" + harvestID + ", quantity=" + quantity + ", quality='" + quality + "'}";
    }
}