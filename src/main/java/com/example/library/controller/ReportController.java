package com.example.library.controller;
import com.example.library.domain.*;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.ReportService;
import com.example.library.service.UserService;
import com.example.library.util.BookGenerator;
import com.example.library.util.LibraryGenerator;
import com.example.library.util.UserGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@Api(value="Reports")
@RequiredArgsConstructor
@Log4j2
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addUserReport/{id}", method = RequestMethod.GET)
    public ResponseEntity userReport(@PathVariable Long id) {
        if(!userService.exist(id)){
            throw new RecordNotFoundException("Invalid user id : " + id);
        }
            User user = userService.get(id);
            UserGenerator userGenerator = new UserGenerator();
            reportService.save(userGenerator.report(user));
            return new ResponseEntity("User was reported", HttpStatus.OK);
    }

    @RequestMapping(value = "/addBookReport/{id}", method = RequestMethod.GET)
    public ResponseEntity bookReport(@PathVariable Long id) {
        if(!bookService.exist(id)){
            throw new RecordNotFoundException("Invalid book id : " + id);
        }
            Book book = bookService.get(id);
            BookGenerator bookGenerator = new BookGenerator();
            reportService.save(bookGenerator.report(book));
            return new ResponseEntity("Book was reported", HttpStatus.OK);
    }

    @RequestMapping(value = "/addLibraryReport/{id}", method = RequestMethod.GET)
    public ResponseEntity libraryReport(@PathVariable Long id) {
        if(!libraryService.exist(id)){
            throw new RecordNotFoundException("Invalid library id : " + id);
        }
            Library library = libraryService.get(id);
            LibraryGenerator libraryGenerator = new LibraryGenerator();
            reportService.save(libraryGenerator.report(library));
            return new ResponseEntity("Library was reported", HttpStatus.OK);
    }
}
