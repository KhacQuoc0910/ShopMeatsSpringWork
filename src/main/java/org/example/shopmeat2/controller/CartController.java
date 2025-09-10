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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        if (principal == null) return "redirect:/login";

        Users user = userService.getUserByUsername(principal.getUsername());
        if (user == null) return "redirect:/login";

        List<Cart> carts = cartService.getAllCartsByUserID(user.getUserID());

        // Tính subtotal cho từng item
        for (Cart cart : carts) {
            if (cart.getProduct() != null && cart.getProduct().getPrice() != null && cart.getQuantity() != null) {
                var unit = cart.getProduct().getPrice().setScale(2, java.math.RoundingMode.HALF_UP);
                var qty  = cart.getQuantity().setScale(2, java.math.RoundingMode.HALF_UP);
                var line = unit.multiply(qty).setScale(2, java.math.RoundingMode.HALF_UP);
                cart.setTotalPrice(line);
            } else {
                cart.setTotalPrice(java.math.BigDecimal.ZERO.setScale(2));
            }
        }

        // Tổng cộng
        java.math.BigDecimal grandTotal = carts.stream()
                .map(Cart::getTotalPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO.setScale(2), java.math.BigDecimal::add)
                .setScale(2, java.math.RoundingMode.HALF_UP);

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
            @RequestParam(name = "quantity") String quantityText,
            Model model
    ) {
        if (userDetails == null) return "redirect:/login";

        Users user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) { model.addAttribute("error", "Không tìm thấy user!"); return "error"; }

        var product = productService.getProductById(productId);
        if (product == null) { model.addAttribute("error", "Sản phẩm không tồn tại"); return "error"; }


        BigDecimal quantity;
        try {
            String norm = quantityText.trim().replace(',', '.');
            quantity = new BigDecimal(norm).setScale(2, java.math.RoundingMode.HALF_UP);
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException();
        } catch (Exception e) {
            model.addAttribute("error", "Số lượng không hợp lệ. Vui lòng nhập: 1, 1,2 hoặc 1,25");
            return "error";
        }

        cartService.addCart(user, product, quantity);
        return "redirect:/cart/list";
    }

    @GetMapping("/edit/{id}")
    public String editCartItemForm(@PathVariable Long id,
                                   @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                                   Model model) {
        if (principal == null) return "redirect:/login";

        Users user = userService.getUserByUsername(principal.getUsername());
        if (user == null) return "redirect:/login";

        // userId là int, cartId là Long
        Cart cart = cartService.findCartByUserIdAndCartID(user.getUserID(), id).orElse(null);
        if (cart == null) {
            model.addAttribute("error", "Không tìm thấy item");
            return "error";
        }

        model.addAttribute("cartItem", cart);
        return "home/editCartItem";
    }

    @PostMapping("/edit/{id}")
    public String updateCartItem(@PathVariable Long id,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                                 @RequestParam("quantity") String quantityText,
                                 Model model) {
        if (principal == null) return "redirect:/login";
        Users user = userService.getUserByUsername(principal.getUsername());
        if (user == null) return "redirect:/login";

        BigDecimal qty;
        try {
            qty = new BigDecimal(quantityText.trim().replace(',', '.')).setScale(2, RoundingMode.HALF_UP);
            if (qty.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException();
        } catch (Exception e) {
            model.addAttribute("error", "Số lượng không hợp lệ"); return "error";
        }

        boolean ok = cartService.updateQuantityForUser(user.getUserID(), id, qty); // userID là int
        if (!ok) { model.addAttribute("error", "Không tìm thấy item hoặc không thuộc user"); return "error"; }

        return "redirect:/cart/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable("id") Long id) {
        cartService.deleteCartByID(id);
        return "redirect:/cart/list";
    }
}
