package org.example.shopmeat2.dal;

import org.example.shopmeat2.modals.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetails, Integer> {
}
