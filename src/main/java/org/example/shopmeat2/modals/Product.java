package org.example.shopmeat2.modals;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private int productID;

    @Column(name = "productname")
    private String productName;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stockweight")
    private BigDecimal stockWeight;

    @Column(name = "imageurl")
    private String imageURL;

    @Column(name = "status")
    private short status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductDetails> details;

    public Product() {
    }

    public Product(int productID, String productName, String category, BigDecimal price,
                   BigDecimal stockWeight, String imageURL, short status) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockWeight = stockWeight;
        this.imageURL = imageURL;
        this.status = status;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(BigDecimal stockWeight) {
        this.stockWeight = stockWeight;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public List<ProductDetails> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetails> details) {
        this.details = details;
    }
}
