package main;

import ui.LoginFrame;
import dao.DatabaseConnection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Test database connection
        try {
            DatabaseConnection.getConnection();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Cannot connect to database. Please ensure:\n" +
                "1. MySQL is running\n" +
                "2. Database 'AgricultureMonitoringSystem' exists\n" +
                "3. Correct username/password in DatabaseConnection.java",
                "Database Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        
        try {
           
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                // Fallback for any issues
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
       
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LoginFrame().setVisible(true);
                } catch (Exception e) {
                    System.err.println("Error creating LoginFrame: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Failed to initialize application interface.\n" +
                        "Please check the console for details.",
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}