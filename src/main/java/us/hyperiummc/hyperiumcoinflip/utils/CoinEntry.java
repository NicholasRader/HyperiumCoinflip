package us.hyperiummc.hyperiumcoinflip.utils;

public class CoinEntry {
    private double amount;

    private boolean side;

    private boolean currency;

    public CoinEntry(double amount, boolean side, boolean currency) {
        this.amount = amount;
        this.side = side;
        this.currency = currency;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean getSide() {
        return this.side;
    }

    public boolean getCurrency() {
        return this.currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSide(boolean side) {
        this.side = side;
    }

    public void setCurrency(boolean currency) {
        this.currency = currency;
    }
}
