package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String accountNumber;
    protected String accountHolder;
    protected double balance;
    protected List<Transaction> transactions;
    
    public Account(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        transactions.add(new Transaction("INITIAL_DEPOSIT", initialBalance, balance));
    }
    
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    
    public abstract String getAccountType();
    public abstract double getInterestRate();
    public abstract void applyInterest();
    public abstract double calculateMonthlyInterest();
    protected abstract boolean canWithdraw(double amount);
    
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (canWithdraw(amount)) {
            balance -= amount;
            transactions.add(new Transaction("WITHDRAWAL", -amount, balance));
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance));
    }
}
