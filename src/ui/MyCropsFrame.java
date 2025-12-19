package ui;

import model.Crop;
import model.Field;
import model.User;
import model.Farmer;
import dao.CropDAO;
import dao.FieldDAO;
import service.DashboardService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class MyCropsFrame extends JFrame {
    private User user;
    private Farmer farmer;
    private CropDAO cropDAO;
    private FieldDAO fieldDAO;
    private DashboardService dashboardService;
    private JTable cropsTable;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_GREEN = new Color(34, 153, 84);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);

    public MyCropsFrame(User user, Farmer farmer) {
        this.user = user;
        this.farmer = farmer;
        this.cropDAO = new CropDAO();
        this.fieldDAO = new FieldDAO();
        this.dashboardService = new DashboardService();
        
        initializeUI();
        loadCropsData();
    }

    private void initializeUI() {
        setTitle("AgriPortal - My Crops: " + farmer.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("My Crops Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);

        JButton backButton = createButton("Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton addButton = createButton("Add New Crop");
        JButton editButton = createButton("Edit Crop");
        JButton deleteButton = createButton("Delete Crop");
        JButton refreshButton = createButton("Refresh");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddCropDialog();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedCrop();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCrop();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCropsData();
            }
        });

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(refreshButton);

        // Table
        String[] columns = {"Crop ID", "Name", "Field", "Category", "Status", 
                           "Planting Date", "Expected Harvest", "Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cropsTable = new JTable(tableModel);
        cropsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cropsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        cropsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cropsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(cropsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JButton createButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(Color.WHITE); // White background
        button.setForeground(Color.BLACK); // Black text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect - light gray on hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }

    private void loadCropsData() {
        try {
            tableModel.setRowCount(0);
            List<Crop> crops = dashboardService.getFarmerCrops(farmer.getFarmerID());
            
            if (crops.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No crops found for this farmer.");
                return;
            }
            
            for (Crop crop : crops) {
                Field field = fieldDAO.getFieldByID(crop.getFieldID());
                String fieldName = field != null ? field.getName() : "Unknown";
                
                tableModel.addRow(new Object[]{
                    crop.getCropID(),
                    crop.getName(),
                    fieldName,
                    crop.getCategory(),
                    crop.getStatus(),
                    crop.getPlantingDate().toString(),
                    crop.getExpectedHarvestDate() != null ? crop.getExpectedHarvestDate().toString() : "N/A",
                    String.format("$%.2f", crop.getPriceOrValue())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading crops: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showAddCropDialog() {
        // Create the dialog
        final JDialog dialog = new JDialog(this, "Add New Crop", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Get farmer's fields
        final List<Field> fields = dashboardService.getFarmerFields(farmer.getFarmerID());
        
        if (fields.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fields found. Please add a field first.");
            return;
        }
        
        final String[] fieldNames = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fieldNames[i] = fields.get(i).getName();
        }

        // Create form components
        final JComboBox<String> fieldCombo = new JComboBox<>(fieldNames);
        final JTextField nameField = new JTextField();
        final JTextField descField = new JTextField();
        final JTextField categoryField = new JTextField();
        final JTextField valueField = new JTextField("0.0");
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PLANTED", "GROWING", "HARVESTED", "DORMANT"});
        final JTextField plantingDateField = new JTextField(LocalDate.now().toString());
        final JTextField harvestDateField = new JTextField(LocalDate.now().plusMonths(3).toString());

        // Add components to form
        formPanel.add(new JLabel("Field:"));
        formPanel.add(fieldCombo);
        formPanel.add(new JLabel("Crop Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Estimated Value:"));
        formPanel.add(valueField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Planting Date:"));
        formPanel.add(plantingDateField);
        formPanel.add(new JLabel("Expected Harvest:"));
        formPanel.add(harvestDateField);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = createButton("Save Crop");
        JButton cancelButton = createButton("Cancel");

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCropData(dialog, fields, fieldCombo, nameField, descField, categoryField, 
                           valueField, statusCombo, plantingDateField, harvestDateField);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }

    private void saveCropData(JDialog dialog, List<Field> fields, JComboBox<String> fieldCombo,
                            JTextField nameField, JTextField descField, JTextField categoryField,
                            JTextField valueField, JComboBox<String> statusCombo,
                            JTextField plantingDateField, JTextField harvestDateField) {
        try {
            // Validate inputs
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter crop name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int selectedFieldIndex = fieldCombo.getSelectedIndex();
            if (selectedFieldIndex < 0) {
                JOptionPane.showMessageDialog(dialog, "Please select a field.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Field selectedField = fields.get(selectedFieldIndex);
            
            // Parse dates
            LocalDate plantingDate;
            LocalDate harvestDate;
            
            try {
                plantingDate = LocalDate.parse(plantingDateField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid planting date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                harvestDate = LocalDate.parse(harvestDateField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid harvest date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse value
            double cropValue;
            try {
                cropValue = Double.parseDouble(valueField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for crop value.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create crop object
            Crop newCrop = new Crop(
                selectedField.getFieldID(),
                nameField.getText().trim(),
                descField.getText().trim(),
                categoryField.getText().trim(),
                cropValue,
                (String) statusCombo.getSelectedItem(),
                plantingDate,
                harvestDate
            );

            // Save to database
            if (cropDAO.createCrop(newCrop)) {
                JOptionPane.showMessageDialog(dialog, "Crop added successfully!");
                loadCropsData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add crop to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void editSelectedCrop() {
        int selectedRow = cropsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to edit.");
            return;
        }

        int cropID = (int) tableModel.getValueAt(selectedRow, 0);
        Crop crop = cropDAO.getCropByID(cropID);
        
        if (crop != null) {
            showEditCropDialog(crop);
        } else {
            JOptionPane.showMessageDialog(this, "Could not find the selected crop.");
        }
    }

    private void showEditCropDialog(Crop crop) {
        JOptionPane.showMessageDialog(this, 
            "Edit Crop: " + crop.getName() + 
            "\n\nEdit functionality will be implemented in the next version." +
            "\nFor now, you can delete and recreate the crop if needed.");
    }

    private void deleteSelectedCrop() {
        int selectedRow = cropsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to delete.");
            return;
        }

        int cropID = (int) tableModel.getValueAt(selectedRow, 0);
        String cropName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete crop: " + cropName + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (cropDAO.updateCropStatus(cropID, "DELETED")) {
                JOptionPane.showMessageDialog(this, "Crop marked as deleted successfully!");
                loadCropsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete crop.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}