/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author apprentice
 */
public interface OrderDao {
    
    // Create
    public void addOrder(String date, Order aOrder);

    // Read
//    public Collection<Order> getAllOrders();
    public Order getOrder(String date, int number);
    public Collection<Order> getOrdersByDate(String date);
    public Set<String> getAllDates();

    // Update
    public void updateOrder(String date, Order orderToUpdate);

    // Delete
    public Order removeOrder(String date, int number);

    // Save and Load
    public void loadFromFile() throws IOException;
    public void saveToFile() throws IOException;
}
