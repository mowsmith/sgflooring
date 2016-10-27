package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.StateTax;
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
public class StateTaxDaoImpl implements StateTaxDao {

    private HashMap<String, StateTax> stateTaxes;
    private final String FILE_NAME;
    private final String DELIMITER = ",";

    public StateTaxDaoImpl() {
        this.FILE_NAME = "Data/Taxes.txt";
        this.stateTaxes = new HashMap<>();
    }

    @Override
    public void addStateTax(StateTax aStateTax) {
        stateTaxes.put(aStateTax.getState(), aStateTax);
    }

    @Override
    public Collection<StateTax> getAllStateTaxes() {
        return stateTaxes.values();
    }

    @Override
    public StateTax getStateTax(String state) {
        return stateTaxes.get(state);
    }

    @Override
    public void updateStateTax(StateTax stateToUpdate) {
        stateTaxes.put(stateToUpdate.getState(), stateToUpdate);
    }

    @Override
    public StateTax removeStateTax(String state) {
        return stateTaxes.remove(state);
    }

    @Override
    public void loadFromFile() throws IOException {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));

            while (sc.hasNextLine()) {
                String stateLine = sc.nextLine();
                String[] stateProperties = stateLine.split(DELIMITER);

                if (stateProperties.length != 2) {
                    continue;
                }

                String state = stateProperties[0];
                double taxRate;

                try {
                    taxRate = Double.parseDouble(stateProperties[1]);
                } catch (NumberFormatException e) {
                    Logger.getLogger(StateTaxDaoImpl.class.getName()).log(Level.SEVERE, null, e);
                    continue;
                }

                StateTax s = new StateTax(state, taxRate);
                stateTaxes.put(s.getState(), s);
            }

            sc.close();

        } catch (FileNotFoundException ex) {
            new FileWriter(FILE_NAME);
        }
    }

    @Override
    public void saveToFile() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));

        for (StateTax s : stateTaxes.values()) {
            writer.println(s.getState() + DELIMITER
                    + s.getTaxRate());
        }

        writer.flush();
        writer.close();
    }
}
