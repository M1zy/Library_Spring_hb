package com.example.library.service;

import com.example.library.domain.BookRegistration;
import com.example.library.repos.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    public void save(BookRegistration registration){
        registrationRepository.save(registration);
    }

    public List<BookRegistration> listAll() {
        return (List<BookRegistration>) registrationRepository.findAll();
    }

    public BookRegistration get(Long id) {
        return registrationRepository.findById(id).get();
    }

    public void delete(Long id) {
        registrationRepository.deleteById(id);
    }

    public boolean exist(Long id){
        return registrationRepository.existsById(id);
    }
}
