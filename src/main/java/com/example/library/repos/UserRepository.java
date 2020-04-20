package com.example.library.repos;

import com.example.library.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends org.springframework.data.repository.CrudRepository<User, Long> {
}
