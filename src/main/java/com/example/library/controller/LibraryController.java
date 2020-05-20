package com.example.library.controller;

import com.example.library.domain.Library;
import com.example.library.dto.LibraryDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.LibraryService;
import com.example.library.util.LibraryGenerator;
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
    public List<LibraryDto> list() {
        List<Library> libraries = libraryService.listAll();
        return libraries.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public ResponseEntity<LibraryDto> getLibrary(@PathVariable Long id){
        if(!libraryService.exist(id)){
            throw new RecordNotFoundException("Invalid library id : "+id);
        }
            Library library = libraryService.get(id);
            return new ResponseEntity<>(mapper.convertToDto(library),HttpStatus.OK);
    }

    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<LibraryDto>  filter(@PathVariable String keyword) {
        List<Library> libraries = libraryService.listByName(keyword);
        return libraries.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<LibraryDto> saveLibrary(@Valid @RequestBody LibraryDto libraryDto) throws ParseException {
        Library library = mapper.convertToEntity(libraryDto);
        libraryService.save(library);
        return new ResponseEntity<>(libraryDto, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            libraryService.delete(id);
            return new ResponseEntity("Library was deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid library id : "+id);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<LibraryDto> updateLibrary(@Valid @RequestBody LibraryDto libraryDto) throws ParseException {
        if(!libraryService.exist(libraryDto.getId())){
            throw new RecordNotFoundException("Invalid library id : " + libraryDto.getId());
        }
        Library library = mapper.convertToEntity(libraryDto);
        libraryService.save(library);
        return new ResponseEntity<>(libraryDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/quantity/{id}", method=RequestMethod.GET)
    public Integer getQuantityOfBooks(@PathVariable Long id) {
        if(!libraryService.exist(id)){
            throw new RecordNotFoundException("Invalid library id : " + id);
        }
        return libraryService.get(id).getBookRegistrations().size();
    }

    @RequestMapping(value = "/toFile/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelLibraryReport(@PathVariable Long id) throws IOException {
        if(!libraryService.exist(id)){
            throw new RecordNotFoundException("Invalid library id : " + id);
        }
            Library library = libraryService.get(id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-disposition", "attachment;filename=library.xlsx");
            LibraryGenerator libraryGenerator = new LibraryGenerator();
            ByteArrayInputStream in = libraryGenerator.toExcel(library);
            in.close();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(in));
    }
}
