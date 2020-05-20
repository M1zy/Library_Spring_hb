package com.example.library.controller;

import com.example.library.domain.Book;
import com.example.library.domain.BookRegistration;
import com.example.library.dto.BookDto;
import com.example.library.dto.RegistrationDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.ftp.FtpService;
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
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    private Mapper mapper = new Mapper();

    @Autowired
    private FtpService ftpService;

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
        List<Book> books = bookService.listByName(keyword);
        return books.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<BookDto> saveBook(@Valid @RequestBody BookDto bookDto){
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
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        if(!bookService.exist(bookDto.getId())){
            throw new RecordNotFoundException("Invalid book id : " + bookDto.getId());
        }
            Book book = mapper.convertToEntity(bookDto);
            bookService.save(book);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/addRegistration", method = RequestMethod.PUT)
    public ResponseEntity<?> addingLibrary(@Valid @RequestBody RegistrationDto registrationDto) {
            try {
                BookRegistration bookRegistration = mapper.convertToEntity(registrationDto);
                Book book = bookService.get(bookRegistration.getBook().getId());
                book.addRegistration(bookRegistration);
                bookService.save(book);
                return new ResponseEntity<>("Book added to library successfully", HttpStatus.OK);
            }
            catch (Exception e){
                throw new RecordNotFoundException("Invalid book or library ids;");
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

    @RequestMapping(value = "/listOfBooks", method = RequestMethod.GET)
    public List<String> listFiles() throws IOException {
         List<String> listFiles = ftpService.listFilenames("");
        return listFiles;
    }

    @RequestMapping(value="/upload/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@PathVariable Long id,@RequestPart(required = true) MultipartFile file) {
        try {
            Book book = bookService.get(id);
            book.setPath(file.getOriginalFilename());
            ftpService.uploadFile(file);
            bookService.save(book);
            return new ResponseEntity<>("Book report was uploaded successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid book id : " + id);
        }
    }

    @RequestMapping(value="/download/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> download(@PathVariable Long id) {
        try {
            Book book = bookService.get(id);
            ftpService.downloadFile(book);
            return new ResponseEntity<>("Book report was downloaded successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException(bookService.get(id).getPath());
        }
    }
}