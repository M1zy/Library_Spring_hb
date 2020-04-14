package com.example.library.controller;

import com.example.library.domain.Book;
import com.example.library.domain.BookRent;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import com.example.library.dto.UserDto;
import com.example.library.mapper.Mapper;
import com.example.library.service.BookRentService;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Api(value="Users", description="Operations to users")
@RequiredArgsConstructor
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

    public  boolean isValidBookRent(Long idUser,Long idBook,Long idLibrary){
        if(!userService.exist(idUser)){
            return false;
        }
        if(!libraryService.exist(idLibrary)){
            return false;
        }
        if(!bookService.exist(idBook)){
            return false;
        }
        return true;
    }

    public boolean isLibraryContainingBook(Library library,Book book){
        if(libraryService.listByBook(book).contains(library))return true;
        return false;
    }


    @RequestMapping(value = "/addRent/{idUser},{idBook},{idLibrary}", method = RequestMethod.PUT)
    public ResponseEntity addRent(@PathVariable Long idUser,@PathVariable Long idBook,
                                  @PathVariable Long idLibrary) throws ParseException {
        if(!isValidBookRent(idUser,idBook,idLibrary)||!isLibraryContainingBook(
                libraryService.get(idLibrary),bookService.get(idBook)
        )){
            return new ResponseEntity("Wrong attributes",HttpStatus.CONFLICT);
        }
        User user=userService.get(idUser);
        Library library=libraryService.get(idLibrary);
        Book book=bookService.get(idBook);
        BookRent bookRent;
        if(bookRentService.bookRent(library,user)!=null){
            bookRent=bookRentService.bookRent(library,user);
        }
        else {
            bookRent = new BookRent(new HashSet<Book>(), library, user);
        }
            bookRent.addBook(book);
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
        if(!isValidBookRent(idUser,idBook,idLibrary)){
            return new ResponseEntity("Wrong attributes",HttpStatus.CONFLICT);
        }
        User user=userService.get(idUser);
        Library library=libraryService.get(idLibrary);
        Book book=bookService.get(idBook);

        BookRent bookRent=bookRentService.bookRent(book,library,user);
        bookRent.removeBook(book);
        book.addLibrary(library);
        bookRentService.save(bookRent);
        userService.save(user);
        bookService.save(book);
        return new ResponseEntity("Book was returned to the library", HttpStatus.OK);}


}
