package com.example.library.controller;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import com.example.library.dto.BookDto;
import com.example.library.dto.UserDto;
import com.example.library.mapper.Mapper;
import com.example.library.service.BookRentService;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Api(value="Users", description="Operations to users")
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
    public List<UserDto> list(Model model) {
        List<User> users = userService.listAll();
        return users.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public UserDto showUser(@PathVariable Long id){
        User user = userService.get(id);
        return mapper.convertToDto(user);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveUser(@RequestBody UserDto userDto) throws ParseException {
        User user = mapper.convertToEntity(userDto);
        userService.save(user);
        return new ResponseEntity("User saved successfully", HttpStatus.OK);
    }


    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity("User deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable Long id,@RequestBody UserDto userDto) throws ParseException {
        User user = userService.get(id);
        User newUser = mapper.convertToEntity(userDto);
        newUser.setId(id);
        newUser.setBookRentSet(user.getBookRentSet());
        userService.save(newUser);
        return new ResponseEntity("User updated successfully", HttpStatus.OK);
    }


    @RequestMapping(value = "/addRent/{idUser},{idBook},{idLibrary}", method = RequestMethod.PUT)
    public ResponseEntity addRent(@PathVariable Long idUser,@PathVariable Long idBook,
                                  @PathVariable Long idLibrary) throws ParseException {
        User user;
        Library library;
        Book book;
        if(userService.exist(idUser)){
            user=userService.get(idUser);
        }
        else return new ResponseEntity("No such user was found",HttpStatus.CONFLICT);
        if(libraryService.exist(idLibrary)){
            library=libraryService.get(idLibrary);
        }
        else return new ResponseEntity("No such library was found",HttpStatus.CONFLICT);
        if(bookService.exist(idBook)){
            book=bookService.get(idBook);
        }
        else return new ResponseEntity("No such book was found",HttpStatus.CONFLICT);
        if(!libraryService.listByBook(book).contains(library)){
           return new ResponseEntity("This library doesn't contain this book",HttpStatus.CONFLICT);
        }
        BookRent bookRent=new BookRent(book,library,user);
        user.addBookRent(bookRent);
        userService.save(user);
        library.removeBook(book);
        libraryService.save(library);
        book.removeLibrary(library);
        bookService.save(book);
        return new ResponseEntity("Book was rented", HttpStatus.OK);
    }

    @RequestMapping(value = "/returnRent/{idUser},{idBook},{idLibrary}", method = RequestMethod.PUT)
    public ResponseEntity returnRent(@PathVariable Long idUser,@PathVariable Long idBook,
                                  @PathVariable Long idLibrary) throws ParseException {
        User user;
        Library library;
        Book book;
        if(userService.exist(idUser)){
            user=userService.get(idUser);
        }
        else return new ResponseEntity("No such user was found",HttpStatus.CONFLICT);
        if(libraryService.exist(idLibrary)){
            library=libraryService.get(idLibrary);
        }
        else return new ResponseEntity("No such library was found",HttpStatus.CONFLICT);
        if(bookService.exist(idBook)){
            book=bookService.get(idBook);
        }
        else return new ResponseEntity("No such book was found",HttpStatus.CONFLICT);
        BookRent bookRent=bookRentService.bookRent(book,library,user);
        user.removeBookRent(bookRent);
        book.addLibrary(library);
        userService.save(user);
        bookService.save(book);
        return new ResponseEntity("Book was returned to the library", HttpStatus.OK);}


}
