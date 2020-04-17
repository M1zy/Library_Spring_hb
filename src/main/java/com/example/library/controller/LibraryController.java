package com.example.library.controller;

import com.example.library.domain.Library;
import com.example.library.dto.LibraryDto;
import com.example.library.mapper.Mapper;
import com.example.library.service.LibraryService;
import com.example.library.util.ExcelGenerator;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/library")
@Api(value="Libraries", description="Operations to libraries")
@RequiredArgsConstructor
@Log4j2
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @Autowired
    private Mapper mapper=new Mapper();

    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public List<LibraryDto> list(Model model) {
        List<Library> libraries = libraryService.listAll();
        return libraries.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public LibraryDto getLibrary(@PathVariable Long id, Model model){
        try {
            libraryService.get(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        Library library = libraryService.get(id);
        return mapper.convertToDto(library);
    }

    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<LibraryDto>  filter(@PathVariable String keyword,
                            Model model) {
        List<Library> libraries = libraryService.listByName(keyword);
        return libraries.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveLibrary(@RequestBody LibraryDto libraryDto) throws ParseException {
        try {
            Library library = mapper.convertToEntity(libraryDto);
            libraryService.save(library);
            return new ResponseEntity("Library saved successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            libraryService.delete(id);
            return new ResponseEntity("Library deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity updateLibrary(@RequestBody LibraryDto libraryDto) throws ParseException {
        try {
        Library storedLibrary = libraryService.get(libraryDto.getId());
        Library library=mapper.convertToEntity(libraryDto);
        storedLibrary.setName(library.getName());
        storedLibrary.setAddress(library.getAddress());
        libraryService.save(storedLibrary);
        return new ResponseEntity("Library updated successfully", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/quantity/{id}", method=RequestMethod.GET)
    public Integer getQuantityOfBooks(@PathVariable Long id,Model model) {
        if(!libraryService.exist(id)){
            log.error("This library doesn't exist");
        }
        return libraryService.get(id).getBooks().size();
    }

    @RequestMapping(value = "/toFile/{id}", method = RequestMethod.POST)
    public ResponseEntity excelLibraryReport(@PathVariable Long id) throws IOException {
        try {
            libraryService.get(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
        Library library = libraryService.get(id);
        ExcelGenerator.libraryToExcel(library);

        return new ResponseEntity("Excel was created",HttpStatus.OK);
    }

}
