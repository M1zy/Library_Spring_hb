package com.example.Library.repos;

import com.example.Library.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
List<Book> findBooksByNameContainsOrAuthorContains(String name,String author);
Book findBookById(Integer index);

}
