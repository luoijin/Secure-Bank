package view;

import controller.BankController;
import model.Account;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerAccountListView extends JFrame {
    private BankController controller;
    
    public CustomerAccountListView() {
        controller = BankController.getInstance();
        
        setTitle("My Accounts");
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        
        List<Account> accounts = controller.getCustomerAccounts();
        
        if (accounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "You don't have any accounts yet!",
                "No Accounts",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Style.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = Style.createTitle("My Accounts");
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Account Number", "Holder", "Type", "Balance", "Interest Rate"};
        Object[][] data = new Object[accounts.size()][5];
        
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            data[i][0] = acc.getAccountNumber();
            data[i][1] = acc.getAccountHolder();
            data[i][2] = acc.getAccountType();
            data[i][3] = String.format("₱%.2f", acc.getBalance());
            data[i][4] = String.format("%.1f%%", acc.getInterestRate() * 100);
        }
        
        JTable table = new JTable(data, columns) {
            // Optional: Add alternating row colors
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return c;
            }
        };
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(45); // Increased height for better padding
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom cell renderer with padding
        DefaultTableCellRenderer paddedRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                
                // Add horizontal padding
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                
                // Set alignment
                if (column == 3 || column == 4) { // Balance and Interest Rate columns
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        };
        
        // Apply the padded renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
        }
        
        // Style table header with padding
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Style.CARD_BG);
        header.setForeground(Style.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Taller header
        
        // Custom header renderer with padding
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
        
        // Configure scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Style.BORDER, 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2) // Additional padding around table
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Add some empty space around the table
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Style.BACKGROUND);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Top padding
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        
        // Optional: Add a summary panel at the bottom
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(Style.BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel summaryLabel = new JLabel(String.format(
            "Total Accounts: %d | Total Balance: ₱%.2f", 
            accounts.size(), totalBalance
        ));
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        summaryLabel.setForeground(Style.TEXT_PRIMARY);
        summaryPanel.add(summaryLabel);
        
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}