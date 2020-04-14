package com.example.library.controller;

import com.example.library.domain.Book;

import com.example.library.domain.Library;
import com.example.library.dto.BookDto;
import com.example.library.mapper.Mapper;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/book")
@Api(value="Books", description="Operations to books")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public List<BookDto> list(Model model) {
        List<Book> books = bookService.listAll();
        return books.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public BookDto showBook(@PathVariable Long id){
        Book book = bookService.get(id);
        return mapper.convertToDto(book);
    }


    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<BookDto>  filter(@PathVariable String keyword,
                             Model model) {
        List<Book> books = bookService.listByNameOrAuthor(keyword);
        return books.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveBook(@RequestBody BookDto bookDto) throws ParseException {
        Book book = mapper.convertToEntity(bookDto);
        bookService.save(book);
        return new ResponseEntity("Book saved successfully", HttpStatus.OK);
    }


    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity("Book deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateBook(@PathVariable Long id,@RequestBody BookDto bookDto) throws ParseException {
        Book storedBook = bookService.get(id);
        Book book = mapper.convertToEntity(bookDto);
        storedBook.setAuthor(book.getAuthor());
        storedBook.setDescription(book.getDescription());
        storedBook.setName(book.getName());
        storedBook.setYear(book.getYear());
        bookService.save(storedBook);
        return new ResponseEntity("Book updated successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/addLibrary/{idBook}_to_{idLibrary}", method = RequestMethod.PUT)
    public ResponseEntity addingLibrary(@PathVariable Long idBook,@PathVariable Long idLibrary) throws ParseException {
        Book book = bookService.get(idBook);
        Library library = libraryService.get(idLibrary);
        book.addLibrary(library);
        bookService.save(book);
        return new ResponseEntity("Book added to library successfully", HttpStatus.OK);
    }





}