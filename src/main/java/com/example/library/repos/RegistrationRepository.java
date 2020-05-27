package com.example.library.repos;

import com.example.library.domain.BookRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends CrudRepository<BookRegistration, Long> {
}
