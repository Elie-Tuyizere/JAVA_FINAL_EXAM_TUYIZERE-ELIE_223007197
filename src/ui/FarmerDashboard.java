package ui;

import model.User;
import model.Farmer;
import javax.swing.*;
import javax.swing.border.*;
import dao.FarmerDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class FarmerDashboard extends JFrame {
    private User user;
    private Farmer farmer;
    
    // Color scheme
    private final Color PRIMARY_GREEN = new Color(34, 153, 84);
    private final Color SECONDARY_GREEN = new Color(76, 175, 80);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color TEXT_LIGHT = new Color(117, 117, 117);
    private final Color BORDER_GRAY = new Color(224, 224, 224);
    
    public FarmerDashboard(User user, Farmer farmer) {
        this.user = user;
        this.farmer = farmer;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("AgriPortal - Farmer: " + farmer.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        
        // Set application icon
        setIconImage(createAppIcon());
        
        createMainLayout();
    }
    
    private Image createAppIcon() {
        // Create a simple green icon programmatically
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setColor(PRIMARY_GREEN);
        g2d.fillRoundRect(4, 4, 24, 24, 8, 8);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("A", 12, 20);
        g2d.dispose();
        return icon;
    }
    
    private void createMainLayout() {
        // Main container with modern layout
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_WHITE);
        
        // Create header
        mainContainer.add(createHeader(), BorderLayout.NORTH);
        
        // Create main content area
        mainContainer.add(createContentArea(), BorderLayout.CENTER);
        
        setContentPane(mainContainer);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        // Left side - Logo and title
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setBackground(Color.WHITE);
        
        // Logo placeholder
        JLabel logoLabel = new JLabel("🌱");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel titleLabel = new JLabel("AgriPortal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        
        JLabel subtitleLabel = new JLabel("Farmer Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        
        leftHeader.add(logoLabel);
        leftHeader.add(titleLabel);
        leftHeader.add(Box.createHorizontalStrut(10));
        leftHeader.add(subtitleLabel);
        
        // Right side - User info and controls
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + farmer.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(TEXT_DARK);
        
        // Profile button with BLACK text
        JButton profileBtn = createModernButton(" Profile", Color.BLACK);
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get updated data from user input
                String newLocation = JOptionPane.showInputDialog(FarmerDashboard.this, 
                    "Enter new location:", farmer.getLocation());
                
                if (newLocation != null && !newLocation.trim().isEmpty()) {
                    // Update model
                    farmer.setLocation(newLocation.trim());
                    
                    // Save to database through DAO
                    FarmerDAO farmerDAO = new FarmerDAO();
                    boolean success = farmerDAO.updateFarmer(farmer);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(FarmerDashboard.this,
                            "Profile updated successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        
        // Logout button with RED text
        JButton logoutBtn = createModernButton("Logout", new Color(220, 53, 69));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 53, 69), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Add logout functionality
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(FarmerDashboard.this, 
                    "Are you sure you want to logout?", "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    FarmerDashboard.this.dispose();
                    new LoginFrame().setVisible(true);
                }
            }
        });
        
        rightHeader.add(welcomeLabel);
        rightHeader.add(Box.createHorizontalStrut(15));
        rightHeader.add(profileBtn);
        rightHeader.add(Box.createHorizontalStrut(10));
        rightHeader.add(logoutBtn);
        
        header.add(leftHeader, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BACKGROUND_WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create sidebar navigation
        contentArea.add(createSidebar(), BorderLayout.WEST);
        
        // Create main dashboard content
        contentArea.add(createDashboardContent(), BorderLayout.CENTER);
        
        return contentArea;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 248, 246));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        sidebar.setPreferredSize(new Dimension(220, 0));
        
        // Navigation title
        JLabel navTitle = new JLabel("NAVIGATION");
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        navTitle.setForeground(TEXT_LIGHT);
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        navTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        sidebar.add(navTitle);
        
        // Navigation items with icons
        String[][] menuItems = {
            {"", "Dashboard", "primary"},
            {"", "My Crops", "normal"},
            {"", "My Products", "normal"},
            {"", "Orders", "normal"},
            {"", "Marketplace", "normal"},
            {"", "Weather & Advice", "normal"},
            {"", "Reports", "normal"},
            {"", "Settings", "normal"}
        };
        
        for (String[] item : menuItems) {
            JButton menuButton = createMenuButton(item[0], item[1], item[2].equals("primary"));
            menuButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private JButton createMenuButton(String icon, final String text, final boolean isActive) {
        final JButton button = new JButton("<html><div style='text-align: left;'>" + icon + " " + text + "</div></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isActive) {
            button.setBackground(PRIMARY_GREEN);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        } else {
            button.setForeground(TEXT_DARK);
            button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        }
        
        // Add action listener for menu buttons
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMenuClick(text);
            }
        });
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(240, 240, 240));
                    button.setOpaque(true);
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(null);
                    button.setOpaque(false);
                }
            }
        });
        
        return button;
    }
    
    // Method to handle menu clicks
    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
            case "My Crops":
                new MyCropsFrame(user, farmer).setVisible(true);
                break;
            case "My Products":
                new MyProductsFrame().setVisible(true);
                break;
            case "Orders":
                new OrdersFrame().setVisible(true);
                break;
            case "Marketplace":
                new MarketplaceFrame(user, farmer).setVisible(true);
                break;
            case "Weather & Advice":
                JOptionPane.showMessageDialog(this, "Weather & Advice feature coming soon!");
                break;
            case "Reports":
                new ReportsFrame(user, farmer).setVisible(true);
                break;
            case "Settings":
                new SettingsFrame(user, farmer).setVisible(true);
                break;
            default:
                // For Dashboard or other items, do nothing or show message
                if (!menuItem.equals("Dashboard")) {
                    JOptionPane.showMessageDialog(this, menuItem + " feature is under development!");
                }
                break;
        }
    }
    
    // ... REST OF THE FarmerDashboard CLASS METHODS REMAIN THE SAME ...
    // Keep all the existing methods like createDashboardContent(), createWelcomeCard(), 
    // createStatsRow(), createFarmsSection(), createWeatherSection(), etc.
    // These methods should remain unchanged from your original code
    
    private JPanel createDashboardContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND_WHITE);
        
        // Welcome card
        content.add(createWelcomeCard());
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Stats row
        content.add(createStatsRow());
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Main content row (2 columns)
        JPanel mainRow = new JPanel(new GridLayout(1, 2, 20, 0));
        mainRow.setBackground(BACKGROUND_WHITE);
        
        mainRow.add(createFarmsSection());
        mainRow.add(createWeatherSection());
        
        content.add(mainRow);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Bottom row
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomRow.setBackground(BACKGROUND_WHITE);
        
        bottomRow.add(createRecommendedCropsSection());
        bottomRow.add(createQuickActionsSection());
        
        content.add(bottomRow);
        
        return content;
    }
    
    // ... ALL OTHER EXISTING METHODS REMAIN THE SAME ...
    private JPanel createWelcomeCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("<html><div style='font-size: 18px; color: #2196F3;'>Good morning! </div>" +
                                        "<div style='font-size: 24px; font-weight: bold; margin-top: 5px;'><marquee>Welcome back, </marquee>" + farmer.getFullName() + "!</div>" +
                                        "<div style='font-size: 14px; color: #666; margin-top: 8px;'>Here's what's happening with your farms today.</div></html>");
        
        // Reports button with BLACK text
        JButton detailedReportsBtn = createModernButton("View Detailed Reports", Color.BLACK);
        detailedReportsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReportsFrame(user, farmer).setVisible(true);
            }
        });
        
        card.add(welcomeLabel, BorderLayout.CENTER);
        card.add(detailedReportsBtn, BorderLayout.EAST);
        
        return card;
    }

    private JPanel createStatsRow() {
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        statsRow.setBackground(BACKGROUND_WHITE);
        
        String[][] statsData = {
            {"2", "Active Farms", " ", String.valueOf(PRIMARY_GREEN.getRGB())},
            {"5", "Growing Crops", " ", String.valueOf(new Color(76, 175, 80).getRGB())},
            {"12", "Pending Tasks", " ", String.valueOf(new Color(255, 152, 0).getRGB())},
            {"3.9M Rwf", "Monthly Revenue", " ", String.valueOf(new Color(156, 39, 176).getRGB())}
        };
        
        for (String[] stat : statsData) {
            Color color = new Color(Integer.parseInt(stat[3]));
            statsRow.add(createStatCard(stat[0], stat[1], stat[2], color));
        }
        
        return statsRow;
    }

    private JPanel createStatCard(String value, String label, String icon, Color color) {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }

    private JPanel createFarmsSection() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("My Farms");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttons.setBackground(Color.WHITE);
        
        String[] btnLabels = {"Add Farm", "Update", "Delete", "Refresh"};
        for (String label : btnLabels) {
            JButton btn = createSmallButton(label);
            buttons.add(btn);
        }
        
        header.add(title, BorderLayout.WEST);
        header.add(buttons, BorderLayout.EAST);
        
        // Table
        String[] columns = {"ID", "Name", "Location", "Area (ha)", "Status"};
        Object[][] data = {
            {"1", "GreenField", "Kigali", "2.5", "Active"},
            {"2", "Green Acres", "Kigali", "3.5", "Active"},
            {"3", "Sunny Slope", "Northern", "5.2", "Maintenance"}
        };
        
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        
        card.add(header, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createWeatherSection() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Weather Forecast");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columns = {"Date", "Condition", "Temp (°C)", "Rain (mm)"};
        Object[][] data = {
            {"2025-11-09", "Heavy Rain", "16.0", "2.0"},
            {"2025-11-10", "Partly Cloudy", "18.5", "0.0"},
            {"2025-11-11", "Sunny", "22.0", "0.0"},
            {"2025-11-12", "Light Clouds", "20.5", "0.5"}
        };
        
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        
        // Weather summary
        JPanel summary = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summary.setBackground(new Color(225, 245, 254));
        summary.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel summaryLabel = new JLabel("<html><b>Weather Advice:</b> Good conditions for planting. Monitor soil moisture.</html>");
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        summary.add(summaryLabel);
        
        card.add(title, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(summary, BorderLayout.SOUTH);
        
        return card;
    }

    private JPanel createRecommendedCropsSection() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Recommended Crops");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JTextArea cropsText = new JTextArea();
        cropsText.setText("Based on your soil conditions and current season, we recommend:\n\n" +
                         "• Maize (Zea mays) - Suitable for your region\n" +
                         "• Beans (Phaseolus) - Good intercropping option\n" +
                         "• Sweet Potatoes - High market demand\n" +
                         "• Tomatoes - For greenhouse cultivation\n\n" +
                         "Plant during the rainy season for best results.");
        cropsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cropsText.setLineWrap(true);
        cropsText.setWrapStyleWord(true);
        cropsText.setEditable(false);
        cropsText.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(cropsText);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        
        card.add(title, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createQuickActionsSection() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        actionsGrid.setBackground(Color.WHITE);
        
        String[][] actions = {
            {" ", "Add New Crop", "Plant new crops in your fields"},
            {" ", "Schedule Irrigation", "Set up watering schedules"},
            {" ", "Generate Report", "Create farm performance reports"},
            {" ", "Marketplace", "Sell your products online"}
        };
        
        for (String[] action : actions) {
            actionsGrid.add(createActionButton(action[0], action[1], action[2]));
        }
        
        card.add(title, BorderLayout.NORTH);
        card.add(actionsGrid, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createActionButton(String icon, final String title, String description) {
        final JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        buttonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(new Color(245, 248, 246));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(Color.WHITE);
            }
        });
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(TEXT_LIGHT);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(buttonPanel.getBackground());
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);
        
        buttonPanel.add(iconLabel, BorderLayout.WEST);
        buttonPanel.add(textPanel, BorderLayout.CENTER);
        
        // Add click action
        buttonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (title.equals("Add New Crop")) {
                    new MyCropsFrame(user, farmer).setVisible(true);
                } else if (title.equals("Schedule Irrigation")) {
                    JOptionPane.showMessageDialog(FarmerDashboard.this, "Irrigation scheduling coming soon!");
                } else if (title.equals("Generate Report")) {
                    new ReportsFrame(user, farmer).setVisible(true);
                } else if (title.equals("Marketplace")) {
                    new MarketplaceFrame(user, farmer).setVisible(true);
                }
            }
        });
        
        return buttonPanel;
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

    private JButton createSmallButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
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
}