package com.example.library.mapper;

import com.example.library.domain.Book;
import com.example.library.domain.BookRegistration;
import com.example.library.domain.Library;
import com.example.library.dto.BookDto;
import com.example.library.dto.LibraryDto;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Mapper {
    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ModelMapper modelMapper;

    public BookDto convertToDto(Book book)  {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookDto.setLibraryIds(book.getLibraries().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return bookDto;
    }

    public Book convertToEntity(BookDto bookDto) throws ParseException {
        Book book = modelMapper.map(bookDto, Book.class);
        if (bookDto.getId() != null) {
            book.setLibraries(bookDto.getLibraryIds().stream().map(x->registrationService.get(x)).collect(Collectors.toSet()));
        }
        return book;
    }

    public LibraryDto convertToDto(Library library) {
        LibraryDto libraryDto = modelMapper.map(library, LibraryDto.class);
        libraryDto.setBookIds(library.getBooks().stream().map(x->x.getId()).collect(Collectors.toSet()));
        return libraryDto;
    }

    public Library convertToEntity(LibraryDto libraryDto) throws ParseException {
        Library library = modelMapper.map(libraryDto, Library.class);
        if (libraryDto.getId() != null) {
            library.setBooks(libraryDto.getBookIds().stream().map(x->registrationService.get(x)).collect(Collectors.toSet()));
        }
        return library;
    }
}
