package com.example.library.repos;
import com.example.library.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
List<Book> findBooksByNameContainsOrAuthorContains(String name,String author);
List<Book> findBooksByNameContains(String name);
Book findBookById(Long index);
}
