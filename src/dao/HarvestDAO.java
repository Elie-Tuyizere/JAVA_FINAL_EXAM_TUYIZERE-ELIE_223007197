package dao;

import model.Harvest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HarvestDAO {
    private Connection connection;
    
    public HarvestDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new harvest
    public boolean createHarvest(Harvest harvest) {
        String sql = "INSERT INTO Harvests (CropID, HarvestDate, Quantity, Quality, Price, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, harvest.getCropID());
            stmt.setDate(2, Date.valueOf(harvest.getHarvestDate()));
            stmt.setDouble(3, harvest.getQuantity());
            stmt.setString(4, harvest.getQuality());
            stmt.setDouble(5, harvest.getPrice());
            stmt.setString(6, harvest.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    harvest.setHarvestID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating harvest: " + e.getMessage());
        }
        return false;
    }
    
    // Get harvests by crop ID
    public List<Harvest> getHarvestsByCropID(int cropID) {
        List<Harvest> harvests = new ArrayList<>();
        String sql = "SELECT * FROM Harvests WHERE CropID = ? ORDER BY HarvestDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cropID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                harvests.add(extractHarvestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting harvests by crop ID: " + e.getMessage());
        }
        return harvests;
    }
    
    // Get harvests by farmer ID
    public List<Harvest> getHarvestsByFarmerID(int farmerID) {
        List<Harvest> harvests = new ArrayList<>();
        String sql = "SELECT h.* FROM Harvests h JOIN Crops c ON h.CropID = c.CropID JOIN Fields f ON c.FieldID = f.FieldID WHERE f.FarmerID = ? ORDER BY h.HarvestDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                harvests.add(extractHarvestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting harvests by farmer ID: " + e.getMessage());
        }
        return harvests;
    }
    
    // Get total harvest quantity by farmer
    public double getTotalHarvestQuantity(int farmerID) {
        String sql = "SELECT SUM(h.Quantity) as TotalQuantity FROM Harvests h JOIN Crops c ON h.CropID = c.CropID JOIN Fields f ON c.FieldID = f.FieldID WHERE f.FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalQuantity");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total harvest quantity: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Helper method to extract Harvest from ResultSet
    private Harvest extractHarvestFromResultSet(ResultSet rs) throws SQLException {
        Harvest harvest = new Harvest();
        harvest.setHarvestID(rs.getInt("HarvestID"));
        harvest.setCropID(rs.getInt("CropID"));
        harvest.setHarvestDate(rs.getDate("HarvestDate").toLocalDate());
        harvest.setQuantity(rs.getDouble("Quantity"));
        harvest.setQuality(rs.getString("Quality"));
        harvest.setPrice(rs.getDouble("Price"));
        harvest.setNotes(rs.getString("Notes"));
        harvest.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return harvest;
    }
}