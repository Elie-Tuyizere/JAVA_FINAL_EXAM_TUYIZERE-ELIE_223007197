// ReportService.java
package service;

import dao.*;
import model.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportService {
    private FarmerDAO farmerDAO;
    private FieldDAO fieldDAO;
    private CropDAO cropDAO;
    private HarvestDAO harvestDAO;
    private MarketDAO marketDAO;
    private IrrigationScheduleDAO irrigationDAO;
    
    public ReportService() {
        this.farmerDAO = new FarmerDAO();
        this.fieldDAO = new FieldDAO();
        this.cropDAO = new CropDAO();
        this.harvestDAO = new HarvestDAO();
        this.marketDAO = new MarketDAO();
        this.irrigationDAO = new IrrigationScheduleDAO();
    }
    
    // Farm Performance Report
    public Map<String, Object> generateFarmPerformanceReport(int farmerID, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<String, Object>();
        Farmer farmer = farmerDAO.getFarmerByID(farmerID);
        List<Field> fields = fieldDAO.getFieldsByFarmerID(farmerID);
        List<Crop> crops = cropDAO.getCropsByFarmerID(farmerID);
        List<Harvest> harvests = harvestDAO.getHarvestsByFarmerID(farmerID);
        List<Market> marketRecords = marketDAO.getMarketRecordsByFarmerID(farmerID);
        
        // Basic farmer info
        report.put("farmerName", farmer.getFullName());
        report.put("location", farmer.getLocation());
        report.put("experience", farmer.getExperience() + " years");
        report.put("reportPeriod", startDate + " to " + endDate);
        report.put("generatedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        // Field statistics
        report.put("totalFields", fields.size());
        double totalArea = 0.0;
        for (Field field : fields) {
            totalArea += field.getArea();
        }
        report.put("totalArea", String.format("%.2f ha", totalArea));
        
        // Crop statistics
        report.put("totalCrops", crops.size());
        int growingCrops = 0;
        int harvestedCrops = 0;
        for (Crop crop : crops) {
            if ("GROWING".equals(crop.getStatus())) {
                growingCrops++;
            }
            if ("HARVESTED".equals(crop.getStatus())) {
                harvestedCrops++;
            }
        }
        report.put("growingCrops", growingCrops);
        report.put("harvestedCrops", harvestedCrops);
        
        // Harvest statistics
        double totalHarvestQuantity = 0.0;
        for (Harvest harvest : harvests) {
            if (!harvest.getHarvestDate().isBefore(startDate) && !harvest.getHarvestDate().isAfter(endDate)) {
                totalHarvestQuantity += harvest.getQuantity();
            }
        }
        report.put("totalHarvestQuantity", String.format("%.2f kg", totalHarvestQuantity));
        
        // Financial statistics
        double totalRevenue = 0.0;
        for (Market market : marketRecords) {
            if (!market.getSaleDate().isBefore(startDate) && !market.getSaleDate().isAfter(endDate)) {
                totalRevenue += market.getPrice() * market.getQuantitySold();
            }
        }
        report.put("totalRevenue", String.format("$%.2f", totalRevenue));
        
        // Irrigation statistics
        List<IrrigationSchedule> irrigations = irrigationDAO.getSchedulesByFarmerID(farmerID);
        int completedIrrigations = 0;
        int pendingIrrigations = 0;
        for (IrrigationSchedule irrigation : irrigations) {
            if ("COMPLETED".equals(irrigation.getStatus())) {
                completedIrrigations++;
            }
            if ("PENDING".equals(irrigation.getStatus())) {
                pendingIrrigations++;
            }
        }
        report.put("completedIrrigations", completedIrrigations);
        report.put("pendingIrrigations", pendingIrrigations);
        
        // Crop performance breakdown
        Map<String, Double> cropPerformance = new HashMap<String, Double>();
        for (Crop crop : crops) {
            double cropHarvest = 0.0;
            for (Harvest harvest : harvests) {
                if (harvest.getCropID() == crop.getCropID()) {
                    cropHarvest += harvest.getQuantity();
                }
            }
            cropPerformance.put(crop.getName(), cropHarvest);
        }
        report.put("cropPerformance", cropPerformance);
        
        // Field utilization
        Map<String, String> fieldUtilization = new HashMap<String, String>();
        for (Field field : fields) {
            int fieldCrops = 0;
            for (Crop crop : crops) {
                if (crop.getFieldID() == field.getFieldID()) {
                    fieldCrops++;
                }
            }
            double utilization = (fieldCrops / field.getCapacity()) * 100;
            fieldUtilization.put(field.getName(), String.format("%.1f%%", utilization));
        }
        report.put("fieldUtilization", fieldUtilization);
        
        return report;
    }
    
    // Financial Report
    public Map<String, Object> generateFinancialReport(int farmerID, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<String, Object>();
        List<Market> marketRecords = marketDAO.getMarketRecordsByFarmerID(farmerID);
        List<Harvest> harvests = harvestDAO.getHarvestsByFarmerID(farmerID);
        
        // Filter records by date
        List<Market> filteredMarkets = new ArrayList<Market>();
        for (Market market : marketRecords) {
            if (!market.getSaleDate().isBefore(startDate) && !market.getSaleDate().isAfter(endDate)) {
                filteredMarkets.add(market);
            }
        }
        
        List<Harvest> filteredHarvests = new ArrayList<Harvest>();
        for (Harvest harvest : harvests) {
            if (!harvest.getHarvestDate().isBefore(startDate) && !harvest.getHarvestDate().isAfter(endDate)) {
                filteredHarvests.add(harvest);
            }
        }
        
        // Revenue calculations
        double totalRevenue = 0.0;
        double totalQuantitySold = 0.0;
        for (Market market : filteredMarkets) {
            totalRevenue += market.getPrice() * market.getQuantitySold();
            totalQuantitySold += market.getQuantitySold();
        }
        
        double averagePrice = totalQuantitySold > 0 ? totalRevenue / totalQuantitySold : 0;
        
        // Market breakdown
        Map<String, Double> revenueByMarket = new HashMap<String, Double>();
        for (Market market : filteredMarkets) {
            double marketRevenue = market.getPrice() * market.getQuantitySold();
            String marketName = market.getMarketName();
            if (revenueByMarket.containsKey(marketName)) {
                revenueByMarket.put(marketName, revenueByMarket.get(marketName) + marketRevenue);
            } else {
                revenueByMarket.put(marketName, marketRevenue);
            }
        }
        
        // Crop revenue breakdown
        Map<String, Double> revenueByCrop = new HashMap<String, Double>();
        for (Market market : filteredMarkets) {
            Harvest harvest = null;
            for (Harvest h : harvests) {
                if (h.getHarvestID() == market.getHarvestID()) {
                    harvest = h;
                    break;
                }
            }
            if (harvest != null) {
                Crop crop = cropDAO.getCropByID(harvest.getCropID());
                if (crop != null) {
                    double cropRevenue = market.getPrice() * market.getQuantitySold();
                    String cropName = crop.getName();
                    if (revenueByCrop.containsKey(cropName)) {
                        revenueByCrop.put(cropName, revenueByCrop.get(cropName) + cropRevenue);
                    } else {
                        revenueByCrop.put(cropName, cropRevenue);
                    }
                }
            }
        }
        
        report.put("totalRevenue", totalRevenue);
        report.put("totalQuantitySold", totalQuantitySold);
        report.put("averagePrice", averagePrice);
        report.put("totalTransactions", filteredMarkets.size());
        report.put("revenueByMarket", revenueByMarket);
        report.put("revenueByCrop", revenueByCrop);
        report.put("reportPeriod", startDate + " to " + endDate);
        
        return report;
    }
    
    // Crop Analysis Report
    public Map<String, Object> generateCropAnalysisReport(int farmerID) {
        Map<String, Object> report = new HashMap<String, Object>();
        List<Crop> crops = cropDAO.getCropsByFarmerID(farmerID);
        List<Harvest> harvests = harvestDAO.getHarvestsByFarmerID(farmerID);
        
        Map<String, List<Object>> cropAnalysis = new HashMap<String, List<Object>>();
        
        for (Crop crop : crops) {
            List<Object> analysis = new ArrayList<Object>();
            
            // Basic crop info
            analysis.add(crop.getStatus());
            analysis.add(crop.getPlantingDate());
            analysis.add(crop.getExpectedHarvestDate());
            
            // Harvest data
            List<Harvest> cropHarvests = new ArrayList<Harvest>();
            for (Harvest harvest : harvests) {
                if (harvest.getCropID() == crop.getCropID()) {
                    cropHarvests.add(harvest);
                }
            }
            
            double totalHarvest = 0.0;
            double totalQualityScore = 0.0;
            for (Harvest harvest : cropHarvests) {
                totalHarvest += harvest.getQuantity();
                totalQualityScore += getQualityScore(harvest.getQuality());
            }
            double avgQualityScore = cropHarvests.size() > 0 ? totalQualityScore / cropHarvests.size() : 0;
            
            analysis.add(totalHarvest);
            analysis.add(avgQualityScore);
            analysis.add(cropHarvests.size());
            
            cropAnalysis.put(crop.getName(), analysis);
        }
        
        report.put("cropAnalysis", cropAnalysis);
        report.put("totalCropsAnalyzed", crops.size());
        
        return report;
    }
    
    private double getQualityScore(String quality) {
        if (quality == null) return 3.0;
        
        String qualityUpper = quality.toUpperCase();
        if ("EXCELLENT".equals(qualityUpper)) {
            return 5.0;
        } else if ("GOOD".equals(qualityUpper)) {
            return 4.0;
        } else if ("AVERAGE".equals(qualityUpper)) {
            return 3.0;
        } else if ("POOR".equals(qualityUpper)) {
            return 2.0;
        } else if ("VERY_POOR".equals(qualityUpper)) {
            return 1.0;
        } else {
            return 3.0;
        }
    }
    
    // Seasonal Report
    public Map<String, Object> generateSeasonalReport(int farmerID, String season) {
        Map<String, Object> report = new HashMap<String, Object>();
        // Implementation for seasonal analysis
        report.put("season", season);
        report.put("message", "Seasonal report for " + season + " is under development");
        return report;
    }
}