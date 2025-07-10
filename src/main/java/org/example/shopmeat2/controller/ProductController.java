package org.example.shopmeat2.controller;

import org.example.shopmeat2.modals.ProductDetails;
import org.example.shopmeat2.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/HomePages")
public class ProductController {


    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/")
    public String homePage(Model model) {
        return "home/homepages";
    }
    @GetMapping("/list")
    public String list(Model model) {
          model.addAttribute("data",productService.findAll());
          return "home/product_list";
    }
    @PostMapping("/detail")
    public String detailPost(@RequestParam int id, Model model) {
        ProductDetails productDetail = productService.findDetailsByID(id);

        if (productDetail == null) {
            return "error/404";
        }

        model.addAttribute("productDetail", productDetail);
        return "home/product_detail";
    }

}
