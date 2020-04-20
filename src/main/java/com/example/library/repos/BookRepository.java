package com.example.library.repos;

import com.example.library.domain.Book;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends org.springframework.data.repository.CrudRepository<Book, Long> {
List<Book> findBooksByNameContainsOrAuthorContains(String name,String author);
Book findBookById(Integer index);
}
