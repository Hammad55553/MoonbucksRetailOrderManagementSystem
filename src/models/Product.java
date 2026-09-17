package models;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private String productId;
    private String name;
    private double rate;
    private double bundlingCharge;

    public Product(String productId, String name, double rate, double bundlingCharge) {
        this.productId = productId;
        this.name = name;
        this.rate = rate;
        this.bundlingCharge = bundlingCharge;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getRate() { return rate; }
    public double getBundlingCharge() { return bundlingCharge; }
    
    public void setName(String name) { this.name = name; }
    public void setRate(double rate) { this.rate = rate; }
    public void setBundlingCharge(double bundlingCharge) { this.bundlingCharge = bundlingCharge; }

    public abstract double calculateTotalCost();

    @Override
    public String toString() {
        return String.format("[%s] %s | Rate: %.2f | Packing: %.2f | Total Cost: %.2f", 
                productId, name, rate, bundlingCharge, calculateTotalCost());
    }
}
