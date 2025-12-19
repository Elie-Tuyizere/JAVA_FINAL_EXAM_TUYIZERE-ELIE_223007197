package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Crop {
    private int cropID;
    private int fieldID;
    private String name;
    private String description;
    private String category;
    private double priceOrValue;
    private String status;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
    private LocalDateTime createdAt;
    
    public Crop() {}
    
    public Crop(int fieldID, String name, String description, String category, 
                double priceOrValue, String status, LocalDate plantingDate, 
                LocalDate expectedHarvestDate) {
        this.fieldID = fieldID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.priceOrValue = priceOrValue;
        this.status = status;
        this.plantingDate = plantingDate;
        this.expectedHarvestDate = expectedHarvestDate;
    }
    
    // Getters and Setters
    public int getCropID() { return cropID; }
    public void setCropID(int cropID) { this.cropID = cropID; }
    
    public int getFieldID() { return fieldID; }
    public void setFieldID(int fieldID) { this.fieldID = fieldID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPriceOrValue() { return priceOrValue; }
    public void setPriceOrValue(double priceOrValue) { this.priceOrValue = priceOrValue; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getPlantingDate() { return plantingDate; }
    public void setPlantingDate(LocalDate plantingDate) { this.plantingDate = plantingDate; }
    
    public LocalDate getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Crop{cropID=" + cropID + ", name='" + name + "', status='" + status + "'}";
    }
}