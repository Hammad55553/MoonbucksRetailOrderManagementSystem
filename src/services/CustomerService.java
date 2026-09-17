package services;

import data_access.FileStorage;
import models.Customer;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private static final String FILE_NAME = "data/customers.txt";
    private List<Customer> customers;

    public CustomerService() {
        this.customers = FileStorage.loadFromFile(FILE_NAME);
    }

    public void addCustomer(Customer customer) {
        if (getCustomerById(customer.getCustomerId()).isPresent()) {
            System.out.println("Error: Customer with ID " + customer.getCustomerId() + " already exists.");
            return;
        }
        customers.add(customer);
        save();
        System.out.println("Customer added successfully.");
    }

    public void editCustomer(String id, String newName, String newAddress, String newContact) {
        Optional<Customer> customerOpt = getCustomerById(id);
        if (customerOpt.isPresent()) {
            Customer c = customerOpt.get();
            c.setName(newName);
            c.setAddress(newAddress);
            c.setContactNumber(newContact);
            save();
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    public void deleteCustomer(String id) {
        if (customers.removeIf(c -> c.getCustomerId().equalsIgnoreCase(id))) {
            save();
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    public void viewAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        customers.forEach(System.out::println);
    }

    public Optional<Customer> getCustomerById(String id) {
        return customers.stream().filter(c -> c.getCustomerId().equalsIgnoreCase(id)).findFirst();
    }

    private void save() {
        FileStorage.saveToFile(customers, FILE_NAME);
    }
}
