package controller;

import model.*;
import java.io.*;
import java.util.*;

public class BankController {
    private static BankController instance;
    private Map<String, Account> accounts;
    private Map<String, User> users;
    private User currentUser;
    private static final String DATA_FILE = "bank_data.ser";
    
    private BankController() {
    	accounts = new HashMap<>();
        users = new HashMap<>();
        
        // Add shutdown hook to save on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving data on shutdown...");
            saveData();
        }));
        
        loadData();
        
        if (users.isEmpty()) {
            initializeDefaultUsers();
        }
    }
    
    public static BankController getInstance() {
        if (instance == null) {
            instance = new BankController();
        }
        return instance;
    }
    
    private void initializeDefaultUsers() {
        // Only add default admin if no users exist
        if (users.isEmpty()) {
            User admin = new User("admin", "admin123", "admin@bank.com", "System Administrator", "000-000-0000", "ADMIN");
            users.put("admin", admin);
            saveData();
        }
    }
    
    // Register a new customer
    public boolean registerCustomer(String username, String password, String email, 
                                   String fullName, String phone) {
        // Check if username already exists
        if (users.containsKey(username)) {
            return false;
        }
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            return false;
        }
        
        // Create new customer user
        User customer = new User(username, password, email, fullName, phone, "CUSTOMER");
        users.put(username, customer);
        
        // Auto-create a checking account for the new customer
        String accountNumber = generateAccountNumber();
        Account account = new CheckingAccount(accountNumber, fullName, 0.0);
        accounts.put(accountNumber, account);
        customer.addAccount(accountNumber);
        
        saveData();
        return true;
    }
    
    // Authenticate user
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }
    
    public String createAccount(String accountHolder, String type, double initialBalance) {
        // Validate inputs
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name is required");
        }
        
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        if ("SAVINGS".equals(type) && initialBalance < 100) {
            throw new IllegalArgumentException("Savings account requires minimum ₱100");
        }
        
        String accountNumber = generateAccountNumber();
        Account account;
        
        if ("SAVINGS".equals(type)) {
            account = new SavingsAccount(accountNumber, accountHolder, initialBalance);
        } else {
            account = new CheckingAccount(accountNumber, accountHolder, initialBalance);
        }
        
        accounts.put(accountNumber, account);
        
        // Link to current user if not admin creating for someone else
        if (currentUser != null && !currentUser.isAdmin()) {
            currentUser.addAccount(accountNumber);
        }
        
        // Also link to account holder user if exists
        for (User user : users.values()) {
            if (user.getFullName().equalsIgnoreCase(accountHolder)) {
                user.addAccount(accountNumber);
                break;
            }
        }
        
        saveData();
        return accountNumber;
    }
    
    // Generate unique account number
    private String generateAccountNumber() {
        String accountNumber;
        do {
            int random = (int)(Math.random() * 10000);
            accountNumber = "ACC" + String.format("%04d", random);
        } while (accounts.containsKey(accountNumber)); // Ensure uniqueness
        
        return accountNumber;
    }
    
    // Get accounts for current user
    public List<Account> getCustomerAccounts() {
        if (currentUser == null || currentUser.isAdmin()) {
            return new ArrayList<>();
        }
        
        List<Account> customerAccounts = new ArrayList<>();
        
        // Method 1: Check by account numbers in user's account list
        for (String accountNumber : currentUser.getAccountNumbers()) {
            Account account = accounts.get(accountNumber);
            if (account != null) {
                customerAccounts.add(account);
            }
        }
        
        // Method 2: Fallback - check by account holder name
        if (customerAccounts.isEmpty()) {
            for (Account account : accounts.values()) {
                if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
                    customerAccounts.add(account);
                    // Add to user's account numbers for future
                    if (!currentUser.getAccountNumbers().contains(account.getAccountNumber())) {
                        currentUser.addAccount(account.getAccountNumber());
                    }
                }
            }
        }
        
        return customerAccounts;
    }
    
    // Check if username exists
    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String normalizedEmail = email.trim().toLowerCase();
        
        for (User user : users.values()) {
            String userEmail = user.getEmail();
            // getEmail() now always returns non-null string
            if (userEmail != null && userEmail.trim().toLowerCase().equals(normalizedEmail)) {
                return true;
            }
        }
        return false;
    }
    
    public User getCurrentUser() { return currentUser; }
    public void logout() { currentUser = null; }
    
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
    
    private boolean hasAccessToAccount(Account account) {
        User currentUser = getCurrentUser();
        
        // Check if account holder name matches
        if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
            return true;
        }
        
        // Check if account number is in user's account list
        for (String accNum : currentUser.getAccountNumbers()) {
            if (accNum.equals(account.getAccountNumber())) {
                return true;
            }
        }
        
        return false;
    }

    
    public boolean deposit(String accountNumber, double amount) {        
        Account account = getAccount(accountNumber);
        if (account == null) return false;
        
        // Check permission for non-admin users
        if (!getCurrentUser().isAdmin()) {
            if (!hasAccessToAccount(account)) {
                return false;
            }
        }
        
        if (account != null && amount > 0) {
            try {
                account.deposit(amount);
                saveData();
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }
    
    public boolean withdraw(String accountNumber, double amount) {
    	Account account = getAccount(accountNumber);
        if (account == null) return false;
        
        // Check permission for non-admin users
        if (!getCurrentUser().isAdmin()) {
            if (!hasAccessToAccount(account)) {
                return false;
            }
        }
        
        // Check balance
        if (account.getBalance() < amount) {
            return false;
        }
        
        if (account != null && amount > 0) {
            boolean success = account.withdraw(amount);
            if (success) saveData();
            return success;
        }
        return false;
    }
    
    public void applyInterestToAll() {
        for (Account account : accounts.values()) {
            account.applyInterest();
        }
        saveData();
    }
    
    // Get total system balance (sum of all accounts)
    public double getTotalSystemBalance() {
        double totalBalance = 0.0;
        for (Account account : accounts.values()) {
            totalBalance += account.getBalance();
        }
        return totalBalance;
    }
    
    // Get total balance for current user
    public double getTotalBalanceForCurrentUser() {
        if (currentUser == null) return 0.0;
        
        if (currentUser.isAdmin()) {
            return getTotalSystemBalance(); // Admin sees total system balance
        } else {
            // Customer sees only their accounts
            double totalBalance = 0.0;
            
            // Check by account numbers in user's account list
            for (String accountNumber : currentUser.getAccountNumbers()) {
                Account account = accounts.get(accountNumber);
                if (account != null) {
                    totalBalance += account.getBalance();
                }
            }
            
            // Fallback - check by account holder name
            if (totalBalance == 0.0) {
                for (Account account : accounts.values()) {
                    if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
                        totalBalance += account.getBalance();
                        // Add to user's account numbers for future reference
                        if (!currentUser.getAccountNumbers().contains(account.getAccountNumber())) {
                            currentUser.addAccount(account.getAccountNumber());
                        }
                    }
                }
            }
            
            return totalBalance;
        }
    }
    
    // Get account count for current user
    public int getAccountCountForCurrentUser() {
        if (currentUser == null) return 0;
        
        if (currentUser.isAdmin()) {
            return accounts.size(); // Admin sees all accounts
        } else {
            int count = currentUser.getAccountNumbers().size();
            
            // Fallback - check by account holder name
            if (count == 0) {
                for (Account account : accounts.values()) {
                    if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
                        count++;
                        if (!currentUser.getAccountNumbers().contains(account.getAccountNumber())) {
                            currentUser.addAccount(account.getAccountNumber());
                        }
                    }
                }
            }
            
            return count;
        }
    }
    
    // Get transaction count for current user
    public int getTransactionCountForCurrentUser() {
        if (currentUser == null) return 0;
        
        int totalTransactions = 0;
        
        if (currentUser.isAdmin()) {
            // Admin: count all transactions in all accounts
            for (Account account : accounts.values()) {
                totalTransactions += account.getTransactions().size();
            }
        } else {
            // Customer: count only their accounts' transactions
            for (String accountNumber : currentUser.getAccountNumbers()) {
                Account account = accounts.get(accountNumber);
                if (account != null) {
                    totalTransactions += account.getTransactions().size();
                }
            }
            
            // Fallback - check by account holder name
            if (totalTransactions == 0) {
                for (Account account : accounts.values()) {
                    if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
                        totalTransactions += account.getTransactions().size();
                        if (!currentUser.getAccountNumbers().contains(account.getAccountNumber())) {
                            currentUser.addAccount(account.getAccountNumber());
                        }
                    }
                }
            }
        }
        
        return totalTransactions;
    }
    
    @SuppressWarnings("unchecked")
	private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Data file not found. Will initialize with default data.");
            return; // File doesn't exist yet
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (Map<String, Account>) ois.readObject();
            users = (Map<String, User>) ois.readObject();
            System.out.println("Loaded " + users.size() + " users and " + accounts.size() + " accounts");
            
            // IMPORTANT: Re-establish user-account relationships
            for (User user : users.values()) {
                for (String accountNumber : user.getAccountNumbers()) {
                    Account account = accounts.get(accountNumber);
                    if (account == null) {
                        // Account doesn't exist - clean up invalid reference
                        user.getAccountNumbers().remove(accountNumber);
                    }
                }
            }
        } catch (EOFException | FileNotFoundException e) {
            // Empty file or first run
            System.out.println("Data file is empty or not found. Starting fresh.");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            // Start with empty collections
            accounts = new HashMap<>();
            users = new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}