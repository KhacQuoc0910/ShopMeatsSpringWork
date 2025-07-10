package org.example.shopmeat2.dal;

import org.example.shopmeat2.modals.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
