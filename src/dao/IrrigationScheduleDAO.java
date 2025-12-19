package dao;

import model.IrrigationSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IrrigationScheduleDAO {
    private Connection connection;
    
    public IrrigationScheduleDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // Create new irrigation schedule
    public boolean createIrrigationSchedule(IrrigationSchedule schedule) {
        String sql = "INSERT INTO IrrigationSchedules (FieldID, ScheduleDate, WaterAmount, Duration, Status, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getFieldID());
            stmt.setDate(2, Date.valueOf(schedule.getScheduleDate()));
            stmt.setDouble(3, schedule.getWaterAmount());
            stmt.setInt(4, schedule.getDuration());
            stmt.setString(5, schedule.getStatus());
            stmt.setString(6, schedule.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    schedule.setIrrigationScheduleID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating irrigation schedule: " + e.getMessage());
        }
        return false;
    }
    
    // Get irrigation schedules by field ID
    public List<IrrigationSchedule> getSchedulesByFieldID(int fieldID) {
        List<IrrigationSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM IrrigationSchedules WHERE FieldID = ? ORDER BY ScheduleDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fieldID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting irrigation schedules by field ID: " + e.getMessage());
        }
        return schedules;
    }
    
    // Get irrigation schedules by farmer ID
    public List<IrrigationSchedule> getSchedulesByFarmerID(int farmerID) {
        List<IrrigationSchedule> schedules = new ArrayList<>();
        String sql = "SELECT is.* FROM IrrigationSchedules is JOIN Fields f ON is.FieldID = f.FieldID WHERE f.FarmerID = ? ORDER BY is.ScheduleDate DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, farmerID);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting irrigation schedules by farmer ID: " + e.getMessage());
        }
        return schedules;
    }
    
    // Update irrigation schedule status
    public boolean updateScheduleStatus(int scheduleID, String status) {
        String sql = "UPDATE IrrigationSchedules SET Status = ? WHERE IrrigationScheduleID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, scheduleID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating irrigation schedule status: " + e.getMessage());
        }
        return false;
    }
    
    // Get pending irrigation schedules
    public List<IrrigationSchedule> getPendingSchedules() {
        List<IrrigationSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM IrrigationSchedules WHERE Status = 'PENDING' ORDER BY ScheduleDate";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending irrigation schedules: " + e.getMessage());
        }
        return schedules;
    }
    
    // Helper method to extract IrrigationSchedule from ResultSet
    private IrrigationSchedule extractScheduleFromResultSet(ResultSet rs) throws SQLException {
        IrrigationSchedule schedule = new IrrigationSchedule();
        schedule.setIrrigationScheduleID(rs.getInt("IrrigationScheduleID"));
        schedule.setFieldID(rs.getInt("FieldID"));
        schedule.setScheduleDate(rs.getDate("ScheduleDate").toLocalDate());
        schedule.setWaterAmount(rs.getDouble("WaterAmount"));
        schedule.setDuration(rs.getInt("Duration"));
        schedule.setStatus(rs.getString("Status"));
        schedule.setNotes(rs.getString("Notes"));
        schedule.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        return schedule;
    }
}