package service;

import dao.UserDAO;
import dao.FarmerDAO;
import model.User;
import model.Farmer;

public class AuthService {
    private UserDAO userDAO;
    private FarmerDAO farmerDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
        this.farmerDAO = new FarmerDAO();
    }
    
    public User login(String username, String password) {
        return userDAO.authenticateUser(username, password);
    }
    
    public boolean registerFarmer(String username, String password, String email, 
                                 String fullName, String location, String contact, int experience) {
        // Check if username already exists
        if (userDAO.usernameExists(username)) {
            System.err.println("Username already exists: " + username);
            return false;
        }
        
        // Create user first
        User user = new User(username, password, "FARMER", email);
        boolean userCreated = userDAO.createUser(user);
        
        if (userCreated && user.getUserID() > 0) {
            // Create farmer profile
            Farmer farmer = new Farmer(user.getUserID(), fullName, location, contact, experience);
            return farmerDAO.createFarmer(farmer);
        }
        return false;
    }
    
    public Farmer getFarmerProfile(int userID) {
        return farmerDAO.getFarmerByUserID(userID);
    }
    
    public boolean registerUser(String username, String password, String email) {
        if (userDAO.usernameExists(username)) {
            System.err.println("Username already exists: " + username);
            return false;
        }
        
        User user = new User(username, password, "USER", email);
        return userDAO.createUser(user);
    }
}