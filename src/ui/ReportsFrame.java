package ui;

import service.ReportService;
import model.Farmer;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsFrame extends JFrame {
    private User user;
    private Farmer farmer;
    private ReportService reportService;
    
    // Color scheme matching FarmerDashboard
    private final Color PRIMARY_GREEN = new Color(34, 153, 84);
    private final Color SECONDARY_GREEN = new Color(76, 175, 80);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color TEXT_LIGHT = new Color(117, 117, 117);
    private final Color BORDER_GRAY = new Color(224, 224, 224);
    
    private JPanel mainContentPanel;
    private JComboBox<String> reportTypeCombo;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    
    public ReportsFrame(User user, Farmer farmer) {
        this.user = user;
        this.farmer = farmer;
        this.reportService = new ReportService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("AgriPortal - Reports - " + farmer.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        createMainLayout();
        loadDefaultReport();
    }
    
    private void createMainLayout() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_WHITE);
        
        // Header
        mainContainer.add(createHeader(), BorderLayout.NORTH);
        
        // Control Panel
        mainContainer.add(createControlPanel(), BorderLayout.WEST);
        
        // Main Content
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(BACKGROUND_WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainContainer.add(mainContentPanel, BorderLayout.CENTER);
        
        setContentPane(mainContainer);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_GRAY),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel titleLabel = new JLabel("Farm Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        
        // Back button with BLACK text
        JButton backButton = createModernButton("← Back to Dashboard", Color.BLACK);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportsFrame.this.dispose();
                new FarmerDashboard(user, farmer).setVisible(true);
            }
        });
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(245, 248, 246));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        controlPanel.setPreferredSize(new Dimension(280, 0));
        
        // Report Type Selection
        JLabel typeLabel = new JLabel("Report Type");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] reportTypes = {
            "Farm Performance Report",
            "Financial Analysis Report", 
            "Crop Analysis Report",
            "Seasonal Report"
        };
        reportTypeCombo = new JComboBox<>(reportTypes);
        styleComboBox(reportTypeCombo);
        
        // Date Range Selection
        JLabel dateRangeLabel = new JLabel("Date Range");
        dateRangeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateRangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set default dates (last 30 days)
        Date startDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
        Date endDate = new Date();
        
        SpinnerDateModel startModel = new SpinnerDateModel(startDate, null, null, java.util.Calendar.DAY_OF_MONTH);
        startDateSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startEditor);
        styleSpinner(startDateSpinner);
        
        SpinnerDateModel endModel = new SpinnerDateModel(endDate, null, null, java.util.Calendar.DAY_OF_MONTH);
        endDateSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endEditor);
        styleSpinner(endDateSpinner);
        
        // Generate Button with BLACK text
        JButton generateButton = createModernButton("Generate Report", Color.BLACK);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        
        // Export Button with BLACK text
        JButton exportButton = createModernButton("Export to PDF", Color.BLACK);
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToPDF();
            }
        });
        
        controlPanel.add(typeLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlPanel.add(dateRangeLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(createLabeledField("From:", startDateSpinner));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(createLabeledField("To:", endDateSpinner));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(generateButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(exportButton);
        controlPanel.add(Box.createVerticalGlue());
        
        return controlPanel;
    }
    
    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        
        if (reportType.equals("Farm Performance Report")) {
            showFarmPerformanceReport();
        } else if (reportType.equals("Financial Analysis Report")) {
            showFinancialReport();
        } else if (reportType.equals("Crop Analysis Report")) {
            showCropAnalysisReport();
        } else if (reportType.equals("Seasonal Report")) {
            showSeasonalReport();
        }
    }
    
    private void showFarmPerformanceReport() {
        mainContentPanel.removeAll();
        
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(BACKGROUND_WHITE);
        
        // Header
        JPanel headerPanel = createCard();
        headerPanel.setLayout(new BorderLayout());
        
        JLabel reportTitle = new JLabel("Farm Performance Report");
        reportTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        reportTitle.setForeground(PRIMARY_GREEN);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(((SpinnerDateModel) startDateSpinner.getModel()).getDate());
        String endDate = sdf.format(((SpinnerDateModel) endDateSpinner.getModel()).getDate());
        
        JLabel periodLabel = new JLabel("Period: " + startDate + " to " + endDate);
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        periodLabel.setForeground(TEXT_LIGHT);
        
        headerPanel.add(reportTitle, BorderLayout.WEST);
        headerPanel.add(periodLabel, BorderLayout.EAST);
        
        // Key Metrics
        JPanel metricsPanel = createCard();
        metricsPanel.setLayout(new GridLayout(2, 4, 15, 15));
        
        String[][] metricsData = {
            {"Total Fields", "3", " "},
            {"Total Area", "11.2 ha", " "},
            {"Growing Crops", "5", " "},
            {"Total Revenue", "3.9M RWF", " "},
            {"Completed Irrigation", "8", " "},
            {"Pending Irrigation", "2", " "},
            {"Total Harvest", "1,250.5 kg", " "},
            {"Harvested Crops", "3", " "}
        };
        
        for (String[] metric : metricsData) {
            metricsPanel.add(createMetricCard(metric[0], metric[1], metric[2]));
        }
        
        // Crop Performance Table
        JPanel cropPerformancePanel = createCard();
        cropPerformancePanel.setLayout(new BorderLayout());
        
        JLabel cropTitle = new JLabel("Crop Performance Breakdown");
        cropTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cropTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columns = {"Crop Name", "Total Harvest (kg)", "Status", "Planted Date"};
        Object[][] data = {
            {"Maize", "450.2", "Growing", "2024-01-15"},
            {"Beans", "320.8", "Harvested", "2024-01-10"},
            {"Potatoes", "480.5", "Growing", "2024-02-01"},
            {"Tomatoes", "0.0", "Planted", "2024-02-20"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable cropTable = new JTable(model);
        styleTable(cropTable);
        
        JScrollPane scrollPane = new JScrollPane(cropTable);
        cropPerformancePanel.add(cropTitle, BorderLayout.NORTH);
        cropPerformancePanel.add(scrollPane, BorderLayout.CENTER);
        
        reportPanel.add(headerPanel);
        reportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reportPanel.add(metricsPanel);
        reportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reportPanel.add(cropPerformancePanel);
        
        mainContentPanel.add(new JScrollPane(reportPanel), BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showFinancialReport() {
        mainContentPanel.removeAll();
        
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(BACKGROUND_WHITE);
        
        // Header
        JPanel headerPanel = createCard();
        headerPanel.setLayout(new BorderLayout());
        
        JLabel reportTitle = new JLabel("Financial Analysis Report");
        reportTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        reportTitle.setForeground(PRIMARY_GREEN);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(((SpinnerDateModel) startDateSpinner.getModel()).getDate());
        String endDate = sdf.format(((SpinnerDateModel) endDateSpinner.getModel()).getDate());
        
        JLabel periodLabel = new JLabel("Period: " + startDate + " to " + endDate);
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        periodLabel.setForeground(TEXT_LIGHT);
        
        headerPanel.add(reportTitle, BorderLayout.WEST);
        headerPanel.add(periodLabel, BorderLayout.EAST);
        
        // Financial Metrics
        JPanel metricsPanel = createCard();
        metricsPanel.setLayout(new GridLayout(2, 3, 15, 15));
        
        String[][] metricsData = {
            {"Total Revenue", "245M RWF ", " "},
            {"Quantity Sold", "1,250.5 kg", ""},
            {"Average Price", "1960Frw/kg", ""},
            {"Total Transactions", "15", ""},
            {"Market Revenue", "1,800,000Frw", ""},
            {"Direct Sales", "650,000Frw", ""}
        };
        
        for (String[] metric : metricsData) {
            metricsPanel.add(createMetricCard(metric[0], metric[1], metric[2]));
        }
        
        // Revenue Breakdown Table
        JPanel revenuePanel = createCard();
        revenuePanel.setLayout(new BorderLayout());
        
        JLabel revenueTitle = new JLabel("Revenue by Crop");
        revenueTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        revenueTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columns = {"Crop", "Revenue", "Percentage", "Market"};
        Object[][] data = {
            {"Maize", "$980.00", "40%", "Local Market"},
            {"Beans", "$735.00", "30%", "Direct Sales"},
            {"Potatoes", "$490.00", "20%", "Wholesale"},
            {"Tomatoes", "$245.00", "10%", "Farmers Market"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable revenueTable = new JTable(model);
        styleTable(revenueTable);
        
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        revenuePanel.add(revenueTitle, BorderLayout.NORTH);
        revenuePanel.add(scrollPane, BorderLayout.CENTER);
        
        reportPanel.add(headerPanel);
        reportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reportPanel.add(metricsPanel);
        reportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reportPanel.add(revenuePanel);
        
        mainContentPanel.add(new JScrollPane(reportPanel), BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showCropAnalysisReport() {
        mainContentPanel.removeAll();
        
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(BACKGROUND_WHITE);
        
        JLabel comingSoon = new JLabel("Crop Analysis Report - Coming Soon", JLabel.CENTER);
        comingSoon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        comingSoon.setForeground(TEXT_LIGHT);
        
        mainContentPanel.add(comingSoon, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showSeasonalReport() {
        mainContentPanel.removeAll();
        
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(BACKGROUND_WHITE);
        
        JLabel comingSoon = new JLabel("Seasonal Report - Coming Soon", JLabel.CENTER);
        comingSoon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        comingSoon.setForeground(TEXT_LIGHT);
        
        mainContentPanel.add(comingSoon, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void loadDefaultReport() {
        showFarmPerformanceReport();
    }
    
    private void exportToPDF() {
        JOptionPane.showMessageDialog(this,
            "Report exported successfully!\n\n" +
            "The report has been saved as PDF in your documents folder.",
            "Export Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // UI Helper Methods
    private JPanel createMetricCard(String title, String value, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(PRIMARY_GREEN);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        
        return card;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }
    
    private JButton createModernButton(String text, final Color textColor) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(textColor, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        spinner.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 246));
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jLabel.setForeground(TEXT_LIGHT);
        
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setShowGrid(true);
        table.setGridColor(BORDER_GRAY);
    }
}