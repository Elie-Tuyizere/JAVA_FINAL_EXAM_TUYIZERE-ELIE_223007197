package ui;

import model.User;
import model.Farmer;
import dao.FarmerDAO;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends JFrame {
    private User user;
    private Farmer farmer;
    private FarmerDAO farmerDAO;
    private AuthService authService;

    private final Color PRIMARY_GREEN = new Color(34, 153, 84);
    private final Color BACKGROUND_WHITE = new Color(250, 250, 250);

    public SettingsFrame(User user, Farmer farmer) {
        this.user = user;
        this.farmer = farmer;
        this.farmerDAO = new FarmerDAO();
        this.authService = new AuthService();
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("AgriPortal - Settings: " + farmer.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
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

        JLabel titleLabel = new JLabel("Account Settings");
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

        // Settings Panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile Settings
        JPanel profilePanel = createSettingSection("Profile Information");
        final JTextField fullNameField = new JTextField(farmer.getFullName());
        final JTextField locationField = new JTextField(farmer.getLocation());
        final JTextField contactField = new JTextField(farmer.getContact());
        final JTextField experienceField = new JTextField(String.valueOf(farmer.getExperience()));

        profilePanel.add(createFormField("Full Name:", fullNameField));
        profilePanel.add(createFormField("Location:", locationField));
        profilePanel.add(createFormField("Contact:", contactField));
        profilePanel.add(createFormField("Experience (years):", experienceField));

        JButton updateProfileButton = createButton("Update Profile");
        updateProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProfile(fullNameField.getText(), locationField.getText(), 
                             contactField.getText(), experienceField.getText());
            }
        });
        profilePanel.add(updateProfileButton);

        // Account Settings
        JPanel accountPanel = createSettingSection("Account Settings");
        final JTextField usernameField = new JTextField(user.getUsername());
        final JTextField emailField = new JTextField(user.getEmail());
        final JPasswordField passwordField = new JPasswordField();
        final JPasswordField confirmPasswordField = new JPasswordField();

        usernameField.setEditable(false); // Username shouldn't be changed

        accountPanel.add(createFormField("Username:", usernameField));
        accountPanel.add(createFormField("Email:", emailField));
        accountPanel.add(createFormField("New Password:", passwordField));
        accountPanel.add(createFormField("Confirm Password:", confirmPasswordField));

        JButton updateAccountButton = createButton("Update Account");
        updateAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAccount(emailField.getText(), new String(passwordField.getPassword()), 
                             new String(confirmPasswordField.getPassword()));
            }
        });
        accountPanel.add(updateAccountButton);

        // Application Settings
        JPanel appPanel = createSettingSection("Application Settings");
        final JCheckBox notificationsCheck = new JCheckBox("Enable notifications", true);
        final JCheckBox autoBackupCheck = new JCheckBox("Auto backup data", true);
        final JComboBox<String> themeCombo = new JComboBox<>(new String[]{"Light", "Dark", "System"});

        appPanel.add(notificationsCheck);
        appPanel.add(autoBackupCheck);
        appPanel.add(createFormField("Theme:", themeCombo));

        JButton saveSettingsButton = createButton("Save Settings");
        saveSettingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAppSettings(notificationsCheck.isSelected(), 
                              autoBackupCheck.isSelected(), 
                              (String) themeCombo.getSelectedItem());
            }
        });
        appPanel.add(saveSettingsButton);

        // Add all sections to settings panel
        settingsPanel.add(profilePanel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        settingsPanel.add(accountPanel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        settingsPanel.add(appPanel);

        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createSettingSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 10)));

        return section;
    }

    private JPanel createFormField(String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fieldLabel.setPreferredSize(new Dimension(120, 25));

        fieldPanel.add(fieldLabel, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.SOUTH);

        return fieldPanel;
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
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
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

    private void updateProfile(String fullName, String location, String contact, String experience) {
        try {
            if (fullName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Full name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            farmer.setFullName(fullName.trim());
            farmer.setLocation(location.trim());
            farmer.setContact(contact.trim());
            
            try {
                int exp = Integer.parseInt(experience);
                farmer.setExperience(exp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for experience.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (farmerDAO.updateFarmer(farmer)) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAccount(String email, String password, String confirmPassword) {
        try {
            // Basic validation
            if (email.trim().isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.isEmpty()) {
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // In a real application, you would update the password here
                JOptionPane.showMessageDialog(this, "Password update functionality would be implemented here.");
            }

            user.setEmail(email.trim());
            JOptionPane.showMessageDialog(this, "Account settings updated successfully!\nNote: Password change requires additional implementation.");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAppSettings(boolean notifications, boolean autoBackup, String theme) {
        // In a real application, you would save these to a configuration file or database
        JOptionPane.showMessageDialog(this, 
            "Application settings saved:\n" +
            "Notifications: " + (notifications ? "Enabled" : "Disabled") + "\n" +
            "Auto Backup: " + (autoBackup ? "Enabled" : "Disabled") + "\n" +
            "Theme: " + theme + "\n\n" +
            "Note: These settings would be persisted in a real application.");
    }
}