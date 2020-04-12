package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import com.example.library.repos.BookRentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookRentService {

    @Autowired
    BookRentRepository bookRentRepository;

    public BookRent bookRent(Book book, Library library, User user){
        return bookRentRepository.findByBookAndLibraryAndUser(book,library,user);
    }
}
