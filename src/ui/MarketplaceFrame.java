package ui;

import model.Market;
import model.User;
import model.Farmer;
import dao.MarketDAO;
import service.DashboardService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MarketplaceFrame extends JFrame {
    private User user;
    private Farmer farmer;
    private MarketDAO marketDAO;
    private DashboardService dashboardService;
    private JTable marketplaceTable;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_GREEN = new Color(34, 153, 84);
    private final Color ACCENT_ORANGE = new Color(255, 140, 0);
    private final Color ACCENT_BLUE = new Color(41, 128, 185);
    private final Color ACCENT_PURPLE = new Color(142, 68, 173);
    private final Color LIGHT_GREEN = new Color(232, 245, 233);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color TEXT_LIGHT = new Color(117, 117, 117);
    private final Color BORDER_GRAY = new Color(224, 224, 224);

    private final double EXCHANGE_RATE = 1200.0;

    public MarketplaceFrame(User user, Farmer farmer) {
        this.user = user;
        this.farmer = farmer;
        this.marketDAO = new MarketDAO();
        this.dashboardService = new DashboardService();
        
        initializeUI();
        loadMarketplaceData();
    }

    private void initializeUI() {
        setTitle("AgriPortal - Marketplace: " + farmer.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1700, 1000);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with gradient background
        JPanel headerPanel = createGradientHeader();
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("🌾 Agricultural Marketplace");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createModernButton(" Back to Dashboard", Color.BLACK, ACCENT_BLUE);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        // Stats Cards Row
        JPanel statsPanel = createStatsPanel();
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton listProductButton = createModernButton(" List New Product", Color.BLACK, ACCENT_ORANGE);
        JButton viewMarketButton = createModernButton("View Market Prices", Color.BLACK, ACCENT_BLUE);
        JButton analyzeButton = createModernButton("Market Analysis", Color.BLACK, ACCENT_PURPLE);
        JButton myListingsButton = createModernButton(" My Listings", Color.BLACK, PRIMARY_GREEN);

        listProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showListProductDialog();
            }
        });
        
        viewMarketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMarketPrices();
            }
        });
        
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMarketAnalysis();
            }
        });

        myListingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMyListings();
            }
        });

        controlPanel.add(listProductButton);
        controlPanel.add(viewMarketButton);
        controlPanel.add(analyzeButton);
        controlPanel.add(myListingsButton);

        // Marketplace Table with better styling
        String[] columns = {"Product", "Seller", "Quality", "Quantity Available", 
                           "Price/kg (RWF)", "Location", "Rating", "Status", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        marketplaceTable = new JTable(tableModel);
        marketplaceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        marketplaceTable.setRowHeight(40);
        marketplaceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Table header with BLACK text
        marketplaceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        marketplaceTable.getTableHeader().setBackground(PRIMARY_GREEN);
        marketplaceTable.getTableHeader().setForeground(Color.BLACK);
        marketplaceTable.setShowGrid(true);
        marketplaceTable.setGridColor(new Color(240, 240, 240));
        
        // Add row renderer for better visual feedback
        marketplaceTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 250, 252));
                    }
                }
                
                // Color code based on status column
                if (column == 7 && value != null) {
                    String status = value.toString();
                    if ("Available".equals(status)) {
                        c.setBackground(new Color(232, 245, 233)); // Light green
                    } else if ("Hot Deal".equals(status)) {
                        c.setBackground(new Color(255, 243, 224)); // Light orange
                    } else if ("Limited".equals(status)) {
                        c.setBackground(new Color(255, 235, 238)); // Light red
                    }
                }
                
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(marketplaceTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_GREEN, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Featured Products Panel
        JPanel featuredPanel = createFeaturedProductsPanel();

        // Main content container
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(controlPanel, BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(featuredPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createGradientHeader() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(34, 153, 84), 
                    getWidth(), 0, new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(BACKGROUND_WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        statsPanel.add(createStatCard("", "156", "Active Listings", ACCENT_ORANGE));
        statsPanel.add(createStatCard("", "8.7M RWF", "Daily Volume", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("", "92%", "Market Activity", ACCENT_BLUE));
        statsPanel.add(createStatCard("", "4.6/5", "Avg. Rating", ACCENT_PURPLE));

        return statsPanel;
    }

    private JPanel createStatCard(String icon, String value, String label, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(TEXT_LIGHT);
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(labelLabel);

        return card;
    }

    private JPanel createFeaturedProductsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_GREEN, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(400, 0));

        JLabel titleLabel = new JLabel(" Featured Products");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Featured agriculture products with RWF prices
        String[][] featuredProducts = {
            {"", "Organic Maize", "Premium Quality", convertToRwf(450) + "/kg", "HOT"},
            {"", "Irish Potatoes", "Fresh Harvest", convertToRwf(350) + "/kg", "NEW"},
            {"", "Rwandan Tomatoes", "Grade A", convertToRwf(600) + "/kg", "TRENDING"},
            {"", "Sukuma Wiki", "Fresh Greens", convertToRwf(200) + "/bunch", "POPULAR"},
            {"", "Hot Peppers", "Local Variety", convertToRwf(800) + "/kg", "SPICY"},
            {"", "Matoke Bananas", "Ripe & Sweet", convertToRwf(300) + "/bunch", "SWEET"}
        };

        for (String[] product : featuredProducts) {
            panel.add(createFeaturedProductCard(product[0], product[1], product[2], product[3], product[4]));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panel;
    }

    private JPanel createFeaturedProductCard(String icon, String name, String desc, String price, String tag) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_DARK);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(TEXT_LIGHT);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLabel.setForeground(ACCENT_ORANGE);

        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(descLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(priceLabel);

        // Tag
        JLabel tagLabel = new JLabel(tag);
        tagLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tagLabel.setForeground(Color.WHITE);
        tagLabel.setOpaque(true);
        tagLabel.setBackground(getTagColor(tag));
        tagLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(tagLabel, BorderLayout.EAST);

        return card;
    }

    private Color getTagColor(String tag) {
        switch (tag) {
            case "HOT": return new Color(231, 76, 60);
            case "NEW": return new Color(52, 152, 219);
            case "TRENDING": return new Color(155, 89, 182);
            case "POPULAR": return new Color(46, 204, 113);
            case "SPICY": return new Color(255, 87, 34);
            case "SWEET": return new Color(156, 39, 176);
            case "PREMIUM": return new Color(241, 196, 15);
            default: return PRIMARY_GREEN;
        }
    }

    private JButton createModernButton(String text, Color textColor, final Color backgroundColor) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect - maintain black text on hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
                button.setForeground(Color.BLACK);
            }
            
            public void mouseExited(MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }

    // Helper method to convert to RWF (already in RWF, just formatting)
    private String convertToRwf(double price) {
        return String.format("%,d RWF", (int) price);
    }

    private void loadMarketplaceData() {
        tableModel.setRowCount(0);
        
        
        Object[][] agricultureData = {
            {"Maize Grain", "Green Valley Farms", "Grade A", "2,500 kg", "450 RWF", "Musanze", "4.8", "Available", "+250 78 123 4567"},
            {"Beans (Red)", "Mountain Harvest", "Premium", "1,200 kg", "800 RWF", "Rubavu", "4.6", "Hot Deal", "+250 79 234 5678"},
            {"Irish Potatoes", "Highland Growers", "Fresh", "5,000 kg", "350 RWF", "Burera", "4.3", "Available", "+250 72 345 6789"},
            {"Tomatoes", "Sunrise Gardens", "Grade A", "800 kg", "600 RWF", "Kigali", "4.7", "Limited", "+250 73 456 7890"},
            {"Sukuma Wiki", "Urban Greens", "Organic", "300 bunches", "200 RWF", "Kicukiro", "4.5", "Available", "+250 78 567 8901"},
            {"Carrots", "Fresh Roots Co.", "Export Quality", "600 kg", "550 RWF", "Nyabihu", "4.9", "Hot Deal", "+250 76 678 9012"},
            {"Onions", "Spice Fields", "Local Variety", "1,000 kg", "400 RWF", "Ruhango", "4.2", "Available", "+250 75 789 0123"},
            {"Cabbage", "Green Leaf Farms", "Fresh", "700 kg", "300 RWF", "Gicumbi", "4.4", "Available", "+250 72 890 1234"},
            {"Avocados", "Tropical Fruits", "Hass Variety", "400 kg", "1,200 RWF", "Rusizi", "4.8", "Premium", "+250 79 901 2345"},
            {"Pineapples", "Sweet Harvest", "Ripe & Sweet", "250 kg", "800 RWF", "Kamonyi", "4.6", "Available", "+250 78 012 3456"},
            {"Coffee Beans", "Rwanda Highlands", "Specialty Grade", "150 kg", "3,500 RWF", "Nyamasheke", "5.0", "Premium", "+250 72 123 8901"},
            {"Tea Leaves", "Mountain Tea Co.", "Premium Quality", "300 kg", "2,800 RWF", "Nyamagabe", "4.7", "Available", "+250 79 234 9012"}
        };

        for (Object[] row : agricultureData) {
            tableModel.addRow(row);
        }
    }

    private void showListProductDialog() {
        JOptionPane.showMessageDialog(this, 
            " List New Agricultural Product\n\n" +
            "This feature allows you to:\n" +
            "• Add your farm products to the marketplace\n" +
            "• Set competitive prices in RWF\n" +
            "• Specify product quality and quantity\n" +
            "• Upload product images\n" +
            "• Manage your farm listings\n\n" +
            "Feature coming in the next update!",
            "List Farm Product", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMarketPrices() {
        JOptionPane.showMessageDialog(this, 
            " Agriculture Market Price Trends (RWF)\n\n" +
            "Current market prices for key crops:\n" +
            "• Maize: 400 - 500 RWF/kg \n" +
            "• Beans: 700 - 900 RWF/kg \n" +
            "• Potatoes: 300 - 400 RWF/kg \n" +
            "• Tomatoes: 550 - 700 RWF/kg \n" +
            "• Sukuma Wiki: 150 - 250 RWF/bunch \n" +
            "• Carrots: 500 - 600 RWF/kg \n" +
            "• Onions: 350 - 450 RWF/kg \n\n" +
            "Live market data from Rwanda Agriculture Board",
            "Agriculture Market Prices", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMarketAnalysis() {
        JOptionPane.showMessageDialog(this, 
            " Agriculture Market Analysis\n\n" +
            "Advanced analytics for farmers:\n" +
            "• Seasonal price trends in RWF\n" +
            "• Regional demand analysis\n" +
            "• Competitor pricing across regions\n" +
            "• Weather impact on prices\n" +
            "• Export opportunity analysis\n" +
            "• Crop rotation recommendations\n\n" +
            "Powered by Rwanda Agriculture Board data",
            "Market Analysis", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMyListings() {
        JOptionPane.showMessageDialog(this, 
            " My Farm Listings\n\n" +
            "Manage your agricultural marketplace presence:\n" +
            "• View your active farm product listings\n" +
            "• Update prices based on market trends\n" +
            "• Track sales performance and revenue\n" +
            "• Respond to buyer inquiries\n" +
            "• Monitor product ratings and reviews\n" +
            "• Analyze seasonal sales patterns\n\n" +
            "Farmer dashboard coming soon!",
            "My Farm Listings", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}