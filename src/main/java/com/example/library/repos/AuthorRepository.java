package com.example.library.repos;

import com.example.library.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
List<Author> findAuthorsByNameContains(String name);
}
