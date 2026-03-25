package model;

public class CheckingAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.01;
    private static final double OVERDRAFT_LIMIT = -500.0;
    
    public CheckingAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance);
    }
    
    @Override
    public String getAccountType() { return "CHECKING"; }
    
    @Override
    public double getInterestRate() { return INTEREST_RATE; }
    
    @Override
    public void applyInterest() {
        if (balance > 0) {
            double interest = balance * INTEREST_RATE;
            balance += interest;
            transactions.add(new Transaction("INTEREST", interest, balance));
        }
    }
    
    @Override
    public double calculateMonthlyInterest() {
        return balance > 0 ? balance * (INTEREST_RATE / 12) : 0;
    }
    
    @Override
    protected boolean canWithdraw(double amount) {
        return (balance - amount) >= OVERDRAFT_LIMIT;
    }
}