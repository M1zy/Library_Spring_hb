package com.example.Library.Controller;

import com.example.Library.domain.Book;

import com.example.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Map<String, Object> model) {
        model.put("name", name);

        return "Book/greeting";
    }

    @RequestMapping("/")
    public String main(Map<String, Object> model) {
        Iterable<Book> books = bookService.listAll();
        model.put("books", books);
        return "Book/main";
    }

    @RequestMapping("/search")
    public String filter(@RequestParam String keyword,
                         Map<String, Object> model) {
        Iterable<Book> books = bookService.listByNameOrAuthor(keyword);
        model.put("books", books);
        return "Book/main";
    }

    @RequestMapping("/new")
    public String newBookForm(Map<String, Object> model) {
        Book book = new Book();
        model.put("book", book);
        return "Book/new_book";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveBook(@RequestParam String name, @RequestParam String author,
                           @RequestParam Integer year, @RequestParam String description,
                           Map<String, Object> model) {
        Book book = new Book(name, author, year, description);
        bookService.save(book);
        return "redirect:/";
    }

    @RequestMapping("/edit")
    public String editBookForm(@RequestParam Long id,
                               Map<String, Object> model) {
        Book book = bookService.get(id);
        model.put("book", book);
        return "Book/edit_book";
    }

    @RequestMapping("/delete")
    public String deleteCustomerForm(@RequestParam long id) {
        bookService.delete(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateBook(@RequestParam Long id, @RequestParam String name, @RequestParam String author,
                             @RequestParam Integer year, @RequestParam String description,
                             Map<String, Object> model) {
        Book book = bookService.get(id);
        book.setAuthor(author);
        book.setDescription(description);
        book.setName(name);
        book.setYear(year);
        bookService.save(book);
        return "redirect:/";
    }




  /*  @PostMapping
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
    }*/




}