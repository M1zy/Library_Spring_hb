package com.example.library.mapper;

import com.example.library.domain.*;
import com.example.library.dto.*;
import com.example.library.exception.RecordNotFoundException;
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
    private CartService cartService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthorService authorService;

    public BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookDto.setAuthorIds(book.getAuthors().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return bookDto;
    }

    public Book convertToEntity(BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        try {
            book.setAuthors(bookDto.getAuthorIds().stream().map(x -> authorService.get(x)).collect(Collectors.toSet()));
        }
        catch (Exception ex){
            throw new RecordNotFoundException("No such authors were found");
        }
        return book;
    }

    public LibraryDto convertToDto(Library library) {
        LibraryDto libraryDto = modelMapper.map(library, LibraryDto.class);
        return libraryDto;
    }

    public Library convertToEntity(LibraryDto libraryDto) {
        Library library = modelMapper.map(libraryDto,Library.class);
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
            user.setCartSet(oldUser.getCartSet());
            }
        return user;
    }

    public Cart convertToEntity(CartDto cartDto){
        Cart cart = modelMapper.map(cartDto, Cart.class);
        if (cart.getId() != null && cartDto.getRegistrationsIds() != null) {
            cart.setRegistrations(cartDto.getRegistrationsIds().stream().map(x->registrationService.get(x)).collect(Collectors.toSet()));
            cart.setUser(userService.get(cartDto.getUserId()));
        }
        return cart;
    }

    public CartDto convertToDto(Cart cart){
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
            cartDto.setRegistrationsIds(cart.getRegistrations().stream().map(x->x.getId()).collect(Collectors.toSet()));
            cartDto.setUserId(cart.getUser().getId());
        return cartDto;
    }

    public BookRegistration convertToEntity(RegistrationDto registrationDto){
        BookRegistration bookRegistration = modelMapper.map(registrationDto, BookRegistration.class);
        try{
            bookRegistration.setLibrary(libraryService.get(registrationDto.getLibraryId()));
            bookRegistration.setBook(bookService.get(registrationDto.getBookId()));
        }
        catch (Exception ex){
            throw new RecordNotFoundException("No such ids were found");
        }
        return bookRegistration;
    }

    public RegistrationDto convertToDto(BookRegistration bookRegistration){
        RegistrationDto registrationDto = modelMapper.map(bookRegistration,RegistrationDto.class);
        registrationDto.setBookId(bookRegistration.getBook().getId());
        registrationDto.setLibraryId(bookRegistration.getLibrary().getId());
        return registrationDto;
    }

    public AuthorDto convertToDto(Author author){
        AuthorDto authorDto = modelMapper.map(author,AuthorDto.class);
        authorDto.setBookIds(author.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()));
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
