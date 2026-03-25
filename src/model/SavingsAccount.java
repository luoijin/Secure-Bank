package model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.03;
    private static final double MIN_BALANCE = 100.0;
    
    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance);
    }
    
    @Override
    public String getAccountType() { return "SAVINGS"; }
    
    @Override
    public double getInterestRate() { return INTEREST_RATE; }
    
    @Override
    public void applyInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        transactions.add(new Transaction("INTEREST", interest, balance));
    }
    
    @Override
    public double calculateMonthlyInterest() {
        return balance * (INTEREST_RATE / 12);
    }
    
    @Override
    protected boolean canWithdraw(double amount) {
        return (balance - amount) >= MIN_BALANCE;
    }
}