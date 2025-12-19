package dao;

import model.Market;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarketDAO {
    private Connection connection;
    
    public MarketDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new market record
    public boolean createMarketRecord(Market market) {
        String sql = "INSERT INTO Markets (HarvestID, MarketName, Price, QuantitySold, SaleDate, BuyerName, Contact) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, market.getHarvestID());
            stmt.setString(2, market.getMarketName());
            stmt.setDouble(3, market.getPrice());
            stmt.setDouble(4, market.getQuantitySold());
            stmt.setDate(5, Date.valueOf(market.getSaleDate()));
            stmt.setString(6, market.getBuyerName());
            stmt.setString(7, market.getContact());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    market.setMarketID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating market record: " + e.getMessage());
        }
        return false;
    }
    
    // Get market records by harvest ID
    public List<Market> getMarketRecordsByHarvestID(int harvestID) {
        List<Market> markets = new ArrayList<>();
        String sql = "SELECT * FROM Markets WHERE HarvestID = ? ORDER BY SaleDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, harvestID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                markets.add(extractMarketFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting market records by harvest ID: " + e.getMessage());
        }
        return markets;
    }
    
    // Get market records by farmer ID
    public List<Market> getMarketRecordsByFarmerID(int farmerID) {
        List<Market> markets = new ArrayList<>();
        String sql = "SELECT m.* FROM Markets m JOIN Harvests h ON m.HarvestID = h.HarvestID " +
                    "JOIN Crops c ON h.CropID = c.CropID JOIN Fields f ON c.FieldID = f.FieldID " +
                    "WHERE f.FarmerID = ? ORDER BY m.SaleDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                markets.add(extractMarketFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting market records by farmer ID: " + e.getMessage());
        }
        return markets;
    }
    
    // Get total revenue by farmer
    public double getTotalRevenue(int farmerID) {
        String sql = "SELECT SUM(m.Price * m.QuantitySold) as TotalRevenue FROM Markets m " +
                    "JOIN Harvests h ON m.HarvestID = h.HarvestID " +
                    "JOIN Crops c ON h.CropID = c.CropID " +
                    "JOIN Fields f ON c.FieldID = f.FieldID " +
                    "WHERE f.FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalRevenue");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Helper method to extract Market from ResultSet
    private Market extractMarketFromResultSet(ResultSet rs) throws SQLException {
        Market market = new Market();
        market.setMarketID(rs.getInt("MarketID"));
        market.setHarvestID(rs.getInt("HarvestID"));
        market.setMarketName(rs.getString("MarketName"));
        market.setPrice(rs.getDouble("Price"));
        market.setQuantitySold(rs.getDouble("QuantitySold"));
        market.setSaleDate(rs.getDate("SaleDate").toLocalDate());
        market.setBuyerName(rs.getString("BuyerName"));
        market.setContact(rs.getString("Contact"));
        market.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return market;
    }
}