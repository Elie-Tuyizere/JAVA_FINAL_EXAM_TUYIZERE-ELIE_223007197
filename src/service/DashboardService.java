package service;

import dao.*;
import model.*;
import java.util.List;

public class DashboardService {
    private FarmerDAO farmerDAO;
    private FieldDAO fieldDAO;
    private CropDAO cropDAO;
    private IrrigationScheduleDAO irrigationDAO;
    private HarvestDAO harvestDAO;
    private MarketDAO marketDAO;
    
    public DashboardService() {
        this.farmerDAO = new FarmerDAO();
        this.fieldDAO = new FieldDAO();
        this.cropDAO = new CropDAO();
        this.irrigationDAO = new IrrigationScheduleDAO();
        this.harvestDAO = new HarvestDAO();
        this.marketDAO = new MarketDAO();
    }
    
    // Farmer statistics
    public Farmer getFarmer(int farmerID) {
        return farmerDAO.getFarmerByID(farmerID);
    }
    
    public List<Field> getFarmerFields(int farmerID) {
        return fieldDAO.getFieldsByFarmerID(farmerID);
    }
    
    public List<Crop> getFarmerCrops(int farmerID) {
        return cropDAO.getCropsByFarmerID(farmerID);
    }
    
    public List<IrrigationSchedule> getFarmerIrrigationSchedules(int farmerID) {
        return irrigationDAO.getSchedulesByFarmerID(farmerID);
    }
    
    public List<Harvest> getFarmerHarvests(int farmerID) {
        return harvestDAO.getHarvestsByFarmerID(farmerID);
    }
    
    public List<Market> getFarmerMarketRecords(int farmerID) {
        return marketDAO.getMarketRecordsByFarmerID(farmerID);
    }
    
    // Statistics methods
    public int getTotalFieldsCount(int farmerID) {
        return getFarmerFields(farmerID).size();
    }
    
    public int getTotalCropsCount(int farmerID) {
        return getFarmerCrops(farmerID).size();
    }
    
    public int getGrowingCropsCount(int farmerID) {
        List<Crop> crops = getFarmerCrops(farmerID);
        int count = 0;
        for (Crop crop : crops) {
            if ("GROWING".equals(crop.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    public double getTotalArea(int farmerID) {
        List<Field> fields = getFarmerFields(farmerID);
        double totalArea = 0.0;
        for (Field field : fields) {
            totalArea += field.getArea();
        }
        return totalArea;
    }
    
    public double getTotalRevenue(int farmerID) {
        return marketDAO.getTotalRevenue(farmerID);
    }
    
    public int getPendingIrrigationCount(int farmerID) {
        List<IrrigationSchedule> schedules = getFarmerIrrigationSchedules(farmerID);
        int count = 0;
        for (IrrigationSchedule schedule : schedules) {
            if ("PENDING".equals(schedule.getStatus())) {
                count++;
            }
        }
        return count;
    }
}