package models;

public class FragileProduct extends Product {
    private static final double FRAGILE_SURCHARGE = 10.0;

    public FragileProduct(String productId, String name, double rate, double bundlingCharge) {
        super(productId, name, rate, bundlingCharge);
    }

    @Override
    public double calculateTotalCost() {
        // As per requirement: cost for packing fragile items is more than packing non-fragile items
        return getRate() + getBundlingCharge() + FRAGILE_SURCHARGE;
    }
    
    @Override
    public String toString() {
        return "[Fragile] " + super.toString() + " (Includes Surcharge)";
    }
}
