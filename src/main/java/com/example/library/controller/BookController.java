package com.example.library.controller;
import com.example.library.domain.Book;
import com.example.library.domain.Library;
import com.example.library.dto.BookDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.util.BookGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public List<BookDto> list() {
        List<Book> books = bookService.listAll();
        return books.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public ResponseEntity<BookDto> getBook(@PathVariable Long id){
        if(!bookService.exist(id)){
            throw new RecordNotFoundException("Invalid book id : " + id);
        }
        Book book = bookService.get(id);
        return new ResponseEntity<>(mapper.convertToDto(book), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<BookDto>  filter(@PathVariable String keyword) {
        List<Book> books = bookService.listByNameOrAuthor(keyword);
        return books.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<BookDto> saveBook(@Valid @RequestBody BookDto bookDto) throws ParseException {
            Book book = mapper.convertToEntity(bookDto);
            bookService.save(book);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return new ResponseEntity("Book was deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid book id : " + id);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) throws ParseException {
        if(!bookService.exist(bookDto.getId())){
            throw new RecordNotFoundException("Invalid book id : " + bookDto.getId());
        }
            Book storedBook = bookService.get(bookDto.getId());
            Book book = mapper.convertToEntity(bookDto);
            storedBook.setAuthor(book.getAuthor());
            storedBook.setDescription(book.getDescription());
            storedBook.setName(book.getName());
            storedBook.setYear(book.getYear());
            bookService.save(storedBook);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
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
                throw new RecordNotFoundException("Invalid book or library ids : " + idBook + " ," + idLibrary);
            }
    }

    @RequestMapping(value = "/toFile/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelBookReport(@PathVariable Long id) throws IOException {
            if(!bookService.exist(id)){
                throw new RecordNotFoundException("Invalid book id : " +id);
            }
            Book book = bookService.get(id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-disposition", "attachment;filename=book.xlsx");
            BookGenerator bookGenerator = new BookGenerator();
            ByteArrayInputStream in = bookGenerator.toExcel(book);
            in.close();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(in));
    }
}