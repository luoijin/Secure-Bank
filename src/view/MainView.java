package view;

import controller.BankController;
import model.Account;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@SuppressWarnings("serial")
public class MainView extends JFrame {
    private BankController controller;
    
    public MainView() {
        controller = BankController.getInstance();
        User user = controller.getCurrentUser();
        
        if (user == null) {
            JOptionPane.showMessageDialog(this, "No user logged in!");
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        
        setTitle("SecureBank - Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Style.BACKGROUND);
        
        // Modern Header
        JPanel headerPanel = createModernHeader(user);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content with Stats and Menu
        JScrollPane scrollPane = new JScrollPane(createContentPanel(user));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    @SuppressWarnings("unused")
    private JPanel createModernHeader(User user) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Style.BORDER),
            BorderFactory.createEmptyBorder(24, 40, 24, 40)
        ));

        // Left: Welcome message
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome back, " + user.getFullName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Style.TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(welcomeLabel);

        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(Style.TEXT_SECONDARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(dateLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Right: Role badge and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        JLabel roleBadge = Style.createBadge(
            user.getRole(),
            user.isAdmin() ? Style.PRIMARY_LIGHT : Style.SUCCESS_LIGHT,
            user.isAdmin() ? Style.PRIMARY : Style.SUCCESS
        );
        rightPanel.add(roleBadge);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setForeground(Style.TEXT_PRIMARY);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Style.BORDER, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                logoutBtn.setBackground(Style.BACKGROUND);
            }
            public void mouseExited(MouseEvent evt) {
                logoutBtn.setBackground(Color.WHITE);
            }
        });
        
        rightPanel.add(logoutBtn);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel(User user) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Style.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));
        
        // Statistics Section
        JPanel statsPanel = createStatsPanel(user);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statsPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        
        JPanel menuGrid = createMenuGrid(user);
        menuGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(menuGrid);
        
        return contentPanel;
    }
    
    private JPanel createStatsPanel(User user) {
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.setBackground(Style.BACKGROUND);
        statsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Get statistics using the controller methods
        int totalAccounts = controller.getAccountCountForCurrentUser();
        double totalBalance = controller.getTotalBalanceForCurrentUser();
        int recentTransactions = controller.getTransactionCountForCurrentUser();
        
        // Create stat cards
        statsContainer.add(Style.createStatCard(
            user.isAdmin() ? "Total Accounts" : "My Accounts",
            String.valueOf(totalAccounts),
            Style.PRIMARY
        ));
        
        statsContainer.add(Style.createStatCard(
            user.isAdmin() ? "Total System Balance" : "Total Balance",
            String.format("₱%.2f", totalBalance),
            Style.SUCCESS
        ));
        
        statsContainer.add(Style.createStatCard(
            user.isAdmin() ? "All Transactions" : "Transactions",
            String.valueOf(recentTransactions),
            Style.WARNING
        ));
        
        return statsContainer;
    }
    
    @SuppressWarnings("unused")
    private JPanel createMenuGrid(User user) {
        JPanel gridContainer = new JPanel(new GridLayout(0, 3, 20, 20));
        gridContainer.setBackground(Style.BACKGROUND);

        if (user.isAdmin()) {
            gridContainer.add(createActionCard(
                "Create Account", 
                "Add new bank account to the system",
                e -> new CreateAccountView().setVisible(true)
            ));
            
            gridContainer.add(createActionCard(
                "All Accounts", 
                "View and manage all accounts",
                e -> new AccountListView().setVisible(true)
            ));
            
            gridContainer.add(createActionCard(
                "Apply Interest", 
                "Apply interest to all accounts",
                e -> applyInterest()
            ));
            
            gridContainer.add(createActionCard(
                "Generate Report", 
                "View detailed system reports",
                e -> new ReportView().setVisible(true)
            ));
        } else {
            gridContainer.add(createActionCard(
                "Open Account", 
                "Create a new bank account",
                e -> new CreateAccountView().setVisible(true)
            ));
            
            gridContainer.add(createActionCard(
                "My Accounts", 
                "View all your accounts",
                e -> new CustomerAccountListView().setVisible(true)
            ));
        }

        // Common actions
        gridContainer.add(createActionCard(
            "Deposit", 
            "Deposit money into account",
            e -> new TransactionView("DEPOSIT").setVisible(true)
        ));
        
        gridContainer.add(createActionCard(
            "Withdraw", 
            "Withdraw money from account",
            e -> new TransactionView("WITHDRAW").setVisible(true)
        ));
        
        gridContainer.add(createActionCard(
            "Account Details", 
            "View transaction history",
            e -> new AccountDetailsView()
        ));
        
        return gridContainer;
    }
    
    private JPanel createActionCard(String title, String description, ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBackground(Style.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Style.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(320, 130));
        
        // Content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Style.CARD_BG);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Style.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(titleLabel);
        
        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(Style.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(descLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click"));
            }
            
            @Override
            public void mouseEntered(MouseEvent evt) {
                card.setBackground(Style.CARD_HOVER);
                textPanel.setBackground(Style.CARD_HOVER);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.PRIMARY, 2),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                card.setBackground(Style.CARD_BG);
                textPanel.setBackground(Style.CARD_BG);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.BORDER, 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
            }
        });
        
        return card;
    }
    
    private void applyInterest() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to apply interest to all accounts?",
            "Confirm Action",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.applyInterestToAll();
            JOptionPane.showMessageDialog(this, 
                "Interest applied to all accounts successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            // Auto-refresh the dashboard
            refreshDashboard();
        }
    }

    // Add this helper method to MainView.java
    private void refreshDashboard() {
        dispose();
        new MainView().setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.logout();
            dispose();
            new LoginView().setVisible(true);
        }
    }
}