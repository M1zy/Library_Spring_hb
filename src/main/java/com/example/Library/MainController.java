package com.example.Library;

import com.example.Library.domain.Book;
import com.example.Library.domain.BookRegistration;
import com.example.Library.domain.Library;
import com.example.Library.repos.BookRepository;

import com.example.Library.repos.ConnectionsRepository;
import com.example.Library.repos.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private  BookRepository bookRepository;

    @Autowired
    private  LibraryRepository libraryRepository;

    @Autowired
    private ConnectionsRepository connectionsRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name,
                            Map<String, Object> model) {
        model.put("name", name);

        return "greeting";
    }

    @GetMapping("/")
    public String main(Map<String,Object> model){
        Iterable<Book> books = bookRepository.findAll();
        Iterable<Library> libraries= libraryRepository.findAll();
        Iterable<BookRegistration> connections= connectionsRepository.findAll();
        model.put("books",books);
        model.put("libraries",libraries);
        model.put("conns",connections);
        return "main";
    }

    @PostMapping
    public String add(@RequestParam String name, @RequestParam String author,
                      @RequestParam Integer year, @RequestParam String description,
                      Map<String,Object> model){
        Book book = new Book(name,author,year,description);
        bookRepository.save(book);
        Iterable<Book> books = bookRepository.findAll();
        model.put("books",books);
        return "redirect:/";
    }
    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                      Map<String,Object> model){
       List<Book> books= bookRepository.findBooksByNameContainsOrAuthorContains(filter,filter);
       model.put("books",books);
       Iterable<Library>libraries=libraryRepository.findAll();
       model.put("libraries",libraries);
       return "main";
    }

    @PostMapping("addLibrary")
    public String addLibrary(@RequestParam String library,
                         Map<String,Object> model) {
        Library lib = new Library(library);
        libraryRepository.save(lib);

        return "redirect:/";
    }




}