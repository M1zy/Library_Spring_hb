package com.example.library.service;

import com.example.library.domain.Author;
import com.example.library.domain.Orders;
import com.example.library.domain.User;
import com.example.library.repos.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public void save(Orders orders){
        ordersRepository.save(orders);
    }

    public List<Orders> listAll() {
        return (List<Orders>) ordersRepository.findAll();
    }

    public Orders get(Long id) {
        return ordersRepository.findById(id).get();
    }

    public List<Orders> listByUser(User user){
        return (List<Orders>) ordersRepository.findAllByUser(user);
    }
}
