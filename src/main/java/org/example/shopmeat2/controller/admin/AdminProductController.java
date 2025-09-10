package org.example.shopmeat2.controller.admin;

import org.example.shopmeat2.modals.Product;
import org.example.shopmeat2.service.admin.AdminProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class AdminProductController {
    private final AdminProductService productService;

    public AdminProductController(AdminProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/api/products/{id}/disable")
    public ResponseEntity<Product> disableProduct(@PathVariable Integer id) {
        Product updated = productService.disableProduct(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> addProduct(
            @RequestParam String productName,
            @RequestParam String category,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) BigDecimal stockWeight,
            @RequestParam(required = false) MultipartFile imageFile
    ) throws IOException {
        if (stockWeight == null) stockWeight = BigDecimal.ZERO;
        Product product = productService.addProduct(productName, category, price, stockWeight, imageFile);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Integer id,
            @RequestParam String productName,
            @RequestParam BigDecimal price,
            @RequestParam BigDecimal stockWeight,
            @RequestParam(required = false) MultipartFile imageFile
    ) throws IOException {

        Product updated = productService.updateProduct(id, productName, price, stockWeight, imageFile);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/api/products/{id}/restore")
    public ResponseEntity<Product> restoreProduct(@PathVariable Integer id) {
        Product restored = productService.restoreProduct(id);
        return ResponseEntity.ok(restored);
    }

}



