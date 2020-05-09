package com.example.library.service;
import com.example.library.domain.Book;
import com.example.library.domain.Library;
import com.example.library.repos.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {
    @Autowired
    LibraryRepository libraryRepository;

    public void save(Library library){
        libraryRepository.save(library);
    }

    public List<Library> listAll() {
        return (List<Library>) libraryRepository.findAll();
    }

    public Library get(Long id) {
        return libraryRepository.findById(id).get();
    }

    public void delete(Long id) {
        libraryRepository.deleteById(id);
    }

    public List<Library> listByName(String name){
        return libraryRepository.findLibrariesByNameContains(name);
    }

    public List<Library> listByBook(Book book){
        return libraryRepository.findLibrariesByBooksContains(book);
    }

    public boolean exist(Long id){
        return libraryRepository.existsById(id);
    }

    public Set<Book> bookSet(Long id){return libraryRepository.findById(id).get().getBooks();}

    public void allLibrariesToConsole(){
        System.out.println("LIBRARIES:");
        for (Library library:
                listAll()) {
            System.out.print("Library-");
            library.toConsole();
        }
    }
}
