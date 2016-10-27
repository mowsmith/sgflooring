/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Product;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author apprentice
 */
public interface ProductDao {

    // Create
    public void addProduct(Product aProduct);

    // Read
    public Collection<Product> getAllProducts();
    public Product getProduct(String material);

    // Update
    public void updateProduct(Product productToUpdate);

    // Delete
    public Product removeProduct(String material);

    // Save and Load
    public void loadFromFile() throws IOException;
    public void saveToFile() throws IOException;

}
