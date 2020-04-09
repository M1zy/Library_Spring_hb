package com.example.library.repos;

import com.example.library.domain.BookRegistration;
import org.springframework.data.repository.CrudRepository;

public interface RegistrationRepository extends CrudRepository<BookRegistration,Long> {

}
