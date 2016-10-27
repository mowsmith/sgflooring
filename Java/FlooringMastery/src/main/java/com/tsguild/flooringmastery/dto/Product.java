package com.tsguild.flooringmastery.dto;

import java.text.DecimalFormat;

/**
 *
 * @author Morgan Smith
 */
public class Product {

    private String productType;
    private double costSqFt;
    private double laborSqFt;

    public Product(String productType, double costSqFt, double laborSqFt) {
        this.productType = productType;
        this.costSqFt = Math.round(costSqFt * 100.0) / 100.0;
        this.laborSqFt = Math.round(laborSqFt * 100.0) / 100.0;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getCostSqFt() {
        return costSqFt;
    }

    public void setCostSqFt(double costSqFt) {
        this.costSqFt = Math.round(costSqFt * 100.0) / 100.0;;
    }

    public double getLaborSqFt() {
        return laborSqFt;
    }

    public void setLaborSqFt(double laborSqFt) {
        this.laborSqFt = Math.round(laborSqFt * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        productType = productType.substring(0, 1).toUpperCase() + productType.substring(1);
        return "Material: " + productType + ", Cost per SqFt: $" + costSqFt + ", Labor per SqFt: $" + laborSqFt;
    }
}
