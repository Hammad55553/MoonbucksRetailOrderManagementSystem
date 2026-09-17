package services;

import data_access.FileStorage;
import models.Order;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private static final String FILE_NAME = "data/orders.txt";
    private List<Order> orders;

    public OrderService() {
        this.orders = FileStorage.loadFromFile(FILE_NAME);
    }

    public void addOrder(Order order) {
        if (getOrderById(order.getOrderId()).isPresent()) {
            System.out.println("Error: Order with ID " + order.getOrderId() + " already exists.");
            return;
        }
        orders.add(order);
        save();
        System.out.println("Order added successfully.");
    }

    public void editOrder(Order newOrderDetails) {
        Optional<Order> existingOpt = getOrderById(newOrderDetails.getOrderId());
        if (existingOpt.isPresent()) {
            orders.remove(existingOpt.get());
            orders.add(newOrderDetails);
            save();
            System.out.println("Order updated successfully.");
        } else {
            System.out.println("Order not found.");
        }
    }

    public void deleteOrder(String id) {
        if (orders.removeIf(o -> o.getOrderId().equalsIgnoreCase(id))) {
            save();
            System.out.println("Order deleted successfully.");
        } else {
            System.out.println("Order not found.");
        }
    }

    public void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        orders.forEach(System.out::println);
    }

    public void viewOrdersByCustomer(String customerId) {
        boolean found = false;
        for (Order o : orders) {
            if (o.getCustomerId().equalsIgnoreCase(customerId)) {
                System.out.println(o);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders found for this customer.");
        }
    }

    public Optional<Order> getOrderById(String id) {
        return orders.stream().filter(o -> o.getOrderId().equalsIgnoreCase(id)).findFirst();
    }

    private void save() {
        FileStorage.saveToFile(orders, FILE_NAME);
    }
}
