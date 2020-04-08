package com.example.library.controller;

import com.example.library.domain.Book;

import com.example.library.dto.BookDto;
import com.example.library.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import org.modelmapper.ModelMapper;
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
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    private BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        return bookDto;
    }

    private Book convertToEntity(BookDto bookDto) throws ParseException {
        Book book = modelMapper.map(bookDto, Book.class);

        if (bookDto.getId() != null) {
            Book oldBook = bookService.get(bookDto.getId());
            book.setDescription(oldBook.getDescription());
            book.setLibraries(oldBook.getLibraries());
        }
        return book;
    }



    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public List<BookDto> list(Model model) {
        List<Book> books = bookService.listAll();
        return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public BookDto showBook(@PathVariable Long id){
        Book book = bookService.get(id);
        return convertToDto(book);
    }


    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<BookDto>  filter(@PathVariable String keyword,
                             Model model) {
        List<Book> books = bookService.listByNameOrAuthor(keyword);
        return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveBook(@RequestBody BookDto bookDto) throws ParseException {
        Book book = convertToEntity(bookDto);
        bookService.save(book);
        return new ResponseEntity("Book saved successfully", HttpStatus.OK);
    }


    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity("Book deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProduct(@PathVariable Long id,@RequestBody BookDto bookDto) throws ParseException {
        Book storedBook = bookService.get(id);
        Book book = convertToEntity(bookDto);
        storedBook.setAuthor(book.getAuthor());
        storedBook.setDescription(book.getDescription());
        storedBook.setName(book.getName());
        storedBook.setYear(book.getYear());
        bookService.save(book);
        return new ResponseEntity("Book updated successfully", HttpStatus.OK);
    }





}