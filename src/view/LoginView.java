package view;

import controller.BankController;
import model.User;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private BankController controller;
    
    public LoginView() {
        controller = BankController.getInstance();
        setTitle("SecureBank - Login");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 0, 0));
        
        // LEFT SIDE - Branding Panel
        JPanel brandingPanel = createBrandingPanel();
        add(brandingPanel);
        
        // RIGHT SIDE - Login Form
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
        JLabel taglineLabel = new JLabel("Banking Made Simple & Secure");
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
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Style.BACKGROUND);
        
        // Welcome Text
        JLabel welcomeLabel = Style.createTitle("Welcome Back");
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(welcomeLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel subtitleLabel = Style.createSubtitle("Sign in to access your account");
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(subtitleLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Username Field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(Style.TEXT_PRIMARY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(userLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        usernameField = Style.createModernTextField(20);
        usernameField.setMaximumSize(new Dimension(450, 48));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(usernameField);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password Field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(Style.TEXT_PRIMARY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passLabel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        passwordField = Style.createModernPasswordField(20);
        passwordField.setMaximumSize(new Dimension(450, 48));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passwordField);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Login Button
        JButton loginBtn = Style.createPrimaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(450, 48));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> login());
        formCard.add(loginBtn);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Register Link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        registerPanel.setBackground(Style.BACKGROUND);
        registerPanel.setMaximumSize(new Dimension(450, 30));
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerLabel.setForeground(Style.TEXT_SECONDARY);
        registerPanel.add(registerLabel);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setForeground(Style.PRIMARY);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> {
            dispose();
            new RegistrationView().setVisible(true);
        });
        registerPanel.add(registerBtn);
        
        formCard.add(registerPanel);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Demo Info Card
        JPanel demoCard = Style.createInfoPanel(Style.PRIMARY_LIGHT);
        demoCard.setLayout(new BoxLayout(demoCard, BoxLayout.Y_AXIS));
        demoCard.setMaximumSize(new Dimension(450, 110));
        demoCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel demoTitle = new JLabel("Demo Admin Account");
        demoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        demoTitle.setForeground(Style.PRIMARY);
        demoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoCard.add(demoTitle);
        
        demoCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel userInfo = new JLabel("Username: admin");
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userInfo.setForeground(Style.TEXT_PRIMARY);
        userInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoCard.add(userInfo);
        
        demoCard.add(Box.createRigidArea(new Dimension(0, 4)));
        
        JLabel passInfo = new JLabel("Password: admin123");
        passInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passInfo.setForeground(Style.TEXT_PRIMARY);
        passInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoCard.add(passInfo);
        
        formCard.add(demoCard);
        
        panel.add(formCard, BorderLayout.NORTH);
        return panel;
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty()) {
            showError("Please enter your username");
            usernameField.requestFocus();
            return;
        }
        
        User user = controller.authenticate(username, password);
        if (user != null) {
            dispose();
            new MainView().setVisible(true);
        } else {
            showError("Invalid credentials! Please try again.");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Login Failed", 
            JOptionPane.ERROR_MESSAGE);
    }
}