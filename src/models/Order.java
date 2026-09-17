package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String customerId;
    private List<OrderItem> items;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order ID: %s | Customer ID: %s | Total Amount: %.2f\n", orderId, customerId, calculateTotalAmount()));
        sb.append("Items:\n");
        for (OrderItem item : items) {
            sb.append(" - ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}
