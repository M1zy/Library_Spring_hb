package com.example.library.repos;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface BookRentRepository extends CrudRepository<BookRent, Long> {
    BookRent findByBookAndLibraryAndUser(Book book, Library library, User user);
}
