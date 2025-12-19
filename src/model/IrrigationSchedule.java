package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IrrigationSchedule {
    private int irrigationScheduleID;
    private int fieldID;
    private LocalDate scheduleDate;
    private double waterAmount;
    private int duration;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    
    public IrrigationSchedule() {}
    
    public IrrigationSchedule(int fieldID, LocalDate scheduleDate, double waterAmount, 
                             int duration, String status, String notes) {
        this.fieldID = fieldID;
        this.scheduleDate = scheduleDate;
        this.waterAmount = waterAmount;
        this.duration = duration;
        this.status = status;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getIrrigationScheduleID() { return irrigationScheduleID; }
    public void setIrrigationScheduleID(int irrigationScheduleID) { this.irrigationScheduleID = irrigationScheduleID; }
    
    public int getFieldID() { return fieldID; }
    public void setFieldID(int fieldID) { this.fieldID = fieldID; }
    
    public LocalDate getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(LocalDate scheduleDate) { this.scheduleDate = scheduleDate; }
    
    public double getWaterAmount() { return waterAmount; }
    public void setWaterAmount(double waterAmount) { this.waterAmount = waterAmount; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "IrrigationSchedule{id=" + irrigationScheduleID + ", date=" + scheduleDate + ", status='" + status + "'}";
    }
}