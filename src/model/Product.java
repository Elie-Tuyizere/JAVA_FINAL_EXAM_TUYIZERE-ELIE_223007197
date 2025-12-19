package model;

import java.time.LocalDate;

public class Product {
    private int productID;
    private int cropID;
    private String name;
    private String category;
    private double quantity;
    private double unitPrice;
    private String description;
    private String status;
    private LocalDate listedDate;
    
    // Constructor for creating new product
    public Product(int cropID, String name, String category, double quantity, 
                   double unitPrice, String description, String status, LocalDate listedDate) {
        this.cropID = cropID;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.description = description;
        this.status = status;
        this.listedDate = listedDate;
    }
    
    // Getters and setters
    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }
    
    public int getCropID() { return cropID; }
    public void setCropID(int cropID) { this.cropID = cropID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getListedDate() { return listedDate; }
    public void setListedDate(LocalDate listedDate) { this.listedDate = listedDate; }
    
    // Business logic methods
    public double getTotalValue() {
        return quantity * unitPrice;
    }
    
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status) && quantity > 0;
    }
}