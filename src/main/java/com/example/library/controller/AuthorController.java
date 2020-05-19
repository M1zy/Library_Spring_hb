package com.example.library.controller;

import com.example.library.domain.Author;
import com.example.library.dto.AuthorDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.AuthorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/author")
@Api(value="Authors", description="Operations to authors")
@RequiredArgsConstructor
@Log4j2
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public List<AuthorDto> list() {
        List<Author> authors = authorService.listAll();
        return authors.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public ResponseEntity<AuthorDto> getBook(@PathVariable Long id){
        if(!authorService.exist(id)){
            throw new RecordNotFoundException("Invalid author id : " + id);
        }
        Author author = authorService.get(id);
        return new ResponseEntity<>(mapper.convertToDto(author), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/{keyword}",method = RequestMethod.GET)
    public List<AuthorDto>  filter(@PathVariable String keyword) {
        List<Author> authors = authorService.listByName(keyword);
        return authors.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<AuthorDto> saveBook(@Valid @RequestBody AuthorDto authorDto) {
        Author author = mapper.convertToEntity(authorDto);
        authorService.save(author);
        return new ResponseEntity<>(authorDto, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            authorService.delete(id);
            return new ResponseEntity<>("Author was deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid author id : " + id);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<AuthorDto> updateBook(@Valid @RequestBody AuthorDto authorDto) {
        if(!authorService.exist(authorDto.getId())){
            throw new RecordNotFoundException("Invalid author id : " + authorDto.getId());
        }
        Author author = mapper.convertToEntity(authorDto);
        authorService.save(author);
        return new ResponseEntity<>(authorDto, HttpStatus.OK);
    }
}
