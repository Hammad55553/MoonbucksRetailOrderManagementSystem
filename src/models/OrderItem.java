package models;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String orderId;
    private Product product;
    private int quantity;

    public OrderItem(String orderId, Product product, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public double getSubTotal() {
        return product.calculateTotalCost() * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Qty: %d) - SubTotal: %.2f", product.getName(), quantity, getSubTotal());
    }
}
