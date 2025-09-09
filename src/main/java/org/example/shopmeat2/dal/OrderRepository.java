package org.example.shopmeat2.dal;

import org.example.shopmeat2.modals.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_UserID(Integer   userId);
}
