package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserDashboard extends JFrame {
    private String currentUser;
    
    // Modern Color Scheme
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SECONDARY_BLUE = new Color(52, 152, 219);
    private final Color ACCENT_GREEN = new Color(46, 204, 113);
    private final Color ACCENT_ORANGE = new Color(255, 140, 0);
    private final Color ACCENT_PURPLE = new Color(155, 89, 182);
    private final Color ACCENT_RED = new Color(231, 76, 60);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color TEXT_LIGHT = new Color(117, 117, 117);
    private final Color BORDER_GRAY = new Color(224, 224, 224);
    
    public UserDashboard(String username) {
        this.currentUser = username;
        initializeUI();
    }
    
    public UserDashboard() {
        this("Guest");
    }
    
    private void initializeUI() {
        setTitle("User Dashboard - Welcome " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        createMainLayout();
        setVisible(true);
    }
    
    private void createMainLayout() {
        // Main container with scrollable content
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_WHITE);
        
        // Create header (fixed - no scrolling)
        mainContainer.add(createHeader(), BorderLayout.NORTH);
        
        // Create scrollable content area
        JScrollPane scrollPane = new JScrollPane(createContentArea());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Footer
        mainContainer.add(createFooter(), BorderLayout.SOUTH);
        
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
        
        JLabel logoLabel = new JLabel("UserPortal");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(PRIMARY_BLUE);
        
        JLabel subtitleLabel = new JLabel("User Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(10));
        leftHeader.add(subtitleLabel);
        
        // Right side - User info and controls
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(TEXT_DARK);
        
        // Notification button
        JButton notificationBtn = createIconButton("Notifications", "Notifications", ACCENT_ORANGE);
        notificationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNotifications();
            }
        });
        
        // Profile button
        JButton profileBtn = createIconButton("Profile", "Profile", ACCENT_PURPLE);
        profileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showProfile();
            }
        });
        
        // Logout button
        JButton logoutBtn = createIconButton("Logout", "Logout", ACCENT_RED);
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        rightHeader.add(welcomeLabel);
        rightHeader.add(Box.createHorizontalStrut(15));
        rightHeader.add(notificationBtn);
        rightHeader.add(profileBtn);
        rightHeader.add(logoutBtn);
        
        header.add(leftHeader, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContentArea() {
        JPanel contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setBackground(BACKGROUND_WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        
        // Welcome card
        contentArea.add(createWelcomeCard());
        contentArea.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Quick Stats Row
        contentArea.add(createQuickStatsRow());
        contentArea.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Main Dashboard Grid (2 columns)
        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        mainGrid.setBackground(BACKGROUND_WHITE);
        
        mainGrid.add(createLeftColumn());
        mainGrid.add(createRightColumn());
        
        contentArea.add(mainGrid);
        contentArea.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Additional Components Row
        contentArea.add(createAdditionalComponentsRow());
        contentArea.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Bottom Row
        contentArea.add(createBottomRow());
        
        return contentArea;
    }
    
    private JPanel createWelcomeCard() {
        JPanel card = createGradientCard(PRIMARY_BLUE, SECONDARY_BLUE);
        card.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("<html><div style='font-size: 28px; font-weight: bold; color: white;'>Good Morning, " + currentUser + "!</div>" +
                                        "<div style='font-size: 16px; color: rgba(255,255,255,0.9); margin-top: 8px;'>" +
                                        "Welcome to your personalized dashboard. Here's what's happening today.</div></html>");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton exploreBtn = createModernButton("Explore Features", Color.BLACK, Color.WHITE);
        JButton helpBtn = createModernButton("Get Help", Color.BLACK, Color.WHITE);
        JButton tourBtn = createModernButton("Quick Tour", Color.BLACK, Color.WHITE);
        
        exploreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFeatures();
            }
        });
        helpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        });
        tourBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showQuickTour();
            }
        });
        
        buttonPanel.add(exploreBtn);
        buttonPanel.add(helpBtn);
        buttonPanel.add(tourBtn);
        
        card.add(welcomeLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createQuickStatsRow() {
        JPanel statsRow = new JPanel(new GridLayout(1, 6, 15, 0));
        statsRow.setBackground(BACKGROUND_WHITE);
        
        String[][] statsData = {
            {"12", "Total Projects", "Projects"},
            {"8", "Completed", "Completed"},
            {"3", "In Progress", "In Progress"},
            {"1", "Pending", "Pending"},
            {"24", "Tasks", "Tasks"},
            {"95%", "Productivity", "Productivity"}
        };
        
        Color[] statColors = {
            PRIMARY_BLUE, ACCENT_GREEN, ACCENT_ORANGE, 
            ACCENT_RED, ACCENT_PURPLE, SECONDARY_BLUE
        };
        
        for (int i = 0; i < statsData.length; i++) {
            String[] stat = statsData[i];
            Color color = statColors[i];
            statsRow.add(createAnimatedStatCard(stat[0], stat[1], stat[2], color));
        }
        
        return statsRow;
    }
    
    private JPanel createLeftColumn() {
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setBackground(BACKGROUND_WHITE);
        
        leftColumn.add(createProjectsSection());
        leftColumn.add(Box.createRigidArea(new Dimension(0, 20)));
        leftColumn.add(createRecentActivitySection());
        
        return leftColumn;
    }
    
    private JPanel createRightColumn() {
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setBackground(BACKGROUND_WHITE);
        
        rightColumn.add(createQuickActionsSection());
        rightColumn.add(Box.createRigidArea(new Dimension(0, 20)));
        rightColumn.add(createSystemStatusSection());
        
        return rightColumn;
    }
    
    private JPanel createProjectsSection() {
        JPanel card = createCardWithHeader("My Projects", "Manage your ongoing projects", PRIMARY_BLUE);
        card.setLayout(new BorderLayout());
        
        // Project statistics
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBackground(Color.WHITE);
        
        String[][] projectStats = {
            {"Active", "5", "Active"},
            {"On Hold", "2", "On Hold"},
            {"Completed", "8", "Completed"},
            {"Overdue", "1", "Overdue"}
        };
        
        for (String[] stat : projectStats) {
            statsPanel.add(createMiniStatCard(stat[0], stat[1], stat[2]));
        }
        
        // Projects table
        String[] columns = {"Project Name", "Status", "Due Date", "Progress"};
        Object[][] data = {
            {"Website Redesign", "In Progress", "2024-12-15", "75%"},
            {"Mobile App", "Active", "2024-11-30", "90%"},
            {"Database Migration", "Completed", "2024-10-20", "100%"},
            {"API Development", "On Hold", "2025-01-10", "40%"},
            {"UI/UX Design", "Active", "2024-12-05", "60%"}
        };
        
        JTable projectsTable = createStyledTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(projectsTable);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(tablePanel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRecentActivitySection() {
        JPanel card = createCardWithHeader("Recent Activity", "Latest updates and notifications", ACCENT_ORANGE);
        
        String[] activities = {
            "Project 'Website Redesign' milestone completed",
            "New comment on 'Mobile App' project",
            "Team meeting scheduled for tomorrow 10:00 AM",
            "Task 'Database Optimization' marked as completed",
            "Performance report generated for Q3 2024",
            "Code review requested for feature branch",
            "System maintenance scheduled for weekend",
            "Productivity increased by 15% this week"
        };
        
        JList<String> activityList = new JList<>(activities);
        activityList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityList.setBackground(Color.WHITE);
        activityList.setCellRenderer(new ActivityListRenderer());
        
        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createQuickActionsSection() {
        JPanel card = createCardWithHeader("Quick Actions", "Frequently used features", ACCENT_PURPLE);
        
        JPanel actionsGrid = new JPanel(new GridLayout(3, 2, 12, 12));
        actionsGrid.setBackground(Color.WHITE);
        
        String[][] actions = {
            {"New Project", "Create new project"},
            {"Generate Report", "Create reports"},
            {"Team Chat", "Message team"},
            {"Settings", "Preferences"},
            {"Calendar", "View schedule"},
            {"Search", "Find items"}
        };
        
        Color[] actionColors = {
            PRIMARY_BLUE, ACCENT_GREEN, ACCENT_ORANGE, 
            ACCENT_PURPLE, SECONDARY_BLUE, ACCENT_RED
        };
        
        for (int i = 0; i < actions.length; i++) {
            String[] action = actions[i];
            Color color = actionColors[i];
            actionsGrid.add(createActionButton(action[0], action[1], color));
        }
        
        card.add(actionsGrid, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createSystemStatusSection() {
        JPanel card = createCardWithHeader("System Status", "Platform health and performance", ACCENT_GREEN);
        card.setLayout(new BorderLayout());
        
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(Color.WHITE);
        
        // System metrics
        String[][] metrics = {
            {"Server Uptime", "99.9%", "Online"},
            {"Response Time", "125ms", "Good"},
            {"Active Users", "1,247", "Users"},
            {"Storage Used", "65%", "Storage"},
            {"API Health", "100%", "Healthy"},
            {"Database", "Optimal", "Status"}
        };
        
        for (String[] metric : metrics) {
            statusPanel.add(createSystemMetric(metric[0], metric[1], metric[2]));
            statusPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.setBackground(Color.WHITE);
        performancePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel perfTitle = new JLabel("Performance Trend");
        perfTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar perfBar = new JProgressBar(0, 100);
        perfBar.setValue(85);
        perfBar.setString("85% - Excellent");
        perfBar.setStringPainted(true);
        perfBar.setForeground(ACCENT_GREEN);
        perfBar.setBackground(new Color(240, 240, 240));
        
        performancePanel.add(perfTitle, BorderLayout.NORTH);
        performancePanel.add(perfBar, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(statusPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(performancePanel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createAdditionalComponentsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setBackground(BACKGROUND_WHITE);
        
        row.add(createTeamSection());
        row.add(createResourcesSection());
        row.add(createAnalyticsSection());
        
        return row;
    }
    
    private JPanel createTeamSection() {
        JPanel card = createCardWithHeader("Team Members", "Your collaborators and team", SECONDARY_BLUE);
        
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.Y_AXIS));
        teamPanel.setBackground(Color.WHITE);
        
        String[][] teamMembers = {
            {"HABUMUGISHA Eric", "Project Lead", "Active now", "Joined 2023"},
            {"NTAMAKA Willium", "Developer", "2h ago", "Joined 2024"},
            {"Benard RWEMA", "Designer", "1d ago", "Joined 2023"},
            {"Emily Davis", "QA Engineer", "30m ago", "Joined 2024"}
        };
        
        for (String[] member : teamMembers) {
            teamPanel.add(createTeamMember(member[0], member[1], member[2], member[3]));
            teamPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        card.add(new JScrollPane(teamPanel), BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createResourcesSection() {
        JPanel card = createCardWithHeader("Resources", "Helpful documents and links", ACCENT_ORANGE);
        
        JTextArea resourcesText = new JTextArea();
        resourcesText.setText("Quick Links:\n\n" +
                            "• Documentation Center\n" +
                            "• API Reference Guide\n" +
                            "• Video Tutorials\n" +
                            "• Community Forum\n" +
                            "• Support Tickets\n\n" +
                            "Recent Updates:\n" +
                            "• New feature: Advanced Analytics\n" +
                            "• Updated: User Guide v2.1\n" +
                            "• Fixed: Mobile responsiveness");
        resourcesText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resourcesText.setLineWrap(true);
        resourcesText.setWrapStyleWord(true);
        resourcesText.setEditable(false);
        resourcesText.setBackground(Color.WHITE);
        resourcesText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        card.add(new JScrollPane(resourcesText), BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createAnalyticsSection() {
        JPanel card = createCardWithHeader("Analytics", "Performance insights", ACCENT_PURPLE);
        
        JPanel analyticsPanel = new JPanel();
        analyticsPanel.setLayout(new BoxLayout(analyticsPanel, BoxLayout.Y_AXIS));
        analyticsPanel.setBackground(Color.WHITE);
        
        String[][] analytics = {
            {"Weekly Activity", "+12%", "Trend"},
            {"Task Completion", "94%", "Rate"},
            {"Team Collaboration", "88%", "Score"},
            {"Project Velocity", "High", "Speed"}
        };
        
        for (String[] analytic : analytics) {
            analyticsPanel.add(createAnalyticItem(analytic[0], analytic[1], analytic[2]));
            analyticsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }
        
        card.add(analyticsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setBackground(BACKGROUND_WHITE);
        
        row.add(createUpcomingEventsSection());
        row.add(createTipsSection());
        
        return row;
    }
    
    private JPanel createUpcomingEventsSection() {
        JPanel card = createCardWithHeader("Upcoming Events", "Meetings and deadlines", ACCENT_GREEN);
        
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(Color.WHITE);
        
        String[][] events = {
            {"Team Standup", "Today 10:00 AM", "Conference Room A"},
            {"Project Review", "Tomorrow 2:00 PM", "Zoom Meeting"},
            {"Client Meeting", "Dec 15, 3:00 PM", "Client Office"},
            {"Sprint Planning", "Dec 18, 11:00 AM", "Main Hall"}
        };
        
        for (String[] event : events) {
            eventsPanel.add(createEventItem(event[0], event[1], event[2]));
            eventsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        card.add(new JScrollPane(eventsPanel), BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createTipsSection() {
        JPanel card = createCardWithHeader("Pro Tips", "Productivity recommendations", PRIMARY_BLUE);
        
        String[] tips = {
            "Break large tasks into smaller, manageable chunks",
            "Use time blocking for better focus",
            "Document your progress daily",
            "Review and adjust priorities weekly",
            "Collaborate early and often with team members",
            "Use analytics to track your performance trends"
        };
        
        JList<String> tipsList = new JList<>(tips);
        tipsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tipsList.setBackground(Color.WHITE);
        tipsList.setCellRenderer(new TipsListRenderer());
        
        card.add(new JScrollPane(tipsList), BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(44, 62, 80));
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel footerLabel = new JLabel("© 2025 UserPortal • Professional Dashboard System");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel versionLabel = new JLabel("v2.1.0 • All rights reserved");
        versionLabel.setForeground(new Color(200, 200, 200));
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        footer.add(footerLabel, BorderLayout.WEST);
        footer.add(versionLabel, BorderLayout.EAST);
        
        return footer;
    }
    
    // ==================== HELPER METHODS ====================
    
    private JPanel createGradientCard(final Color startColor, final Color endColor) {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    private JPanel createCardWithHeader(String title, String subtitle, Color accentColor) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 3, 0, accentColor),
            BorderFactory.createEmptyBorder(15, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_DARK);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_LIGHT);
        
        header.add(titleLabel);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(subtitleLabel);
        
        card.add(header, BorderLayout.NORTH);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createAnimatedStatCard(String value, String label, String icon, Color color) {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iconLabel.setForeground(color);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
    
    private JButton createIconButton(String text, String tooltip, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JButton createModernButton(String text, Color textColor, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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
    
    // ==================== ACTION HANDLERS ====================
    
    private void showNotifications() {
        JOptionPane.showMessageDialog(this, "Notifications\n\nYou have 3 new notifications waiting!");
    }
    
    private void showProfile() {
        JOptionPane.showMessageDialog(this, "User Profile\n\nProfile management coming soon!");
    }
    
    private void showFeatures() {
        JOptionPane.showMessageDialog(this, "Explore Features\n\nDiscover all the amazing features available!");
    }
    
    private void showHelp() {
        JOptionPane.showMessageDialog(this, "Help Center\n\nGet assistance and support resources!");
    }
    
    private void showQuickTour() {
        JOptionPane.showMessageDialog(this, "Quick Tour\n\nTake a tour of your dashboard features!");
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
    
    // ==================== CUSTOM RENDERERS ====================
    
    class ActivityListRenderer extends JLabel implements ListCellRenderer<String> {
        public ActivityListRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        
        public Component getListCellRendererComponent(JList list, String value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            setBackground(isSelected ? new Color(225, 245, 254) : Color.WHITE);
            setForeground(TEXT_DARK);
            return this;
        }
    }
    
    class TipsListRenderer extends JLabel implements ListCellRenderer<String> {
        public TipsListRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        }
        
        public Component getListCellRendererComponent(JList list, String value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            setBackground(isSelected ? new Color(232, 245, 233) : Color.WHITE);
            setForeground(TEXT_DARK);
            return this;
        }
    }
    
    // ==================== COMPONENT CREATORS ====================
    
    private JPanel createActionButton(final String title, String description, Color color) {
        final JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(TEXT_LIGHT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);
        
        buttonPanel.add(textPanel, BorderLayout.CENTER);
        
        // Add click action
        buttonPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JOptionPane.showMessageDialog(UserDashboard.this, 
                    "Action: " + title + "\n\nThis feature is available in the full version!");
            }
            
            public void mouseEntered(MouseEvent evt) {
                buttonPanel.setBackground(new Color(245, 248, 246));
            }
            
            public void mouseExited(MouseEvent evt) {
                buttonPanel.setBackground(Color.WHITE);
            }
        });
        
        return buttonPanel;
    }
    
    private JPanel createTeamMember(String name, String role, String status, String joinDate) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(PRIMARY_BLUE);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(roleLabel);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusLabel.setForeground(TEXT_LIGHT);
        
        JLabel joinLabel = new JLabel(joinDate);
        joinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        joinLabel.setForeground(TEXT_LIGHT);
        
        panel.add(infoPanel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(joinLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createSystemMetric(String metric, String value, String status) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel metricLabel = new JLabel(metric);
        metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(PRIMARY_BLUE);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_LIGHT);
        
        panel.add(metricLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createAnalyticItem(String title, String value, String icon) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(ACCENT_GREEN);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        iconLabel.setForeground(TEXT_LIGHT);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createEventItem(String event, String time, String location) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel eventLabel = new JLabel(event);
        eventLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(TEXT_LIGHT);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(eventLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(timeLabel);
        
        JLabel locationLabel = new JLabel(location);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        locationLabel.setForeground(TEXT_LIGHT);
        
        panel.add(infoPanel, BorderLayout.WEST);
        panel.add(locationLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JTable createStyledTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(BORDER_GRAY);
        return table;
    }
    
    private JPanel createMiniStatCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        iconLabel.setForeground(PRIMARY_BLUE);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(PRIMARY_BLUE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(TEXT_LIGHT);
        
        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(titleLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard("TUYIZERE Elie");
            }
        });
    }
}