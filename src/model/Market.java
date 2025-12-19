package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Market {
    private int marketID;
    private int harvestID;
    private String marketName;
    private double price;
    private double quantitySold;
    private LocalDate saleDate;
    private String buyerName;
    private String contact;
    private LocalDateTime createdAt;
    
    public Market() {}
    
    public Market(int harvestID, String marketName, double price, double quantitySold, 
                  LocalDate saleDate, String buyerName, String contact) {
        this.harvestID = harvestID;
        this.marketName = marketName;
        this.price = price;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.buyerName = buyerName;
        this.contact = contact;
    }
    
    // Getters and Setters
    public int getMarketID() { return marketID; }
    public void setMarketID(int marketID) { this.marketID = marketID; }
    
    public int getHarvestID() { return harvestID; }
    public void setHarvestID(int harvestID) { this.harvestID = harvestID; }
    
    public String getMarketName() { return marketName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getQuantitySold() { return quantitySold; }
    public void setQuantitySold(double quantitySold) { this.quantitySold = quantitySold; }
    
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Market{marketID=" + marketID + ", marketName='" + marketName + "', quantitySold=" + quantitySold + "}";
    }
}