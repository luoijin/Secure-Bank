package view;

import controller.BankController;
import model.Account;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class AccountListView extends JFrame {
    private BankController controller;
    
    public AccountListView() {
        controller = BankController.getInstance();
        
        setTitle("All Accounts");
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Style.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = Style.createTitle("All Bank Accounts");
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Account Number", "Holder", "Type", "Balance", "Interest Rate"};
        List<Account> accounts = controller.getAllAccounts();
        Object[][] data = new Object[accounts.size()][5];
        
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            data[i][0] = acc.getAccountNumber();
            data[i][1] = acc.getAccountHolder();
            data[i][2] = acc.getAccountType();
            data[i][3] = String.format("₱%.2f", acc.getBalance());
            data[i][4] = String.format("%.1f%%", acc.getInterestRate() * 100);
        }
        
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Create renderer with padding for all cells
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                // Add uniform padding to all cells
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                
                // Set alignment based on column
                if (column == 3 || column == 4) { // Balance and Interest Rate columns
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        };
        
        // Apply the renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        
        // Style table header with padding
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Style.CARD_BG);
        header.setForeground(Style.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // Create header renderer with padding
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                // Add generous padding to header
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, Style.BORDER),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                return c;
            }
        };
        
        header.setDefaultRenderer(headerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Style.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
}