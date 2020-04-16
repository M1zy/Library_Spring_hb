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
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
    public BookDto getBook(@PathVariable Long id){
        try {
            bookService.get(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
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
        try {
            Book book = mapper.convertToEntity(bookDto);
            bookService.save(book);
            return new ResponseEntity("Book saved successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }


    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return new ResponseEntity("Book deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateBook(@RequestBody BookDto bookDto) throws ParseException {
        try {
            Book storedBook = bookService.get(bookDto.getId());
            Book book = mapper.convertToEntity(bookDto);
            storedBook.setAuthor(book.getAuthor());
            storedBook.setDescription(book.getDescription());
            storedBook.setName(book.getName());
            storedBook.setYear(book.getYear());
            bookService.save(storedBook);
            return new ResponseEntity("Book updated successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/addLibrary/{idBook}_to_{idLibrary}", method = RequestMethod.PUT)
    public ResponseEntity addingLibrary(@PathVariable Long idBook,@PathVariable Long idLibrary) throws ParseException {
        try {
            Book book = bookService.get(idBook);
            Library library = libraryService.get(idLibrary);
            book.addLibrary(library);
            bookService.save(book);
            return new ResponseEntity("Book added to library successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }





}