package com.example.Library.service;

import com.example.Library.domain.Book;
import com.example.Library.repos.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public void save(Book book){
        bookRepository.save(book);
    }

    public List<Book> listAll() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book get(Long id) {
        return bookRepository.findById(id).get();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> listByNameOrAuthor(String filter){
        return (List<Book>) bookRepository.findBooksByNameContainsOrAuthorContains(filter,filter);
    }

}
