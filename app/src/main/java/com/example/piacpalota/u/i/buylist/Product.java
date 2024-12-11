package com.example.piacpalota.u.i.buylist;

public class Product {
    private String name;
    private String price;
    private String quantity;
    private String location;
    private String imageUrl;
    private boolean isExpanded;
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String name, String price, String quantity, String location, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isExpanded = false; // Alapértelmezetten nem bővített
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
