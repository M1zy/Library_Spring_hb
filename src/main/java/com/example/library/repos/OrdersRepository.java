package com.example.library.repos;

import com.example.library.domain.Orders;
import com.example.library.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends CrudRepository<Orders, Long> {
    List<Orders> findAllByUser (User user);
}
