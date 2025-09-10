package org.example.shopmeat2.controller;

import org.example.shopmeat2.modals.Cart;
import org.example.shopmeat2.modals.Users;
import org.example.shopmeat2.service.CartService;
import org.example.shopmeat2.service.ProductService;
import org.example.shopmeat2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String listCart(Model model,
                           @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Users user = userService.getUserByUsername(principal.getUsername());
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> carts = cartService.getAllCartsByUserID(user.getUserID());

        // ✅ Tính totalPrice cho từng cart item
        carts.forEach(cart -> {
            if (cart.getProduct() != null && cart.getProduct().getPrice() != null) {
                cart.setTotalPrice(
                        cart.getProduct().getPrice()
                                .multiply(java.math.BigDecimal.valueOf(cart.getQuantity()))
                );
            }
        });

        // ✅ Tính tổng tất cả
        java.math.BigDecimal grandTotal = carts.stream()
                .map(Cart::getTotalPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("carts", carts);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("currentUser", user);

        return "home/cartList";
    }

    @GetMapping("/add_form")
public String addForm(@RequestParam int productId,Model model) {
        var product = productService.getProductById(productId);
        if (product == null) {
            model.addAttribute("errors","San pham ko ton tai");
            return "errors";
        }
        model.addAttribute("product", product);
        return "home/addForm";
}
    @PostMapping("/add")
    public String addToCart(
            @AuthenticationPrincipal User userDetails,
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity,
            Model model
    ) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Users user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy user!");
            return "error";
        }

        var product = productService.getProductById(productId);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại");
            return "error";
        }

        cartService.addCart(user, product, quantity);
        return "redirect:/cart/list";
    }
}
