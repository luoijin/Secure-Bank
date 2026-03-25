package view;

import controller.BankController;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class CreateAccountView extends JFrame {
    private JTextField holderField;
    private JComboBox<String> typeCombo;
    private JTextField balanceField;
    private BankController controller;
    
    @SuppressWarnings("unused")
	public CreateAccountView() {
        controller = BankController.getInstance();
        setTitle("Create New Account");
        setSize(500, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Style.BACKGROUND);
        
        JPanel cardPanel = Style.createCard();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setPreferredSize(new Dimension(420, 380));
        
        JLabel titleLabel = Style.createTitle("Create Account");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(titleLabel);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Account Holder
        JLabel holderLabel = new JLabel("Account Holder Name");
        holderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        holderLabel.setForeground(Style.TEXT_PRIMARY);
        holderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(holderLabel);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        holderField = Style.createModernTextField(20);
        holderField.setMaximumSize(new Dimension(380, 45));
        holderField.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(holderField);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Account Type
        JLabel typeLabel = new JLabel("Account Type");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(Style.TEXT_PRIMARY);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(typeLabel);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CHECKING"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setMaximumSize(new Dimension(380, 45));
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(typeCombo);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Initial Balance
        JLabel balanceLabel = new JLabel("Initial Balance (₱)");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        balanceLabel.setForeground(Style.TEXT_PRIMARY);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(balanceLabel);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        balanceField = Style.createModernTextField(20);
        balanceField.setMaximumSize(new Dimension(380, 45));
        balanceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(balanceField);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Style.CARD_BG);
        btnPanel.setMaximumSize(new Dimension(380, 45));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelBtn = Style.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);
        
        JButton createBtn = Style.createPrimaryButton("Create Account");
        createBtn.addActionListener(e -> createAccount());
        btnPanel.add(createBtn);
        
        cardPanel.add(btnPanel);
        
        mainPanel.add(cardPanel);
        add(mainPanel);
    }
    
    private void createAccount() {
        try {
            String holder = holderField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            double balance = Double.parseDouble(balanceField.getText());
            
            if (holder.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Account holder name cannot be empty!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (balance <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Initial balance must be positive!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if ("SAVINGS".equals(type) && balance < 100) {
                int option = JOptionPane.showConfirmDialog(this,
                    "Savings account requires minimum ₱100. Proceed anyway?",
                    "Minimum Balance Warning", 
                    JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            String accountNumber = controller.createAccount(holder, type, balance);
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\nAccount Number: " + accountNumber,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Auto-refresh the main dashboard
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof MainView) {
                    MainView mainView = (MainView) window;
                    mainView.dispose();
                    new MainView().setVisible(true);
                    break;
                }
            }
            
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for balance!",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
