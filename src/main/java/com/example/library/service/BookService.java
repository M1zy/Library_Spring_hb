package com.example.library.service;
import com.example.library.domain.Book;
import com.example.library.domain.Library;
import com.example.library.repos.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
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

    public Set<Library> listLibraries(Long id){
        return bookRepository.findById(id).get().getLibraries();
    }

    public List<Book> listByNameOrAuthor(String filter){
        return (List<Book>) bookRepository.findBooksByNameContainsOrAuthorContains(filter,filter);
    }

    public boolean exist(Long id){
        return bookRepository.existsById(id);
    }
}
