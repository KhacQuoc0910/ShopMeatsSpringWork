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

    // 🟢 Xem giỏ hàng theo user đang đăng nhập
    @GetMapping("/list")
    public String listCart(Model model,
                           @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal) {
        if (principal == null) {
            System.out.println(">> Chưa đăng nhập, redirect về /login");
            return "redirect:/login";
        }
        System.out.println(">> Đang load giỏ hàng cho user: " + principal.getUsername());

        Users user = userService.getUserByUsername(principal.getUsername());
        if (user == null) {
            System.out.println(">> Không tìm thấy user trong DB");
            return "redirect:/login";
        }

        List<Cart> carts = cartService.getAllCartsByUserID(user.getUserID());
        System.out.println(">> Tổng số item: " + carts.size());

        model.addAttribute("carts", carts);
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
