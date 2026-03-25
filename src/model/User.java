package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private List<String> accountNumbers;
    
    public User(String username, String password, String email, String fullName, String phone, String role) {
        this.username = username != null ? username : "";
        this.password = password != null ? password : "";
        this.email = email != null ? email : "";
        this.fullName = fullName != null ? fullName : username;
        this.phone = phone != null ? phone : "";
        this.role = role != null ? role : "CUSTOMER";
        this.accountNumbers = new ArrayList<>();
    }
    
    // Add this new constructor for backward compatibility
    public User(String username, String password, String role) {
        this(username, password, username + "@bank.com", username, "", role);
    }
    
    // Getters with null safety
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    public String getEmail() { 
        // Return email if set, otherwise generate from username
        return (email != null && !email.isEmpty()) ? email : username + "@bank.com"; 
    }
    
    public String getFullName() { 
        // Return fullName if set, otherwise use username
        return (fullName != null && !fullName.isEmpty()) ? fullName : username; 
    }
    
    public String getPhone() { 
        return phone != null ? phone : ""; 
    }
    
    public String getRole() { return role; }
    public boolean isAdmin() { return "ADMIN".equals(role); }
    
    public List<String> getAccountNumbers() { return accountNumbers; }
    public void addAccount(String accountNumber) { accountNumbers.add(accountNumber); }
    public void removeAccount(String accountNumber) { accountNumbers.remove(accountNumber); }
    
    // Optional: Add method to get primary account
    public String getPrimaryAccountNumber() {
        return accountNumbers.isEmpty() ? null : accountNumbers.get(0);
    }
    
    // Setters for potential future use
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean hasAccount(String accountNumber) {
        // Check if the account holder name matches
        // Or check if the account number is in user's account list
        for (String accNum : this.accountNumbers) {
            if (accNum.equals(accountNumber)) {
                return true;
            }
        }
        return false;
    }
}