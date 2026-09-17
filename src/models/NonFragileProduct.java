package models;

public class NonFragileProduct extends Product {
    public NonFragileProduct(String productId, String name, double rate, double bundlingCharge) {
        super(productId, name, rate, bundlingCharge);
    }

    @Override
    public double calculateTotalCost() {
        return getRate() + getBundlingCharge();
    }
    
    @Override
    public String toString() {
        return "[Non-Fragile] " + super.toString();
    }
}
