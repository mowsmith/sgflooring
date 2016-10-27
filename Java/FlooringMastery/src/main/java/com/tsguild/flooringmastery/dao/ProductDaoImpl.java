package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morgan Smith
 */
public class ProductDaoImpl implements ProductDao {

    private HashMap<String, Product> products;
    private final String FILE_NAME;
    private final String DELIMITER = ",";

    public ProductDaoImpl() {
        this.FILE_NAME = "Data/Products.txt";
        this.products = new HashMap<>();
    }

    @Override
    public void addProduct(Product aProduct) {
        products.put(aProduct.getProductType(), aProduct);
    }

    @Override
    public Collection<Product> getAllProducts() {
        return products.values();
    }

    @Override
    public Product getProduct(String material) {
        return products.get(material);
    }

    @Override
    public void updateProduct(Product productToUpdate) {
        products.put(productToUpdate.getProductType(), productToUpdate);
    }

    @Override
    public Product removeProduct(String material) {
        return products.remove(material);
    }

    @Override
    public void loadFromFile() throws IOException {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));

            while (sc.hasNextLine()) {
                String productLine = sc.nextLine();
                String[] productProperties = productLine.split(DELIMITER);

                if (productProperties.length != 3) {
                    continue;
                }

                String productType = productProperties[0];
                double costSqFt;
                double laborSqFt;
                
                try {
                    costSqFt = Double.parseDouble(productProperties[1]);
                    laborSqFt = Double.parseDouble(productProperties[2]);
                } catch (NumberFormatException e) {
                    Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, e);
                    continue;
                }
                
                Product p = new Product(productType.toLowerCase(), costSqFt, laborSqFt);
                products.put(p.getProductType(), p);
            }

            sc.close();

        } catch (FileNotFoundException ex) {
            new FileWriter(FILE_NAME);
        }
    }

    @Override
    public void saveToFile() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));

        for (Product p : products.values()) {
            writer.println(p.getProductType() + DELIMITER
                    + p.getCostSqFt() + DELIMITER
                    + p.getLaborSqFt());
        }

        writer.flush();
        writer.close();
    }
}
