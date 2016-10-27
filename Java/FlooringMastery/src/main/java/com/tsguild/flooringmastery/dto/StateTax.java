
package com.tsguild.flooringmastery.dto;

/**
 *
 * @author Morgan Smith
 */
public class StateTax {

    private String state;
    private double taxRate;

    public StateTax(String state, double taxRate) {
        this.state = state;
        this.taxRate = Math.round(taxRate * 100.0) / 100.0;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = Math.round(taxRate * 100.0) / 100.0;
    }
    
    
}
