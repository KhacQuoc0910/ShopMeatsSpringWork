package org.example.shopmeat2.service;

import org.example.shopmeat2.dal.CartRepository;
import org.example.shopmeat2.modals.Cart;
import org.example.shopmeat2.modals.Product;
import org.example.shopmeat2.modals.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    //thêm
    @Transactional
    public Cart addCart(Users user, Product product, BigDecimal quantity) {
        if (user == null || product == null) {
            throw new IllegalArgumentException("User/product is null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        // Chuẩn hoá số lượng: tối đa 2 số lẻ
        BigDecimal qty = quantity.setScale(2, RoundingMode.HALF_UP);

        Optional<Cart> existCart =
                cartRepository.findByUserAndProduct(user.getUserID(), product.getProductID());

        Cart cart;
        if (existCart.isPresent()) {
            cart = existCart.get();
            BigDecimal newQty = cart.getQuantity().add(qty).setScale(2, RoundingMode.HALF_UP);
            cart.setQuantity(newQty);
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(qty);
            cart.setAddedAt(LocalDateTime.now());
            cart.setStatus((short) 1);
        }

        // Tính lại thành tiền = đơn giá * số lượng (đều BigDecimal)
        BigDecimal unitPrice = product.getPrice().setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = unitPrice.multiply(cart.getQuantity())
                .setScale(2, RoundingMode.HALF_UP);
        cart.setTotalPrice(total);

        return cartRepository.save(cart);
    }
    public Optional<Cart> findCartByUserIdAndCartID(int userId, Long cartId) {
        return cartRepository.findByCartIDAndUser_UserID(cartId, userId);
    }
// sửa
    @Transactional
    public boolean updateQuantityForUser(int userId, Long cartId, BigDecimal newQtyRaw) {
        if (cartId == null || newQtyRaw == null || newQtyRaw.compareTo(BigDecimal.ZERO) <= 0) return false;

        BigDecimal newQty = newQtyRaw.setScale(2, RoundingMode.HALF_UP);

        Cart cart = cartRepository.findByCartIDAndUser_UserID(cartId, userId).orElse(null);
        if (cart == null) return false;

        cart.setQuantity(newQty);
        BigDecimal unit = cart.getProduct().getPrice().setScale(2, RoundingMode.HALF_UP);
        cart.setTotalPrice(unit.multiply(newQty).setScale(2, RoundingMode.HALF_UP));

        cartRepository.save(cart);
        return true;
    }

    public List<Cart> getAllCartsByUserID(int userID) {
        return cartRepository.findCartsByUserUserID(userID);
    }

    public void deleteCartByID(long id) {
        cartRepository.deleteById(id);
    }


}
