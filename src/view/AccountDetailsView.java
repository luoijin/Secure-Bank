package view;

import controller.BankController;
import model.Account;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings("serial")
public class AccountDetailsView extends JFrame {
    private BankController controller;
    private Account currentAccount;
    private JTable transactionTable;
    private boolean reportExported = false;
    private boolean reportPrinted = false;
    
    public AccountDetailsView() {
        controller = BankController.getInstance();
        
        setTitle("Account Details");
        setSize(900, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add window listener to detect when window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // If a report was exported or printed, refresh the main dashboard
                if (reportExported || reportPrinted) {
                    refreshMainDashboard();
                }
            }
        });
        
        String accountNumber = JOptionPane.showInputDialog(this, 
            "Enter Account Number:",
            "Account Details",
            JOptionPane.QUESTION_MESSAGE);
        
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            Account account = controller.getAccount(accountNumber);
            
            if (account != null) {
                currentAccount = account;
                displayAccountDetails(account);
                // Only show the frame if we have valid account details
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Account not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } else {
            // User clicked Cancel or X on input dialog
            dispose(); // Close without showing frame
        }
    }
    
    private void displayAccountDetails(Account account) {
        // Check access
        if (!controller.getCurrentUser().isAdmin()) {
            boolean hasAccess = false;
            @SuppressWarnings("unused")
			String currentUsername = controller.getCurrentUser().getUsername();
            
            // Check if current user owns this account
            // First check by username match
            if (account.getAccountHolder().equalsIgnoreCase(controller.getCurrentUser().getFullName())) {
                hasAccess = true;
            }
            
            // Also check by account numbers list
            if (!hasAccess) {
                for (String accNum : controller.getCurrentUser().getAccountNumbers()) {
                    if (accNum.equals(account.getAccountNumber())) {
                        hasAccess = true;
                        break;
                    }
                }
            }
            
            if (!hasAccess) {
                JOptionPane.showMessageDialog(this, 
                    "Access denied! You can only view your own accounts.",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Style.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Style.BACKGROUND);
        
        JLabel titleLabel = Style.createTitle("Account Details");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Button panel on the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Style.BACKGROUND);
        
        // Close button
        JButton closeBtn = createCloseButton();
        buttonPanel.add(closeBtn);
        
        // Export to File button
        JButton exportBtn = createExportButton();
        buttonPanel.add(exportBtn);
        
        // Print button
        JButton printBtn = createPrintButton();
        buttonPanel.add(printBtn);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Info Card
        JPanel infoCard = Style.createCard();
        infoCard.setLayout(new GridLayout(2, 3, 20, 10));
        
        addInfoItem(infoCard, "Account Number", account.getAccountNumber());
        addInfoItem(infoCard, "Account Holder", account.getAccountHolder());
        addInfoItem(infoCard, "Account Type", account.getAccountType());
        addInfoItem(infoCard, "Current Balance", String.format("₱%.2f", account.getBalance()));
        addInfoItem(infoCard, "Interest Rate", String.format("%.1f%%", account.getInterestRate() * 100));
        addInfoItem(infoCard, "Total Transactions", String.valueOf(account.getTransactions().size()));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Style.BACKGROUND);
        infoPanel.add(infoCard, BorderLayout.CENTER);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Transaction History
        JPanel transPanel = new JPanel(new BorderLayout(0, 10));
        transPanel.setBackground(Style.BACKGROUND);
        
        JPanel transHeader = new JPanel(new BorderLayout());
        transHeader.setBackground(Style.BACKGROUND);
        
        JLabel transLabel = new JLabel("Transaction History");
        transLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        transLabel.setForeground(Style.TEXT_PRIMARY);
        transHeader.add(transLabel, BorderLayout.WEST);
        transPanel.add(transHeader, BorderLayout.NORTH);
        
        String[] columns = {"Date & Time", "Type", "Amount", "Balance"};
        List<Transaction> transactions = account.getTransactions();
        Object[][] data = new Object[transactions.size()][4];
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            data[i][0] = sdf.format(t.getTimestamp());
            data[i][1] = t.getType();
            data[i][2] = String.format("₱%.2f", t.getAmount());
            data[i][3] = String.format("₱%.2f", t.getBalanceAfter());
        }
        
        transactionTable = new JTable(data, columns) {
            // Alternate row colors
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return c;
            }
        };
        
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionTable.setRowHeight(40);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Style table header
        JTableHeader header = transactionTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Style.CARD_BG);
        header.setForeground(Style.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // Create left alignment renderer with padding for text columns
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                // Add padding to left-aligned cells
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));
                return c;
            }
        };
        
        // Create right alignment renderer with padding for numeric columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                // Add padding to right-aligned cells
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
                return c;
            }
        };
        
        // Apply alignments:
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        transactionTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        // Create header renderer with left alignment and padding
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                // Add padding to header cells
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, Style.BORDER),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                return c;
            }
        };
        
        header.setDefaultRenderer(headerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Style.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        transPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add transaction panel to main panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Style.BACKGROUND);
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(transPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBackground(new Color(156, 163, 175));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(100, 40));
        
        // Hover effect
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(new Color(107, 114, 128));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(new Color(156, 163, 175));
            }
        });
        
        closeBtn.addActionListener(e -> dispose());
        return closeBtn;
    }
    
    private JButton createExportButton() {
        JButton exportBtn = new JButton("Export to File");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportBtn.setBackground(new Color(46, 204, 113)); // Green color
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFocusPainted(false);
        exportBtn.setBorderPainted(false);
        exportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportBtn.setPreferredSize(new Dimension(150, 40));
        
        // Hover effect
        exportBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportBtn.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportBtn.setBackground(new Color(46, 204, 113));
            }
        });
        
        exportBtn.addActionListener(e -> exportReport());
        return exportBtn;
    }
    
    private JButton createPrintButton() {
        JButton printBtn = new JButton("Print");
        printBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        printBtn.setBackground(new Color(52, 152, 219)); // Blue color
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.setBorderPainted(false);
        printBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printBtn.setPreferredSize(new Dimension(120, 40));
        
        // Hover effect
        printBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printBtn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printBtn.setBackground(new Color(52, 152, 219));
            }
        });
        
        printBtn.addActionListener(e -> printReport());
        return printBtn;
    }
    
    private void addInfoItem(JPanel panel, String label, String value) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Style.CARD_BG);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComp.setForeground(Style.TEXT_SECONDARY);
        labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.add(labelComp);
        
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueComp.setForeground(Style.TEXT_PRIMARY);
        valueComp.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.add(valueComp);
        
        panel.add(itemPanel);
    }
    
    private void exportReport() {
        if (currentAccount == null) return;
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Account Details As");
        fileChooser.setSelectedFile(new File(
            "Account_Statement_" + currentAccount.getAccountNumber() + "_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".txt"
        ));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File fileToSave = fileChooser.getSelectedFile();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
            // Write account information
            writer.println("=".repeat(80));
            writer.println("                    ACCOUNT DETAILS STATEMENT");
            writer.println("=".repeat(80));
            writer.println("Generated: " + new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss").format(new java.util.Date()));
            writer.println();
            
            writer.println("ACCOUNT INFORMATION");
            writer.println("-".repeat(80));
            writer.println("Account Number:      " + currentAccount.getAccountNumber());
            writer.println("Account Holder:      " + currentAccount.getAccountHolder());
            writer.println("Account Type:        " + currentAccount.getAccountType());
            writer.println("Current Balance:     ₱" + String.format("%.2f", currentAccount.getBalance()));
            writer.println("Interest Rate:       " + String.format("%.1f%%", currentAccount.getInterestRate() * 100));
            writer.println("Total Transactions:  " + currentAccount.getTransactions().size());
            writer.println();
            
            // Write transaction history
            writer.println("TRANSACTION HISTORY");
            writer.println("-".repeat(80));
            writer.println(String.format("%-25s %-15s %-15s %-15s", 
                "Date & Time", "Type", "Amount", "Balance"));
            writer.println("-".repeat(80));
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            for (Transaction t : currentAccount.getTransactions()) {
                writer.println(String.format("%-25s %-15s ₱%-14.2f ₱%-14.2f",
                    sdf.format(t.getTimestamp()),
                    t.getType(),
                    t.getAmount(),
                    t.getBalanceAfter()));
            }
            
            writer.println("=".repeat(80));
            writer.println("End of Statement");
            
            JOptionPane.showMessageDialog(this,
                "Account details exported successfully!\nSaved to: " + fileToSave.getAbsolutePath(),
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            reportExported = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to export file: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void printReport() {
        if (currentAccount == null) return;
        
        try {
            // Create a temporary text area for printing
            JTextArea printArea = new JTextArea();
            printArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            
            // Build the printable content
            StringBuilder content = new StringBuilder();
            content.append("=".repeat(80)).append("\n");
            content.append("                    ACCOUNT DETAILS STATEMENT\n");
            content.append("=".repeat(80)).append("\n");
            content.append("Generated: ").append(new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss").format(new java.util.Date())).append("\n\n");
            
            content.append("ACCOUNT INFORMATION\n");
            content.append("-".repeat(80)).append("\n");
            content.append("Account Number:      ").append(currentAccount.getAccountNumber()).append("\n");
            content.append("Account Holder:      ").append(currentAccount.getAccountHolder()).append("\n");
            content.append("Account Type:        ").append(currentAccount.getAccountType()).append("\n");
            content.append("Current Balance:     ₱").append(String.format("%.2f", currentAccount.getBalance())).append("\n");
            content.append("Interest Rate:       ").append(String.format("%.1f%%", currentAccount.getInterestRate() * 100)).append("\n");
            content.append("Total Transactions:  ").append(currentAccount.getTransactions().size()).append("\n\n");
            
            content.append("TRANSACTION HISTORY\n");
            content.append("-".repeat(80)).append("\n");
            content.append(String.format("%-25s %-15s %-15s %-15s\n", 
                "Date & Time", "Type", "Amount", "Balance"));
            content.append("-".repeat(80)).append("\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            for (Transaction t : currentAccount.getTransactions()) {
                content.append(String.format("%-25s %-15s ₱%-14.2f ₱%-14.2f\n",
                    sdf.format(t.getTimestamp()),
                    t.getType(),
                    t.getAmount(),
                    t.getBalanceAfter()));
            }
            
            content.append("=".repeat(80)).append("\n");
            content.append("End of Statement\n");
            
            printArea.setText(content.toString());
            
            // Print the content
            boolean complete = printArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this,
                    "Report sent to printer successfully!",
                    "Print Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                reportPrinted = true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Printing failed: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
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