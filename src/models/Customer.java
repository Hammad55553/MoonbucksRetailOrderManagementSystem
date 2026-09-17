package models;

import java.io.Serializable;

public class Customer implements Serializable {
    private String customerId;
    private String name;
    private String address;
    private String contactNumber;

    public Customer(String customerId, String name, String address, String contactNumber) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s", customerId, name, address, contactNumber);
    }
}
