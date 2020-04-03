package com.example.Library.repos;

import com.example.Library.domain.BookRegistration;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionsRepository extends CrudRepository<BookRegistration, Long> {

}
