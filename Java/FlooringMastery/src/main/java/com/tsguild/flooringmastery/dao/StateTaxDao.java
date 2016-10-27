/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.StateTax;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author apprentice
 */
public interface StateTaxDao {

    // Create
    public void addStateTax(StateTax aStateTax);

    // Read
    public Collection<StateTax> getAllStateTaxes();
    public StateTax getStateTax(String state);

    // Update
    public void updateStateTax(StateTax stateTaxToUpdate);

    // Delete
    public StateTax removeStateTax(String state);

    // Save and Load
    public void loadFromFile() throws IOException;
    public void saveToFile() throws IOException;
}
