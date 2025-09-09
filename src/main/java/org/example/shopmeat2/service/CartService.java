package org.example.shopmeat2.service;

import org.example.shopmeat2.dal.CartRepository;
import org.example.shopmeat2.modals.Cart;
import org.example.shopmeat2.modals.Product;
import org.example.shopmeat2.modals.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    public Cart addCart(Users user, Product product, int quantity) {
        Optional<Cart> existCart = cartRepository.findByUserAndProduct(user.getUserID(), product.getProductID());
        Cart cart;
        if (existCart.isPresent()) {
            cart = existCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
        }else {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setAddedAt(LocalDateTime.now());
            cart.setStatus((short) 1);

        }
return  cartRepository.save(cart);
    }
    public List<Cart> getAllCartsByUserID(int userID) {
        // Sửa lại cho khớp tên hàm trong repository
        return cartRepository.findCartsByUserUserID(userID);
    }


}
