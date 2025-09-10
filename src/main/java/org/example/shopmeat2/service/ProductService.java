package org.example.shopmeat2.service;

import org.example.shopmeat2.dal.ProductDetailRepository;
import org.example.shopmeat2.dal.ProductRepository;
import org.example.shopmeat2.modals.Product;
import org.example.shopmeat2.modals.ProductDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductDetailRepository detail;

    public ProductService(ProductRepository productRepository, ProductDetailRepository detail) {
        this.productRepository = productRepository;
        this.detail = detail;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }


// product detail

    public ProductDetails findDetailsByID(int detailID) {
        return detail.findById(detailID).orElse(null);
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
