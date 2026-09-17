package services;

import data_access.FileStorage;
import models.Product;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private static final String FILE_NAME = "data/products.txt";
    private List<Product> products;

    public ProductService() {
        this.products = FileStorage.loadFromFile(FILE_NAME);
    }

    public void addProduct(Product product) {
        if (getProductById(product.getProductId()).isPresent()) {
            System.out.println("Error: Product with ID " + product.getProductId() + " already exists.");
            return;
        }
        products.add(product);
        save();
        System.out.println("Product added successfully.");
    }

    public void editProduct(String id, String newName, double newRate, double newCharge) {
        Optional<Product> pOpt = getProductById(id);
        if (pOpt.isPresent()) {
            Product p = pOpt.get();
            p.setName(newName);
            p.setRate(newRate);
            p.setBundlingCharge(newCharge);
            save();
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void deleteProduct(String id) {
        if (products.removeIf(p -> p.getProductId().equalsIgnoreCase(id))) {
            save();
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void viewAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        products.forEach(System.out::println);
    }

    public Optional<Product> getProductById(String id) {
        return products.stream().filter(p -> p.getProductId().equalsIgnoreCase(id)).findFirst();
    }

    private void save() {
        FileStorage.saveToFile(products, FILE_NAME);
    }
}
