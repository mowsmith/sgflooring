package com.tsguild.flooringmastery.dto;

import java.text.DecimalFormat;

/**
 *
 * @author Morgan Smith
 */
public class Order {

    private int orderNumber;
    private String customerName;
    private String state;
    private double taxRate;
    private String productType;
    private double area;
    private double costSqFt;
    private double laborSqFt;
    private double materialCost;
    private double laborCost;
    private double tax;
    private double total;

    public Order(int orderNumber, String customerName, StateTax stateTax, Product product, double area) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = stateTax.getState();
        this.taxRate = Math.round(stateTax.getTaxRate() * 100.0) / 100.0;
        this.productType = product.getProductType();
        this.area = Math.round(area * 100.0) / 100.0;
        this.costSqFt = Math.round(product.getCostSqFt() * 100.0) / 100.0;
        this.laborSqFt = Math.round(product.getLaborSqFt() * 100.0) / 100.0;
        this.materialCost = Math.round(area * costSqFt * 100.0) / 100.0;
        this.laborCost = Math.round(area * laborSqFt * 100.0) / 100.0;
        this.tax = Math.round((this.materialCost + this.laborCost) * (taxRate * .01) * 100.0) / 100.0;
        this.total = this.materialCost + this.laborCost + this.tax;
    }

    public Order(int orderNumber, String customerName, String state, double taxRate, String productType, double area, double costSqFt, double laborSqFt, double materialCost, double laborCost, double tax, double total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = Math.round(taxRate * 100.0) / 100.0;
        this.productType = productType;
        this.area = Math.round(area * 100.0) / 100.0;
        this.costSqFt = Math.round(costSqFt * 100.0) / 100.0;
        this.laborSqFt = Math.round(laborSqFt * 100.0) / 100.0;
        this.materialCost = Math.round(materialCost * 100.0) / 100.0;
        this.laborCost = Math.round(laborCost * 100.0) / 100.0;
        this.tax = Math.round(tax * 100.0) / 100.0;;
        this.total = Math.round(total * 100.0) / 100.0;;
    }
    
    

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = Math.round(area * 100.0) / 100.0;
    }

    public double getCostSqFt() {
        return costSqFt;
    }

    public void setCostSqFt(double costSqFt) {
        this.costSqFt = Math.round(costSqFt * 100.0) / 100.0;
    }

    public double getLaborSqFt() {
        return laborSqFt;
    }

    public void setLaborSqFt(double laborSqFt) {
        this.laborSqFt = Math.round(laborSqFt * 100.0) / 100.0;
    }

    public double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(double materialCost) {
        this.materialCost = Math.round(materialCost * 100.0) / 100.0;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        this.laborCost = Math.round(laborCost * 100.0) / 100.0;;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = Math.round(tax * 100.0) / 100.0;;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = Math.round(total * 100.0) / 100.0;;
    }

    @Override
    public String toString() {
        DecimalFormat f = new DecimalFormat("##.00");

        String border = "\n=====================================================================================================\n";
        String line1 = "Order Number: " + orderNumber + ", Customer Name: " + customerName + "\n";
        String line2 = "State: " + state + ", Tax Rate: $" + f.format(taxRate) + "\n";
        String line3 = "Flooring Material: " + productType + ", Cost per Square Foot: $" + costSqFt + ", Labor per Square Foot: $" + laborSqFt + "\n";
        String line4 = "Area: " + f.format(area) + ", Material Cost: $" + f.format(materialCost) + ", Labor Cost: $" + f.format(laborCost) + "\n";
        String line5 = "Tax: $" + f.format(tax) + ", Total: $" + f.format(total);
        String concat = border + line1 + line2 + line3 + line4 + line5 + border;
        return concat;
    }
}