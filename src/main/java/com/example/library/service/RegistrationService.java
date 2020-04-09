package com.example.library.service;

import com.example.library.domain.BookRegistration;
import com.example.library.repos.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    public BookRegistration get(Long id){
        return registrationRepository.findById(id).get();
    }
}
