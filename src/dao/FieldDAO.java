package dao;

import model.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FieldDAO {
    private Connection connection;
    
    public FieldDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new field
    public boolean createField(Field field) {
        String sql = "INSERT INTO Fields (FarmerID, Name, Address, Capacity, Manager, Contact, Area) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, field.getFarmerID());
            stmt.setString(2, field.getName());
            stmt.setString(3, field.getAddress());
            stmt.setDouble(4, field.getCapacity());
            stmt.setString(5, field.getManager());
            stmt.setString(6, field.getContact());
            stmt.setDouble(7, field.getArea());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    field.setFieldID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating field: " + e.getMessage());
        }
        return false;
    }
    
    // Get fields by farmer ID
    public List<Field> getFieldsByFarmerID(int farmerID) {
        List<Field> fields = new ArrayList<>();
        String sql = "SELECT * FROM Fields WHERE FarmerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fields.add(extractFieldFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting fields by farmer ID: " + e.getMessage());
        }
        return fields;
    }
    
    // Get field by ID
    public Field getFieldByID(int fieldID) {
        String sql = "SELECT * FROM Fields WHERE FieldID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fieldID);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFieldFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting field by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Get all fields
    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();
        String sql = "SELECT * FROM Fields";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fields.add(extractFieldFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting fields: " + e.getMessage());
        }
        return fields;
    }
    
    // Update field
    public boolean updateField(Field field) {
        String sql = "UPDATE Fields SET Name = ?, Address = ?, Capacity = ?, Manager = ?, Contact = ?, Area = ? WHERE FieldID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, field.getName());
            stmt.setString(2, field.getAddress());
            stmt.setDouble(3, field.getCapacity());
            stmt.setString(4, field.getManager());
            stmt.setString(5, field.getContact());
            stmt.setDouble(6, field.getArea());
            stmt.setInt(7, field.getFieldID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating field: " + e.getMessage());
        }
        return false;
    }
    
    // Delete field
    public boolean deleteField(int fieldID) {
        String sql = "DELETE FROM Fields WHERE FieldID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fieldID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting field: " + e.getMessage());
        }
        return false;
    }
    
    // Helper method to extract Field from ResultSet
    private Field extractFieldFromResultSet(ResultSet rs) throws SQLException {
        Field field = new Field();
        field.setFieldID(rs.getInt("FieldID"));
        field.setFarmerID(rs.getInt("FarmerID"));
        field.setName(rs.getString("Name"));
        field.setAddress(rs.getString("Address"));
        field.setCapacity(rs.getDouble("Capacity"));
        field.setManager(rs.getString("Manager"));
        field.setContact(rs.getString("Contact"));
        field.setArea(rs.getDouble("Area"));
        field.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return field;
    }
}