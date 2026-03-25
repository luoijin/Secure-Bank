package view;

import controller.BankController;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class RegistrationView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JCheckBox termsCheckbox;
    private BankController controller;
    
    public RegistrationView() {
        controller = BankController.getInstance();
        setTitle("SecureBank - Create Account");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 0, 0));
        
        // LEFT SIDE - Branding Panel
        JPanel brandingPanel = createBrandingPanel();
        add(brandingPanel);
        
        // RIGHT SIDE - Registration Form
        JPanel formPanel = createFormPanel();
        add(formPanel);
    }
    
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Style.PRIMARY);
        panel.setLayout(new GridBagLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Style.PRIMARY);
        content.setMaximumSize(new Dimension(400, 500));
        
        // App Title
        JLabel titleLabel = new JLabel("SecureBank");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Tagline
        JLabel taglineLabel = new JLabel("Join Our Banking Community");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taglineLabel.setForeground(new Color(219, 234, 254));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(taglineLabel);
        
        content.add(Box.createRigidArea(new Dimension(0, 50)));
        
        
        
        panel.add(content);
        return panel;
    }
    
    @SuppressWarnings("unused")
	private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Style.BACKGROUND);
        panel.setLayout(new BorderLayout());
        
        // Scrollable content
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Style.BACKGROUND);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Style.BACKGROUND);
        
        // Header
        JLabel titleLabel = Style.createTitle("Create Your Account");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(titleLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel subtitleLabel = Style.createSubtitle("Join SecureBank and start managing your finances");
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(subtitleLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Full Name
        addFormField(formCard, "Full Name *", fullNameField = Style.createModernTextField(20));
        
        // Email
        addFormField(formCard, "Email Address *", emailField = Style.createModernTextField(20));
        
        // Phone
        addFormField(formCard, "Phone Number", phoneField = Style.createModernTextField(20));
        
        // Username
        JLabel usernameLabel = new JLabel("Username *");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(Style.TEXT_PRIMARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(usernameLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        usernameField = Style.createModernTextField(20);
        usernameField.setMaximumSize(new Dimension(450, 48));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(usernameField);
        
        JLabel usernameHint = Style.createMutedText("Only letters, numbers, and underscores");
        usernameHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(usernameHint);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password
        JLabel passwordLabel = new JLabel("Password *");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(Style.TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passwordLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        passwordField = Style.createModernPasswordField(20);
        passwordField.setMaximumSize(new Dimension(450, 48));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passwordField);
        
        JLabel passHint = Style.createMutedText("Minimum 8 characters with at least one number");
        passHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passHint);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Confirm Password
        addFormField(formCard, "Confirm Password *", confirmPasswordField = Style.createModernPasswordField(20));
        
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Terms Checkbox
        termsCheckbox = new JCheckBox("I agree to the Terms and Conditions");
        termsCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        termsCheckbox.setBackground(Style.BACKGROUND);
        termsCheckbox.setForeground(Style.TEXT_PRIMARY);
        termsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(termsCheckbox);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Register Button
        JButton registerBtn = Style.createPrimaryButton("Register");
        registerBtn.setMaximumSize(new Dimension(450, 48));
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.addActionListener(e -> register());
        formCard.add(registerBtn);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        loginPanel.setBackground(Style.BACKGROUND);
        loginPanel.setMaximumSize(new Dimension(450, 30));
        loginPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginLabel.setForeground(Style.TEXT_SECONDARY);
        loginPanel.add(loginLabel);
        
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setForeground(Style.PRIMARY);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        loginPanel.add(loginBtn);
        
        formCard.add(loginPanel);
        
        contentWrapper.add(formCard, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void addFormField(JPanel parent, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Style.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
        
        field.setMaximumSize(new Dimension(450, 48));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(field);
        
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // Validation
        if (fullName.isEmpty()) {
            showError("Full name is required!");
            fullNameField.requestFocus();
            return;
        }
        
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Please enter a valid email address!");
            emailField.requestFocus();
            return;
        }
        
        if (username.isEmpty()) {
            showError("Username is required!");
            usernameField.requestFocus();
            return;
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showError("Username can only contain letters, numbers, and underscores!");
            usernameField.requestFocus();
            return;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters!");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Password is required!");
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters!");
            passwordField.requestFocus();
            return;
        }
        
        if (!password.matches(".*\\d.*")) {
            showError("Password must contain at least one number!");
            passwordField.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            confirmPasswordField.requestFocus();
            return;
        }
        
        if (!termsCheckbox.isSelected()) {
            showError("Please agree to the Terms and Conditions!");
            return;
        }
        
        if (controller.usernameExists(username)) {
            showError("Username already exists. Please choose a different one.");
            usernameField.requestFocus();
            return;
        }
        
        if (controller.emailExists(email)) {
            showError("Email address is already registered.");
            emailField.requestFocus();
            return;
        }
        
        boolean success = controller.registerCustomer(username, password, email, fullName, phone);
        
        if (success) {
            showSuccess(fullName);
        } else {
            showError("Registration failed. Please try again.");
        }
    }
    
    private void showSuccess(String name) {
        JOptionPane.showMessageDialog(this,
            String.format("<html><div style='padding: 10px; text-align: center;'>" +
                "<h2 style='color: #059669; margin-bottom: 10px;'>🎉 Welcome to SecureBank!</h2>" +
                "<p style='font-size: 13px; margin: 5px 0;'><b>%s</b>, your account has been created successfully.</p>" +
                "<p style='font-size: 12px; color: #64748B; margin-top: 10px;'>You can now login with your credentials.</p>" +
                "</div></html>", name),
            "Registration Successful",
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        new LoginView().setVisible(true);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Registration Error",
            JOptionPane.ERROR_MESSAGE);
    }
}