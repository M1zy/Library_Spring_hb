package com.example.library.service;

import com.example.library.domain.CartRegistration;
import com.example.library.repos.CartRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartRegistrationService {
    @Autowired
    private CartRegistrationRepository cartRegistrationRepository;

    public void save(CartRegistration cartRegistration){
        cartRegistrationRepository.save(cartRegistration);
    }

    public List<CartRegistration> listAll() {
        return (List<CartRegistration>) cartRegistrationRepository.findAll();
    }

    public CartRegistration get(Long id) {
        return cartRegistrationRepository.findById(id).get();
    }

    public void delete(Long id) {
        cartRegistrationRepository.deleteById(id);
    }

    public boolean exist(Long id){
        return cartRegistrationRepository.existsById(id);
    }
}
