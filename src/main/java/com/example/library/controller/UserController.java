package com.example.library.controller;
import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import com.example.library.dto.RentDto;
import com.example.library.dto.UserDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.BookRentService;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.UserService;
import com.example.library.util.UserGenerator;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Api(value="Users", description="Operations to users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookRentService bookRentService;

    @Autowired
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public List<UserDto> list() {
        List<User> users = userService.listAll();
        return users.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        if(!userService.exist(id)){
            throw new RecordNotFoundException("Invalid user id : "+id);
        }
        User user = userService.get(id);
        return new ResponseEntity<>(mapper.convertToDto(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) throws ParseException {
            User user = mapper.convertToEntity(userDto);
            userService.save(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity("User was deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid user id : "+id);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) throws ParseException {
        if(!bookService.exist(userDto.getId())){
            throw new RecordNotFoundException("Invalid user id : " + userDto.getId());
        }
        User newUser = mapper.convertToEntity(userDto);
        userService.save(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public boolean isLibraryContainingBooks(Library library,Set<Book> books){
        return (libraryService.bookSet(library.getId()).containsAll(books));
    }

    @RequestMapping(value = "/addRent", method = RequestMethod.PUT)
    public ResponseEntity addRent(@Valid @RequestBody RentDto rentDto) {
        BookRent rent = mapper.convertToEntity(rentDto);
        if(!isLibraryContainingBooks(rent.getLibrary(),rent.getBooks())){
            return new ResponseEntity("Library doesn't contain this books", HttpStatus.OK);
        }
        BookRent bookRent;
        if(bookRentService.bookRent(rent.getLibrary(),rent.getUser())!=null){
            bookRent=bookRentService.bookRent(rent.getLibrary(),rent.getUser());
        }
        else {
            bookRent = new BookRent(new HashSet<>(),rent.getLibrary(),rent.getUser());
        }
        Set<Book> books = rent.getBooks();
        Library library = rent.getLibrary();
        User user = rent.getUser();
        for (Book book:
             books) {
            bookRent.addBook(book);
            library.removeBook(book);
            book.removeLibrary(library);
        }
            user.addBookRent(bookRent);
            userService.save(user);
            libraryService.save(library);
        return new ResponseEntity("Book was rented", HttpStatus.OK);
    }

    @RequestMapping(value = "/returnRent", method = RequestMethod.PUT)
    public ResponseEntity returnRent(@Valid @RequestBody RentDto rentDto) throws ParseException {
        try {
            BookRent bookRent = mapper.convertToEntity(rentDto);
            User user = bookRent.getUser();
            Set<Book> books = bookRent.getBooks();
            Library library = libraryService.get(rentDto.getLibraryId());
            BookRent newBookRent=bookRentService.bookRent(library,user);
            newBookRent.removeBooks(books);
            library.addBooks(books);
            bookRentService.save(newBookRent);
            libraryService.save(library);
            return new ResponseEntity("Book was returned to the library", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/toFile/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelUserReport(@PathVariable Long id) throws IOException {
        if(!userService.exist(id)){
            throw new RecordNotFoundException("Invalid user id : " + id);
        }
            User user = userService.get(id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-disposition", "attachment;filename=user.xlsx");
            UserGenerator userGenerator = new UserGenerator();
            ByteArrayInputStream in = userGenerator.toExcel(user);
            in.close();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(in));
    }
}
