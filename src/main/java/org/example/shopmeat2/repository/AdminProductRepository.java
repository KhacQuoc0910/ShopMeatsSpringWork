package org.example.shopmeat2.repository;

import org.example.shopmeat2.modals.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductNameIgnoreCase(String productName);
}

