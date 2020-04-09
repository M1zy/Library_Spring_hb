package com.example.library.repos;

import com.example.library.domain.Book;
import com.example.library.domain.Library;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
List<Book> findBooksByNameContainsOrAuthorContains(String name,String author);
Book findBookById(Integer index);
}
