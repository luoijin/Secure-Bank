package view;

import controller.BankController;
import model.Account;
import model.SavingsAccount;
import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class ReportView extends JFrame {
    private BankController controller;
    
    @SuppressWarnings("unused")
	public ReportView() {
        controller = BankController.getInstance();
        
        setTitle("System Report");
        setSize(900, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Style.BACKGROUND);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Style.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = Style.createTitle("Banking System Report");
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Report Content
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        reportArea.setBackground(Color.WHITE);
        reportArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        String report = generateReportContent();
        reportArea.setText(report);
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Style.BORDER, 1));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Style.BACKGROUND);
        
        JButton exportBtn = Style.createSecondaryButton("Export to File");
        exportBtn.addActionListener(e -> exportReport(report));
        btnPanel.add(exportBtn);
        
        JButton printBtn = Style.createPrimaryButton("Print Report");
        printBtn.addActionListener(e -> printReport(reportArea));
        btnPanel.add(printBtn);
        
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private String generateReportContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(90)).append("\n");
        sb.append("                        BANKING SYSTEM REPORT\n");
        sb.append("                 Generated: ").append(new Date()).append("\n");
        sb.append("═".repeat(90)).append("\n\n");
        
        List<Account> accounts = controller.getAllAccounts();
        
        double totalBalance = 0;
        int savingsCount = 0, checkingCount = 0;
        
        for (Account acc : accounts) {
            totalBalance += acc.getBalance();
            if (acc instanceof SavingsAccount) savingsCount++;
            else checkingCount++;
        }
        
        sb.append("EXECUTIVE SUMMARY\n");
        sb.append("─".repeat(90)).append("\n");
        sb.append(String.format("Total Accounts:           %d\n", accounts.size()));
        sb.append(String.format("  • Savings Accounts:     %d\n", savingsCount));
        sb.append(String.format("  • Checking Accounts:    %d\n", checkingCount));
        sb.append(String.format("Total System Balance:     ₱%,.2f\n", totalBalance));
        sb.append(String.format("Average Balance:          ₱%,.2f\n\n", 
            accounts.isEmpty() ? 0 : totalBalance / accounts.size()));
        
        sb.append("DETAILED ACCOUNT INFORMATION\n");
        sb.append("─".repeat(90)).append("\n");
        sb.append(String.format("%-15s %-25s %-12s %-18s %s\n", 
            "Account No.", "Holder", "Type", "Balance", "Interest Rate"));
        sb.append("─".repeat(90)).append("\n");
        
        for (Account acc : accounts) {
            sb.append(String.format("%-15s %-25s %-12s ₱%-17.2f %.1f%%\n",
                acc.getAccountNumber(),
                acc.getAccountHolder().length() > 25 ? 
                    acc.getAccountHolder().substring(0, 22) + "..." : acc.getAccountHolder(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getInterestRate() * 100));
        }
        
        sb.append("═".repeat(90)).append("\n");
        sb.append("                           End of Report\n");
        sb.append("═".repeat(90)).append("\n");
        
        return sb.toString();
    }
    
    private void exportReport(String content) {
        try {
            String filename = "BankReport_" + System.currentTimeMillis() + ".txt";
            PrintWriter writer = new PrintWriter(filename);
            writer.write(content);
            writer.close();
            
            JOptionPane.showMessageDialog(this, 
                "Report exported successfully!\nFile: " + filename,
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting report: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void printReport(JTextArea textArea) {
        try {
            boolean complete = textArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, 
                    "Report sent to printer successfully!",
                    "Print Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Printing failed: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}