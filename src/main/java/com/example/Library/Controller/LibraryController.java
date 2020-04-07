package com.example.Library.Controller;

import com.example.Library.domain.Book;
import com.example.Library.domain.Library;
import com.example.Library.service.BookService;
import com.example.Library.service.LibraryService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/library")
@Api(value="libraries", description="Operations to libraries")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;


    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public Iterable list(Model model) {
        Iterable libraries = libraryService.listAll();
        return libraries;
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public Library showLibrary(@PathVariable Long id, Model model){
        Library library = libraryService.get(id);
        return library;
    }

    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public Iterable  filter(@PathVariable String keyword,
                            Model model) {
        Iterable libraries = libraryService.listByName(keyword);
        return libraries;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveLibrary(@RequestBody Library library) {
        libraryService.save(library);
        return new ResponseEntity("Library saved successfully", HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        libraryService.delete(id);
        return new ResponseEntity("Library deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProduct(@PathVariable Long id,@RequestBody Library library) {
        Library storedLibrary = libraryService.get(id);
        storedLibrary.setName(library.getName());
        storedLibrary.setAddress(library.getAddress());
        return new ResponseEntity("Library updated successfully", HttpStatus.OK);
    }

}
