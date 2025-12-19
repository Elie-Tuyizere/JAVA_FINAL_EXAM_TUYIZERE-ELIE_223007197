package model;

import java.time.LocalDateTime;

public class User {
    private int userID;
    private String username;
    private String password;
    private String userType;
    private String email;
    private LocalDateTime createdAt;
    
    public User() {}
    
    public User(String username, String password, String userType, String email) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
    }
    
    // Getters and Setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "User{userID=" + userID + ", username='" + username + "', userType='" + userType + "'}";
    }
}