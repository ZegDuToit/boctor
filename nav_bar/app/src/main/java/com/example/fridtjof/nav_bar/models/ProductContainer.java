package com.example.fridtjof.nav_bar.models;

public class ProductContainer {

    public String name;
    public String barcode;
    public Float value;
    public Integer type;
    public String[] distributors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String[] getDistributors() {
        return distributors;
    }

    public void setDistributors(String[] distributors) {
        this.distributors = distributors;
    }

    public ProductContainer(){}

    @Override
    public String toString() {
        if(this.name == null || this.value == null || barcode == null) return "Not Initialized";
        return String.format("%s [\"%s\"] : %s€", this.barcode, this.name, this.value);
    }
}
