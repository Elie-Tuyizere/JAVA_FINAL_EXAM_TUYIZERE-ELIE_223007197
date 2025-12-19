package dao;

import model.Farmer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmerDAO {
    private Connection connection;
    
    public FarmerDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new farmer
    public boolean createFarmer(Farmer farmer) {
        String sql = "INSERT INTO Farmers (UserID, FullName, Location, Contact, Experience) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, farmer.getUserID());
            stmt.setString(2, farmer.getFullName());
            stmt.setString(3, farmer.getLocation());
            stmt.setString(4, farmer.getContact());
            stmt.setInt(5, farmer.getExperience());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    farmer.setFarmerID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating farmer: " + e.getMessage());
        }
        return false;
    }
    
    // Get farmer by user ID
    public Farmer getFarmerByUserID(int userID) {
        String sql = "SELECT * FROM Farmers WHERE UserID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFarmerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting farmer by user ID: " + e.getMessage());
        }
        return null;
    }
    
    // Get farmer by farmer ID
    public Farmer getFarmerByID(int farmerID) {
        String sql = "SELECT * FROM Farmers WHERE FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFarmerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting farmer by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Get all farmers
    public List<Farmer> getAllFarmers() {
        List<Farmer> farmers = new ArrayList<>();
        String sql = "SELECT * FROM Farmers";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                farmers.add(extractFarmerFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting farmers: " + e.getMessage());
        }
        return farmers;
    }
    
    // Update farmer
    public boolean updateFarmer(Farmer farmer) {
        String sql = "UPDATE Farmers SET FullName = ?, Location = ?, Contact = ?, Experience = ? WHERE FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, farmer.getFullName());
            stmt.setString(2, farmer.getLocation());
            stmt.setString(3, farmer.getContact());
            stmt.setInt(4, farmer.getExperience());
            stmt.setInt(5, farmer.getFarmerID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating farmer: " + e.getMessage());
        }
        return false;
    }
    
    // Helper method to extract Farmer from ResultSet
    private Farmer extractFarmerFromResultSet(ResultSet rs) throws SQLException {
        Farmer farmer = new Farmer();
        farmer.setFarmerID(rs.getInt("FarmerID"));
        farmer.setUserID(rs.getInt("UserID"));
        farmer.setFullName(rs.getString("FullName"));
        farmer.setLocation(rs.getString("Location"));
        farmer.setContact(rs.getString("Contact"));
        farmer.setExperience(rs.getInt("Experience"));
        farmer.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return farmer;
    }
}