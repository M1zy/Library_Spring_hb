package com.example.Library.Controller;

import com.example.Library.domain.Book;

import com.example.Library.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/book")
@Api(value="Books", description="Operations to books")
public class BookController {
    @Autowired
    private BookService bookService;




    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public Iterable list(Model model) {
        Iterable books = bookService.listAll();
        return books;
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public Book showBook(@PathVariable Long id, Model model){
        Book product = bookService.get(id);
        return product;
    }


    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public Iterable  filter(@PathVariable String keyword,
                             Model model) {
        Iterable books = bookService.listByNameOrAuthor(keyword);
        return books;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveBook(@RequestBody Book book) {
        bookService.save(book);
        return new ResponseEntity("Book saved successfully", HttpStatus.OK);
    }


    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity("Book deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProduct(@PathVariable Long id,@RequestBody Book book) {
        Book storedBook = bookService.get(id);
        storedBook.setAuthor(book.getAuthor());
        storedBook.setDescription(book.getDescription());
        storedBook.setName(book.getName());
        storedBook.setYear(book.getYear());
        bookService.save(book);
        return new ResponseEntity("Book updated successfully", HttpStatus.OK);
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