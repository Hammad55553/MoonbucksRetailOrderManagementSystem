package main;

import models.*;
import services.CustomerService;
import services.OrderService;
import services.ProductService;
import utils.InputUtils;
import java.util.Optional;

public class Main {
    private static CustomerService customerService = new CustomerService();
    private static ProductService productService = new ProductService();
    private static OrderService orderService = new OrderService();

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println(" Moonbucks Retail Order Management System ");
        System.out.println("===========================================");
        
        while (true) {
            System.out.println("\nLogin As:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            int choice = InputUtils.getInt("Enter choice: ");
            
            if (choice == 1) {
                adminMenu();
            } else if (choice == 2) {
                customerLogin();
            } else if (choice == 3) {
                System.out.println("Exiting System. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Manage Customers");
            System.out.println("2. Manage Products");
            System.out.println("3. Manage Orders");
            System.out.println("4. Logout");
            
            int choice = InputUtils.getInt("Enter choice: ");
            switch (choice) {
                case 1: manageCustomers(); break;
                case 2: manageProducts(); break;
                case 3: manageOrders(); break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerLogin() {
        String customerId = InputUtils.getString("Enter Customer ID to Login: ");
        Optional<Customer> cOpt = customerService.getCustomerById(customerId);
        if (cOpt.isPresent()) {
            System.out.println("Welcome, " + cOpt.get().getName() + "!");
            customerMenu(cOpt.get());
        } else {
            System.out.println("Customer ID not found.");
            InputUtils.pause();
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Place an Order (Add)");
            System.out.println("2. View My Orders");
            System.out.println("3. Delete an Order");
            System.out.println("4. Edit an Order");
            System.out.println("5. Search an Order");
            System.out.println("6. Logout");

            int choice = InputUtils.getInt("Enter choice: ");
            switch (choice) {
                case 1: placeOrder(customer); break;
                case 2: orderService.viewOrdersByCustomer(customer.getCustomerId()); InputUtils.pause(); break;
                case 3: 
                    String orderId = InputUtils.getString("Enter Order ID to delete: ");
                    Optional<Order> oOpt = orderService.getOrderById(orderId);
                    if (oOpt.isPresent() && oOpt.get().getCustomerId().equals(customer.getCustomerId())) {
                        orderService.deleteOrder(orderId);
                    } else {
                        System.out.println("Order not found or access denied.");
                    }
                    break;
                case 4:
                    editOrderPrompt(customer.getCustomerId());
                    break;
                case 5:
                    searchOrderPrompt(customer.getCustomerId());
                    break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void placeOrder(Customer customer) {
        String orderId = InputUtils.getString("Enter New Order ID: ");
        if (orderService.getOrderById(orderId).isPresent()) {
            System.out.println("Order ID already exists!");
            return;
        }
        Order order = new Order(orderId, customer.getCustomerId());
        
        while (true) {
            System.out.println("\nAvailable Products:");
            productService.viewAllProducts();
            System.out.println("Enter 'done' to finish adding items.");
            
            String pId = InputUtils.getString("Enter Product ID to add: ");
            if (pId.equalsIgnoreCase("done")) break;
            
            Optional<Product> pOpt = productService.getProductById(pId);
            if (pOpt.isPresent()) {
                int qty = InputUtils.getInt("Enter quantity: ");
                order.addItem(new OrderItem(orderId, pOpt.get(), qty));
                System.out.println("Item added.");
            } else {
                System.out.println("Product not found.");
            }
        }
        
        if (order.getItems().isEmpty()) {
            System.out.println("No items added. Order cancelled.");
        } else {
            orderService.addOrder(order);
            System.out.println("Order placed successfully! Total: " + order.calculateTotalAmount());
        }
        InputUtils.pause();
    }

    private static void editOrderPrompt(String customerIdRestricted) {
        String id = InputUtils.getString("Enter Order ID to edit: ");
        Optional<Order> existingOpt = orderService.getOrderById(id);
        if (!existingOpt.isPresent()) {
            System.out.println("Order not found.");
            return;
        }
        
        Order existing = existingOpt.get();
        if (customerIdRestricted != null && !existing.getCustomerId().equals(customerIdRestricted)) {
            System.out.println("Access denied. You can only edit your own orders.");
            return;
        }
        
        System.out.println("Re-building order from scratch (Items will be replaced).");
        Order newOrder = new Order(id, existing.getCustomerId());
        
        while (true) {
            System.out.println("\nAvailable Products:");
            productService.viewAllProducts();
            System.out.println("Enter 'done' to finish adding items.");
            
            String pId = InputUtils.getString("Enter Product ID to add: ");
            if (pId.equalsIgnoreCase("done")) break;
            
            Optional<Product> pOpt = productService.getProductById(pId);
            if (pOpt.isPresent()) {
                int qty = InputUtils.getInt("Enter quantity: ");
                newOrder.addItem(new OrderItem(id, pOpt.get(), qty));
                System.out.println("Item added.");
            } else {
                System.out.println("Product not found.");
            }
        }
        
        if (newOrder.getItems().isEmpty()) {
            System.out.println("No items added. Edit cancelled.");
        } else {
            orderService.editOrder(newOrder);
        }
    }

    private static void searchOrderPrompt(String customerIdRestricted) {
        String id = InputUtils.getString("Enter Order ID to search: ");
        Optional<Order> existingOpt = orderService.getOrderById(id);
        if (existingOpt.isPresent()) {
            Order o = existingOpt.get();
            if (customerIdRestricted != null && !o.getCustomerId().equals(customerIdRestricted)) {
                System.out.println("Access denied.");
            } else {
                System.out.println(o);
            }
        } else {
            System.out.println("Order not found.");
        }
        InputUtils.pause();
    }

    // ========== ADMIN CRUD METHODS ==========

    private static void manageCustomers() {
        while (true) {
            System.out.println("\n-- Manage Customers --");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Edit Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Search Customer");
            System.out.println("6. Back");
            int choice = InputUtils.getInt("Choice: ");
            
            if (choice == 1) {
                String id = InputUtils.getString("ID: ");
                String name = InputUtils.getString("Name: ");
                String addr = InputUtils.getString("Address: ");
                String contact = InputUtils.getString("Contact: ");
                customerService.addCustomer(new Customer(id, name, addr, contact));
            } else if (choice == 2) {
                customerService.viewAllCustomers();
                InputUtils.pause();
            } else if (choice == 3) {
                String id = InputUtils.getString("ID of customer to edit: ");
                Optional<Customer> cOpt = customerService.getCustomerById(id);
                if (cOpt.isPresent()) {
                    Customer c = cOpt.get();
                    String name = InputUtils.getOptionalString("New Name", c.getName());
                    String addr = InputUtils.getOptionalString("New Address", c.getAddress());
                    String contact = InputUtils.getOptionalString("New Contact", c.getContactNumber());
                    customerService.editCustomer(id, name, addr, contact);
                } else {
                    System.out.println("Customer not found.");
                }
            } else if (choice == 4) {
                String id = InputUtils.getString("ID to delete: ");
                customerService.deleteCustomer(id);
            } else if (choice == 5) {
                String id = InputUtils.getString("ID to search: ");
                Optional<Customer> c = customerService.getCustomerById(id);
                if (c.isPresent()) System.out.println(c.get());
                else System.out.println("Not found.");
                InputUtils.pause();
            } else if (choice == 6) {
                break;
            }
        }
    }

    private static void manageProducts() {
        while (true) {
            System.out.println("\n-- Manage Products --");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Edit Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Product");
            System.out.println("6. Back");
            int choice = InputUtils.getInt("Choice: ");
            
            if (choice == 1) {
                String id = InputUtils.getString("ID: ");
                String name = InputUtils.getString("Name: ");
                double rate = InputUtils.getDouble("Rate: ");
                double charge = InputUtils.getDouble("Bundling Charge: ");
                int type = InputUtils.getInt("Type (1 for Fragile, 2 for Non-Fragile): ");
                
                if (type == 1) {
                    productService.addProduct(new FragileProduct(id, name, rate, charge));
                } else {
                    productService.addProduct(new NonFragileProduct(id, name, rate, charge));
                }
            } else if (choice == 2) {
                productService.viewAllProducts();
                InputUtils.pause();
            } else if (choice == 3) {
                String id = InputUtils.getString("ID of product to edit: ");
                Optional<Product> pOpt = productService.getProductById(id);
                if (pOpt.isPresent()) {
                    Product p = pOpt.get();
                    String name = InputUtils.getOptionalString("New Name", p.getName());
                    double rate = InputUtils.getOptionalDouble("New Rate", p.getRate());
                    double charge = InputUtils.getOptionalDouble("New Charge", p.getBundlingCharge());
                    productService.editProduct(id, name, rate, charge);
                } else {
                    System.out.println("Product not found.");
                }
            } else if (choice == 4) {
                String id = InputUtils.getString("ID to delete: ");
                productService.deleteProduct(id);
            } else if (choice == 5) {
                String id = InputUtils.getString("ID to search: ");
                Optional<Product> p = productService.getProductById(id);
                if (p.isPresent()) System.out.println(p.get());
                else System.out.println("Not found.");
                InputUtils.pause();
            } else if (choice == 6) {
                break;
            }
        }
    }

    private static void manageOrders() {
        while (true) {
            System.out.println("\n-- Manage Orders (Admin) --");
            System.out.println("1. Add Order");
            System.out.println("2. View All Orders");
            System.out.println("3. Edit Order");
            System.out.println("4. Delete Order");
            System.out.println("5. Search Order");
            System.out.println("6. Back");
            int choice = InputUtils.getInt("Choice: ");
            
            if (choice == 1) {
                String cId = InputUtils.getString("Enter Customer ID for this order: ");
                Optional<Customer> cOpt = customerService.getCustomerById(cId);
                if (cOpt.isPresent()) {
                    placeOrder(cOpt.get());
                } else {
                    System.out.println("Customer not found.");
                }
            } else if (choice == 2) {
                orderService.viewAllOrders();
                InputUtils.pause();
            } else if (choice == 3) {
                editOrderPrompt(null);
            } else if (choice == 4) {
                String id = InputUtils.getString("ID to delete: ");
                orderService.deleteOrder(id);
            } else if (choice == 5) {
                searchOrderPrompt(null);
            } else if (choice == 6) {
                break;
            }
        }
    }
}
