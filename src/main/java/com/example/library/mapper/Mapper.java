package com.example.library.mapper;

import com.example.library.domain.*;
import com.example.library.dto.*;
import com.example.library.service.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

@Log4j2
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

    @Autowired
    private AuthorService authorService;

    public BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookDto.setLibraryIds(book.getLibraries().stream().map(x -> x.getId()).collect(Collectors.toSet()));
        if(book.getAuthor()!=null) {
            bookDto.setAuthor_id(book.getAuthor().getId());
        }
        return bookDto;
    }

    public Book convertToEntity(BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        try {
            book.setLibraries(bookDto.getLibraryIds().stream().map(x -> libraryService.get(x)).collect(Collectors.toSet()));
            Author author = authorService.get(bookDto.getAuthor_id());
            author.addBook(book);
            book.setAuthor(author);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return book;
    }

    public LibraryDto convertToDto(Library library) {
        LibraryDto libraryDto = modelMapper.map(library, LibraryDto.class);
        libraryDto.setBookIds(library.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return libraryDto;
    }

    public Library convertToEntity(LibraryDto libraryDto) {
        Library library = modelMapper.map(libraryDto,Library.class);
        if (libraryDto.getId() != null && libraryDto.getBookIds() != null) {
            library.setBooks(libraryDto.getBookIds().stream().map(x->bookService.get(x)).collect(Collectors.toSet()));
        }
        return library;
    }

    public UserDto convertToDto(User user){
        UserDto userDto = modelMapper.map(user,UserDto.class);
        return userDto;
    }

    public User convertToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto,User.class);
            if(userService.exist(user.getId())){
            User oldUser=userService.get(user.getId());
            user.setBookRentSet(oldUser.getBookRentSet());
            }
        return user;
    }

    public BookRent convertToEntity(RentDto bookRentDto){
        BookRent bookRent = modelMapper.map(bookRentDto,BookRent.class);
        if (bookRent.getId() != null&&bookRentDto.getBookIds()!=null) {
            bookRent.setBooks(bookRentDto.getBookIds().stream().map(x->bookService.get(x)).collect(Collectors.toSet()));
            bookRent.setLibrary(libraryService.get(bookRent.getLibrary().getId()));
            bookRent.setUser(userService.get(bookRent.getUser().getId()));
        }
        return bookRent;
    }

    public AuthorDto convertToDto(Author author){
        AuthorDto authorDto = modelMapper.map(author,AuthorDto.class);
        try {
            authorDto.setBookIds(author.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()));
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return authorDto;
    }

    public Author convertToEntity(AuthorDto authorDto) {
        Author author = modelMapper.map(authorDto, Author.class);
        try {
            for (Long i :
                    authorDto.getBookIds()) {
                author.addBook(bookService.get(i));
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return author;
    }
}
