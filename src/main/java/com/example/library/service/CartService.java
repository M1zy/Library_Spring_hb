package com.example.library.service;
import com.example.library.domain.*;
import com.example.library.repos.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public List<Cart> listAll() {
        return (List<Cart>) cartRepository.findAll();
    }

    public Cart get(Long id){
        return cartRepository.findById(id).get();
    }

    public void save(Cart cart){
        cartRepository.save(cart);
    }

    public void delete(Cart cart){
        cartRepository.delete(cart);
    }

    public boolean exist(Long id){return cartRepository.existsById(id);}
}
