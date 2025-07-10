package org.example.shopmeat2.modals;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name="Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ProductID")
    private int productID;
    @Column(name="ProductName")
    private String productName;
    @Column(name="Category")
    private String category;
    @Column(name="Price")
    private double price;
    @Column(name = "StockWeight")
    private double stockWeight;
    @Column(name = "ImageURl")
    private String imageURL;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductDetails> details;
    public Product() {
    }

    public Product(int productID, String productName, String category, double price, double stockWeight, String imageURL) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockWeight = stockWeight;
        this.imageURL = imageURL;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(double stockWeight) {
        this.stockWeight = stockWeight;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<ProductDetails> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetails> details) {
        this.details = details;
    }
}
