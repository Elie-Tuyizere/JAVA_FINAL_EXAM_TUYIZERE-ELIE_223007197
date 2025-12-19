package ui;

import service.AuthService;
import model.User;
import model.Farmer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton registerButton;
    private AuthService authService;
    private final Color PRIMARY_GREEN = new Color(46, 125, 50);      
    private final Color LIGHT_GREEN = new Color(129, 199, 132);      
    private final Color ACCENT_GREEN = new Color(56, 142, 60);       
    private final Color BACKGROUND_COLOR = new Color(232, 245, 233); 
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);        
    private final Color TEXT_SECONDARY = new Color(97, 97, 97);      
    private final Color BORDER_COLOR = new Color(200, 230, 201);     
    
    public LoginFrame() {
        authService = new AuthService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("AgriPortal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 1100);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setupMainPanel();
        initializeEventListeners();
    }
    
    private void setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 40, 0));
        JPanel iconContainer = new JPanel();
        iconContainer.setLayout(new BorderLayout());
        iconContainer.setBackground(BACKGROUND_COLOR);
        iconContainer.setMaximumSize(new Dimension(100, 100));
        iconContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel("🌿", JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setForeground(PRIMARY_GREEN);
        
        iconContainer.add(iconLabel, BorderLayout.CENTER);
        
        JLabel titleLabel = new JLabel("Welcome to AgriPortal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Grow with confidence");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(LIGHT_GREEN);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(iconContainer);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        Border lineBorder = new LineBorder(BORDER_COLOR, 1);
        Border emptyBorder = new EmptyBorder(40, 35, 40, 35);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
        formPanel.setBorder(compoundBorder);
        
        JLabel usernameLabel = createInputLabel("Username");
        usernameField = createTextField();
        
        JLabel passwordLabel = createInputLabel("Password");
        passwordField = createPasswordField();
        
        // User Type Selection - Removed Administrator option
        JLabel userTypeLabel = createInputLabel("Login As");
        final String[] userTypes = {"Farmer", "General User"}; // Removed "Administrator"
        userTypeCombo = new JComboBox<>(userTypes);
        styleComboBox(userTypeCombo);
        
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotPanel.setBackground(CARD_COLOR);
        forgotPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JButton forgotPassword = new JButton("Forgot Password?");
        forgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPassword.setForeground(LIGHT_GREEN);
        forgotPassword.setBackground(CARD_COLOR);
        forgotPassword.setBorderPainted(false);
        forgotPassword.setFocusPainted(false);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        forgotPanel.add(forgotPassword);
        
        // SIGN IN button - Green font on white background
        loginButton = createStyledButton("SIGN IN", Color.WHITE, PRIMARY_GREEN);
        
        JPanel dividerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dividerPanel.setBackground(CARD_COLOR);
        dividerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JSeparator leftSep = new JSeparator(SwingConstants.HORIZONTAL);
        leftSep.setForeground(BORDER_COLOR);
        leftSep.setPreferredSize(new Dimension(80, 1));
        
        JLabel orLabel = new JLabel("or");
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orLabel.setForeground(TEXT_SECONDARY);
        
        JSeparator rightSep = new JSeparator(SwingConstants.HORIZONTAL);
        rightSep.setForeground(BORDER_COLOR);
        rightSep.setPreferredSize(new Dimension(80, 1));
        
        dividerPanel.add(leftSep);
        dividerPanel.add(orLabel);
        dividerPanel.add(rightSep);
        
        // CREATE ACCOUNT button - Green font on light green background
        registerButton = createStyledButton("Create New Account", LIGHT_GREEN, PRIMARY_GREEN);
        
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(userTypeLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(userTypeCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(forgotPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(dividerPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(registerButton);
        
        return formPanel;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(new Color(248, 252, 248));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR, 1);
        Border emptyBorder = new EmptyBorder(12, 15, 12, 15);
        comboBox.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
    }
    
    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR, 1);
        Border emptyBorder = new EmptyBorder(12, 15, 12, 15);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
        field.setBackground(new Color(248, 252, 248));
        
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR, 2);
        Border emptyBorder = new EmptyBorder(12, 15, 12, 15);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
        field.setBackground(new Color(248, 252, 248));
        
        return field;
    }
    
    private JButton createStyledButton(String text, final Color backgroundColor, final Color textColor) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(backgroundColor);
        button.setForeground(textColor); // Green text color
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        
        if (text.equals("SIGN IN")) {
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 2), // Green border
                new EmptyBorder(16, 30, 16, 30)
            ));
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        } else {
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 2), // Green border
                new EmptyBorder(14, 25, 14, 25)
            ));
        }
        
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 248, 246)); // Light background on hover
                button.setForeground(PRIMARY_GREEN); // Maintain green text on hover
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setForeground(textColor);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(225, 245, 254)); // Slightly darker on click
                button.setForeground(PRIMARY_GREEN);
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        JLabel copyrightLabel = new JLabel(" 2025 AgriPortal • TUYIZERE elie");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyrightLabel.setForeground(LIGHT_GREEN);
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel versionLabel = new JLabel("Green Growth Edition");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(129, 199, 132));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        footerPanel.add(copyrightLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        footerPanel.add(versionLabel);
        
        return footerPanel;
    }
    
    private void initializeEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });
        
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    private void login() {
        final String username = usernameField.getText().trim();
        final String password = new String(passwordField.getPassword());
        final String selectedUserType = (String) userTypeCombo.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }
        
        if (selectedUserType == null) {
            showError("Please select a user type.");
            return;
        }
        
        performLogin(username, password, selectedUserType);
    }
    
    private void performLogin(final String username, final String password, final String selectedUserType) {
        loginButton.setText("SIGNING IN...");
        loginButton.setEnabled(false);
        
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() {
                try {
                    Thread.sleep(500);
                    User user = authService.login(username, password);
                    
                    // Validate user type selection matches actual user type
                    if (user != null) {
                        String actualUserType = user.getUserType();
                        String expectedUserType = convertToUserType(selectedUserType);
                        
                        if (!actualUserType.equals(expectedUserType)) {
                            // User type mismatch
                            return null;
                        }
                    }
                    return user;
                } catch (Exception e) {
                    System.err.println("Login error: " + e.getMessage());
                    return null;
                }
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        handleSuccessfulLogin(user);
                    } else {
                        handleFailedLogin();
                    }
                } catch (Exception e) {
                    System.err.println("Error in login process: " + e.getMessage());
                    handleFailedLogin();
                }
            }
        };
        worker.execute();
    }
    
    private String convertToUserType(String selectedType) {
        switch (selectedType) {
            case "Farmer":
                return "FARMER";
            case "General User":
                return "USER";
            default:
                return "USER";
        }
    }
    
    private void handleSuccessfulLogin(User user) {
        loginButton.setText("SIGN IN");
        loginButton.setEnabled(true);
        
        JOptionPane.showMessageDialog(this, 
            "Welcome back, " + user.getUsername() + "!\n" +
            "Logged in as: " + user.getUserType(), 
            "Login Successful", 
            JOptionPane.INFORMATION_MESSAGE);
        
        navigateToDashboard(user);
        this.dispose();
    }
    
    private void handleFailedLogin() {
        loginButton.setText("SIGN IN");
        loginButton.setEnabled(true);
        showError("Invalid credentials or user type mismatch. Please check your username, password, and selected user type.");
        passwordField.setText("");
        passwordField.requestFocus();
    }
    
    private void navigateToDashboard(User user) {
        try {
            if ("FARMER".equals(user.getUserType())) {
                Farmer farmer = authService.getFarmerProfile(user.getUserID());
                if (farmer != null) {
                    new FarmerDashboard(user, farmer).setVisible(true);
                } else {
                    showError("Farmer profile not found.");
                    new UserDashboard(user.getUsername()).setVisible(true);
                }
            } else {
                new UserDashboard(user.getUsername()).setVisible(true);
            }
        } catch (Exception e) {
            System.err.println("Error navigating to dashboard: " + e.getMessage());
            e.printStackTrace();
            showError("Error opening dashboard: " + e.getMessage());
        }
    }
    
    private void showRegistrationDialog() {
        final JDialog registerDialog = new JDialog(this, "Create New Account", true);
        registerDialog.setSize(500, 600);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(CARD_COLOR);
        
        final JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Registration form fields
        final JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        
        // Account Type Selection
        final JLabel accountTypeLabel = createInputLabel("Account Type");
        final String[] accountTypes = {"Farmer", "General User"};
        final JComboBox<String> accountTypeCombo = new JComboBox<>(accountTypes);
        styleComboBox(accountTypeCombo);
        
        // Username
        final JLabel regUsernameLabel = createInputLabel("Username");
        final JTextField regUsernameField = createTextField();
        
        // Password
        final JLabel regPasswordLabel = createInputLabel("Password");
        final JPasswordField regPasswordField = createPasswordField();
        
        // Confirm Password
        final JLabel confirmPasswordLabel = createInputLabel("Confirm Password");
        final JPasswordField confirmPasswordField = createPasswordField();
        
        // Email
        final JLabel emailLabel = createInputLabel("Email Address");
        final JTextField emailField = createTextField();
        
        // Farmer-specific fields (initially hidden)
        final JPanel farmerFieldsPanel = new JPanel();
        farmerFieldsPanel.setLayout(new BoxLayout(farmerFieldsPanel, BoxLayout.Y_AXIS));
        farmerFieldsPanel.setBackground(CARD_COLOR);
        farmerFieldsPanel.setVisible(false);
        
        final JLabel fullNameLabel = createInputLabel("Full Name");
        final JTextField fullNameField = createTextField();
        
        final JLabel locationLabel = createInputLabel("Location");
        final JTextField locationField = createTextField();
        
        final JLabel contactLabel = createInputLabel("Contact Number");
        final JTextField contactField = createTextField();
        
        final JLabel experienceLabel = createInputLabel("Experience (years)");
        final JTextField experienceField = createTextField();
        
        farmerFieldsPanel.add(fullNameLabel);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        farmerFieldsPanel.add(fullNameField);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        farmerFieldsPanel.add(locationLabel);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        farmerFieldsPanel.add(locationField);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        farmerFieldsPanel.add(contactLabel);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        farmerFieldsPanel.add(contactField);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        farmerFieldsPanel.add(experienceLabel);
        farmerFieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        farmerFieldsPanel.add(experienceField);
        
        // Show/hide farmer fields based on account type selection
        accountTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) accountTypeCombo.getSelectedItem();
                farmerFieldsPanel.setVisible("Farmer".equals(selectedType));
                registerDialog.pack();
                registerDialog.setLocationRelativeTo(LoginFrame.this);
            }
        });
        
        formPanel.add(accountTypeLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(accountTypeCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(regUsernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(regUsernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(regPasswordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(regPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(farmerFieldsPanel);
        
        // Buttons
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_COLOR);
        
        // CREATE ACCOUNT button - Green font on white background
        final JButton createAccountButton = createStyledButton("Create Account", Color.WHITE, PRIMARY_GREEN);
        
        // CANCEL button - Green font on light green background
        final JButton cancelButton = createStyledButton("Cancel", LIGHT_GREEN, PRIMARY_GREEN);
        
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount(accountTypeCombo, regUsernameField, regPasswordField, 
                            confirmPasswordField, emailField, fullNameField, locationField, 
                            contactField, experienceField, registerDialog);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerDialog.dispose();
            }
        });
        
        buttonPanel.add(createAccountButton);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(buttonPanel);
        
        registerDialog.add(contentPanel, BorderLayout.CENTER);
        registerDialog.setVisible(true);
    }
    
    private void createAccount(JComboBox<String> accountTypeCombo, JTextField usernameField, 
                             JPasswordField passwordField, JPasswordField confirmPasswordField,
                             JTextField emailField, JTextField fullNameField, JTextField locationField,
                             JTextField contactField, JTextField experienceField, JDialog dialog) {
        try {
            String accountType = (String) accountTypeCombo.getSelectedItem();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String email = emailField.getText().trim();
            
            // Validation
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                showError("Please fill in all required fields.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match.");
                return;
            }
            
            if (password.length() < 6) {
                showError("Password must be at least 6 characters long.");
                return;
            }
            
            boolean success = false;
            
            if ("Farmer".equals(accountType)) {
                String fullName = fullNameField.getText().trim();
                String location = locationField.getText().trim();
                String contact = contactField.getText().trim();
                String experienceStr = experienceField.getText().trim();
                
                if (fullName.isEmpty() || location.isEmpty() || contact.isEmpty() || experienceStr.isEmpty()) {
                    showError("Please fill in all farmer information fields.");
                    return;
                }
                
                int experience;
                try {
                    experience = Integer.parseInt(experienceStr);
                } catch (NumberFormatException e) {
                    showError("Please enter a valid number for experience.");
                    return;
                }
                
                // Register farmer
                success = authService.registerFarmer(username, password, email, fullName, location, contact, experience);
            } else {
                // Register general user
                success = authService.registerUser(username, password, email);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    "Account created successfully!\nYou can now login with your credentials.", 
                    "Registration Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                showError("Failed to create account. Username may already exist.");
            }
            
        } catch (Exception ex) {
            showError("Error creating account: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Authentication Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new LoginFrame().setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Failed to start application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}