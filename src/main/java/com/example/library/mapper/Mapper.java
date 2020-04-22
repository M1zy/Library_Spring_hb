package com.example.library.mapper;

import com.example.library.domain.Book;
import com.example.library.domain.Library;
import com.example.library.domain.User;
import com.example.library.dto.BookDto;
import com.example.library.dto.LibraryDto;
import com.example.library.dto.UserDto;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.text.ParseException;
import java.util.stream.Collectors;

@Controller
public class Mapper {
    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public BookDto convertToDto(Book book)  {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookDto.setLibraryIds(book.getLibraries().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return bookDto;
    }

    public Book convertToEntity(BookDto bookDto) throws ParseException {
        Book book = modelMapper.map(bookDto, Book.class);
        if (bookDto.getId() != null&&bookDto.getLibraryIds()==null) {
            book.setLibraries(bookDto.getLibraryIds().stream().map(x->libraryService.get(x)).collect(Collectors.toSet()));
        }
        return book;
    }

    public LibraryDto convertToDto(Library library) {
        LibraryDto libraryDto = modelMapper.map(library, LibraryDto.class);
        libraryDto.setBookIds(library.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return libraryDto;
    }

    public Library convertToEntity(LibraryDto libraryDto) throws ParseException {
        Library library = modelMapper.map(libraryDto,Library.class);
        if (libraryDto.getId() != null&&libraryDto.getBookIds()==null) {
            library.setBooks(libraryDto.getBookIds().stream().map(x->bookService.get(x)).collect(Collectors.toSet()));
        }
        return library;
    }

    public UserDto convertToDto(User user){
        UserDto userDto = modelMapper.map(user,UserDto.class);
        return userDto;
    }

    public User convertToEntity(UserDto userDto) throws ParseException {
        User user = modelMapper.map(userDto,User.class);
        if (userDto.getId() != null) {
            if(userService.exist(user.getId())){
            User oldUser=userService.get(user.getId());
            user.setBookRentSet(oldUser.getBookRentSet());
            }
        }
        return user;
    }
}
