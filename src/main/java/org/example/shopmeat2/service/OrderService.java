package org.example.shopmeat2.service;

import org.example.shopmeat2.modals.Orders; // ✅ import đúng entity
import org.example.shopmeat2.dal.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    public List<Orders> getAllOrders(int userId) {
        return orderRepo.findByUser_UserID(userId);
    }
}
