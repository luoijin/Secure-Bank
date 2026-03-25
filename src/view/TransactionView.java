package view;

import controller.BankController;
import model.Account;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class TransactionView extends JFrame {
    private JTextField accountField;
    private JTextField amountField;
    private String transactionType;
    private BankController controller;
    private boolean transactionSuccessful = false;
    
    public TransactionView(String type) {
        this.transactionType = type;
        controller = BankController.getInstance();
        
        boolean isDeposit = "DEPOSIT".equals(type);
        setTitle(isDeposit ? "Deposit Money" : "Withdraw Money");
        setSize(500, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add window listener to detect when window is closed (including X button)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // If transaction was successful, refresh the main dashboard
                if (transactionSuccessful) {
                    refreshMainDashboard();
                }
            }
        });
        
        // Main panel with centered content
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Style.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form card with proper sizing
        JPanel formCard = Style.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formCard.setPreferredSize(new Dimension(420, 420));
        
        // Header section with centered text
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Style.CARD_BG);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(isDeposit ? "Deposit Money" : "Withdraw Money");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Style.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel subtitleLabel = new JLabel(
            isDeposit ? "Add funds to your account" : "Withdraw funds from your account"
        );
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Style.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);
        
        formCard.add(headerPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form fields container - CENTERED
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(Style.CARD_BG);
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Account Number field - CENTERED
        JPanel accountPanel = createFormFieldPanel("Account Number", accountField = Style.createModernTextField(20));
        accountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(accountPanel);
        
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Amount field - CENTERED
        JPanel amountPanel = createFormFieldPanel("Amount (₱)", amountField = Style.createModernTextField(20));
        amountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(amountPanel);
        
        formCard.add(fieldsPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Buttons panel - centered with equal spacing
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setBackground(Style.CARD_BG);
        btnPanel.setMaximumSize(new Dimension(350, 50));
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton cancelBtn = createCancelButton();
        cancelBtn.setPreferredSize(new Dimension(140, 45));
        btnPanel.add(cancelBtn);
        
        JButton submitBtn = createSubmitButton(isDeposit);
        submitBtn.setPreferredSize(new Dimension(140, 45));
        btnPanel.add(submitBtn);
        
        formCard.add(btnPanel);
        
        // Add form card to main panel
        mainPanel.add(formCard);
        add(mainPanel);
        
        // Focus on account field
        SwingUtilities.invokeLater(() -> accountField.requestFocus());
    }
    
    private JPanel createFormFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Style.CARD_BG);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(350, 80));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Style.TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        textField.setMaximumSize(new Dimension(350, 45));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(textField);
        
        return panel;
    }
    
    private JButton createCancelButton() {
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(229, 231, 235));
        cancelBtn.setForeground(Style.TEXT_PRIMARY);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelBtn.setBackground(new Color(209, 213, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelBtn.setBackground(new Color(229, 231, 235));
            }
        });
        
        cancelBtn.addActionListener(e -> {
            dispose(); // Just close without refresh
        });
        return cancelBtn;
    }
    
    private JButton createSubmitButton(boolean isDeposit) {
        String buttonText = isDeposit ? "Deposit" : "Withdraw";
        Color buttonColor = isDeposit ? new Color(34, 197, 94) : new Color(239, 68, 68);
        
        JButton submitBtn = new JButton(buttonText);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBackground(buttonColor);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isDeposit) {
                    submitBtn.setBackground(new Color(21, 128, 61));
                } else {
                    submitBtn.setBackground(new Color(220, 38, 38));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitBtn.setBackground(buttonColor);
            }
        });
        
        submitBtn.addActionListener(e -> processTransaction());
        return submitBtn;
    }
    
    private void processTransaction() {
        try {
            String accountNumber = accountField.getText().trim();
            String amountText = amountField.getText().trim();
            
            if (accountNumber.isEmpty()) {
                showError("Please enter an account number!");
                accountField.requestFocus();
                return;
            }
            
            if (amountText.isEmpty()) {
                showError("Please enter an amount!");
                amountField.requestFocus();
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                showError("Please enter a valid number for amount!");
                amountField.requestFocus();
                return;
            }
            
            if (amount <= 0) {
                showError("Amount must be greater than 0!");
                amountField.requestFocus();
                return;
            }
            
            // Validate amount format (max 2 decimal places)
            if (amountText.contains(".")) {
                String[] parts = amountText.split("\\.");
                if (parts.length > 1 && parts[1].length() > 2) {
                    showError("Amount can only have up to 2 decimal places!");
                    amountField.requestFocus();
                    return;
                }
            }
            
            Account account = controller.getAccount(accountNumber);
            if (account == null) {
                showError("Account not found!");
                accountField.requestFocus();
                return;
            }
            
            // CHECK IF USER HAS ACCESS TO THIS ACCOUNT
            if (!controller.getCurrentUser().isAdmin()) {
                boolean hasAccess = false;
                User currentUser = controller.getCurrentUser();
                
                // Check if account holder name matches user's full name
                if (account.getAccountHolder().equalsIgnoreCase(currentUser.getFullName())) {
                    hasAccess = true;
                }
                
                // Also check if account number is in user's account numbers list
                if (!hasAccess) {
                    for (String accNum : currentUser.getAccountNumbers()) {
                        if (accNum.equals(account.getAccountNumber())) {
                            hasAccess = true;
                            break;
                        }
                    }
                }
                
                if (!hasAccess) {
                    String errorMessage = "DEPOSIT".equals(transactionType) 
                        ? "You can only deposit to your own accounts!"
                        : "You can only withdraw from your own accounts!";
                    showError(errorMessage);
                    accountField.requestFocus();
                    return;
                }
            }
            
            // Special validation for withdrawals - check balance
            if ("WITHDRAW".equals(transactionType) && account.getBalance() < amount) {
                showError("Insufficient balance! Available balance: ₱" + 
                         String.format("%.2f", account.getBalance()));
                amountField.requestFocus();
                return;
            }
            
            boolean success;
            if ("DEPOSIT".equals(transactionType)) {
                success = controller.deposit(accountNumber, amount);
            } else {
                success = controller.withdraw(accountNumber, amount);
            }
            
            if (success) {
                showSuccess(account, amount);
            } else {
                if ("WITHDRAW".equals(transactionType)) {
                    showError("Withdrawal failed! Please try again.");
                } else {
                    showError("Deposit failed! Please try again.");
                }
            }
        } catch (Exception e) {
            showError("An error occurred: " + e.getMessage());
        }
    }
    
    private void showSuccess(Account account, double amount) {
        transactionSuccessful = true;
        
        String type = "DEPOSIT".equals(transactionType) ? "Deposit" : "Withdrawal";
        Color successColor = "DEPOSIT".equals(transactionType) ? new Color(34, 197, 94) : new Color(239, 68, 68);
        
        // Create custom dialog
        JDialog successDialog = new JDialog(this, "Transaction Successful", true);
        successDialog.setSize(400, 300);
        successDialog.setLocationRelativeTo(this);
        successDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Success icon
        JLabel successIcon = new JLabel("✓");
        successIcon.setFont(new Font("Segoe UI", Font.BOLD, 48));
        successIcon.setForeground(successColor);
        successIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successIcon);
        
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Success message
        JLabel successLabel = new JLabel(type + " Successful!");
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        successLabel.setForeground(Style.TEXT_PRIMARY);
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        addDetailRow(detailsPanel, "Amount:", String.format("₱%.2f", amount));
        addDetailRow(detailsPanel, "Account:", account.getAccountNumber());
        addDetailRow(detailsPanel, "New Balance:", String.format("₱%.2f", account.getBalance()));
        
        panel.add(detailsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // OK button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(successColor);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> {
            successDialog.dispose();
            dispose(); // Close the transaction window
        });
        
        panel.add(okButton);
        
        successDialog.add(panel);
        successDialog.setVisible(true);
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComp.setForeground(Style.TEXT_SECONDARY);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueComp.setForeground(Style.TEXT_PRIMARY);
        valueComp.setHorizontalAlignment(SwingConstants.RIGHT);
        
        rowPanel.add(labelComp, BorderLayout.WEST);
        rowPanel.add(valueComp, BorderLayout.EAST);
        
        panel.add(rowPanel);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            "<html><div style='padding: 15px; text-align: center;'>" +
            "<h3 style='color: #DC2626; margin-bottom: 10px;'>Transaction Error</h3>" +
            "<p style='font-size: 14px; margin: 10px 0;'>" + message + "</p>" +
            "</div></html>",
            "Transaction Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void refreshMainDashboard() {
        // Find and refresh the MainView
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof MainView && window.isVisible()) {
                MainView mainView = (MainView) window;
                // Refresh the main view
                mainView.dispose();
                new MainView().setVisible(true);
                break;
            }
        }
    }
}