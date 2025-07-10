package org.example.shopmeat2.modals;

import jakarta.persistence.*;

@Entity
@Table(name="ProductDetails")
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DetailID")
    private int detailID;
    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Product product;
    @Column(name="Description")
    private String description;
    @Column(name="ImageURL")
    private String imageURL;


    public ProductDetails() {
    }

    public ProductDetails(int detailID, Product product, String description, String imageURL) {
        this.detailID = detailID;
        this.product = product;
        this.description = description;
        this.imageURL = imageURL;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
