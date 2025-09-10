package org.example.shopmeat2.dal;

import org.example.shopmeat2.modals.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

 @Query("SELECT c FROM Cart c WHERE c.user.userID = :userId AND c.product.productID = :productId")
 Optional<Cart> findByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);
 List<Cart> findCartsByUserUserID(int userId);
 List<Cart> findByUser_UserID(int userId);
 Optional<Cart> findByCartIDAndUser_UserID(Long cartId, int userId);
}
