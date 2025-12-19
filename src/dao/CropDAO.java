package dao;

import model.Crop;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CropDAO {
    private Connection connection;
    
    public CropDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new crop
    public boolean createCrop(Crop crop) {
        String sql = "INSERT INTO Crops (FieldID, Name, Description, Category, PriceOrValue, Status, PlantingDate, ExpectedHarvestDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, crop.getFieldID());
            stmt.setString(2, crop.getName());
            stmt.setString(3, crop.getDescription());
            stmt.setString(4, crop.getCategory());
            stmt.setDouble(5, crop.getPriceOrValue());
            stmt.setString(6, crop.getStatus());
            stmt.setDate(7, Date.valueOf(crop.getPlantingDate()));
            stmt.setDate(8, Date.valueOf(crop.getExpectedHarvestDate()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    crop.setCropID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating crop: " + e.getMessage());
        }
        return false;
    }
    
    // Get crops by field ID
    public List<Crop> getCropsByFieldID(int fieldID) {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT * FROM Crops WHERE FieldID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fieldID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crops.add(extractCropFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting crops by field ID: " + e.getMessage());
        }
        return crops;
    }
    
    // Get crops by farmer ID
    public List<Crop> getCropsByFarmerID(int farmerID) {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT c.* FROM Crops c JOIN Fields f ON c.FieldID = f.FieldID WHERE f.FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crops.add(extractCropFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting crops by farmer ID: " + e.getMessage());
        }
        return crops;
    }
    
    // Get crop by ID
    public Crop getCropByID(int cropID) {
        String sql = "SELECT * FROM Crops WHERE CropID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cropID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCropFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting crop by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Update crop
    public boolean updateCrop(Crop crop) {
        String sql = "UPDATE Crops SET Name = ?, Description = ?, Category = ?, PriceOrValue = ?, Status = ?, PlantingDate = ?, ExpectedHarvestDate = ? WHERE CropID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, crop.getName());
            stmt.setString(2, crop.getDescription());
            stmt.setString(3, crop.getCategory());
            stmt.setDouble(4, crop.getPriceOrValue());
            stmt.setString(5, crop.getStatus());
            stmt.setDate(6, Date.valueOf(crop.getPlantingDate()));
            stmt.setDate(7, Date.valueOf(crop.getExpectedHarvestDate()));
            stmt.setInt(8, crop.getCropID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating crop: " + e.getMessage());
        }
        return false;
    }
    
    // Update crop status
    public boolean updateCropStatus(int cropID, String status) {
        String sql = "UPDATE Crops SET Status = ? WHERE CropID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, cropID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating crop status: " + e.getMessage());
        }
        return false;
    }
    
    // Helper method to extract Crop from ResultSet
    private Crop extractCropFromResultSet(ResultSet rs) throws SQLException {
        Crop crop = new Crop();
        crop.setCropID(rs.getInt("CropID"));
        crop.setFieldID(rs.getInt("FieldID"));
        crop.setName(rs.getString("Name"));
        crop.setDescription(rs.getString("Description"));
        crop.setCategory(rs.getString("Category"));
        crop.setPriceOrValue(rs.getDouble("PriceOrValue"));
        crop.setStatus(rs.getString("Status"));
        crop.setPlantingDate(rs.getDate("PlantingDate").toLocalDate());
        crop.setExpectedHarvestDate(rs.getDate("ExpectedHarvestDate").toLocalDate());
        crop.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return crop;
    }
}