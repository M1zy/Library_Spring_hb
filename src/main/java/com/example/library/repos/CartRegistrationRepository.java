package com.example.library.repos;

import com.example.library.domain.CartRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRegistrationRepository extends CrudRepository<CartRegistration, Long> {
}
