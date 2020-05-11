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
        return bookRepository.findBooksByNameContainsOrAuthorContains(filter,filter);
    }

    public List<Book> listByName(String filter){
        return bookRepository.findBooksByNameContains(filter);
    }

    public boolean exist(Long id){
        return bookRepository.existsById(id);
    }

    private void booksToConsole(List<Book> books) {
        System.out.println("Book:");
        for (Book book :
                books) {
            System.out.print("Book-");
            book.toConsole();
        }
    }

    public void commandToConsole(String[] args){
        for(int i=0;i<args.length;i++) {
            switch (args[i]) {
                case "all": {
                    booksToConsole(listAll());
                    break;
                }
                case "name": {
                    try {
                        booksToConsole(listByName(args[i++]));
                    }
                    catch (Exception ex){
                        booksToConsole(listAll());
                    }
                    break;
                }
            }
        }
    }

}
