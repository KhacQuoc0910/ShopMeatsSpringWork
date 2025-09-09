package org.example.shopmeat2.controller;

import org.example.shopmeat2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/List")
    public String list(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {

            return "redirect:/login";
        }
        model.addAttribute("data", orderService.getAllOrders(id));
        return "home/OrderList";
    }


}
