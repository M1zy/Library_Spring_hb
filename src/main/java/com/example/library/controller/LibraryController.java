package com.example.library.controller;

import com.example.library.domain.Library;
import com.example.library.dto.LibraryDto;
import com.example.library.mapper.Mapper;
import com.example.library.service.LibraryService;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/library")
@Api(value="Libraries", description="Operations to libraries")
@RequiredArgsConstructor
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
    public LibraryDto showLibrary(@PathVariable Long id, Model model){
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
        Library library = mapper.convertToEntity(libraryDto);
        libraryService.save(library);
        return new ResponseEntity("Library saved successfully", HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        libraryService.delete(id);
        return new ResponseEntity("Library deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateLibrary(@PathVariable Long id,@RequestBody LibraryDto libraryDto) throws ParseException {
        Library storedLibrary = libraryService.get(id);
        Library library=mapper.convertToEntity(libraryDto);
        storedLibrary.setName(library.getName());
        storedLibrary.setAddress(library.getAddress());
        libraryService.save(storedLibrary);
        return new ResponseEntity("Library updated successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/list_quantityOfBooks", method=RequestMethod.GET)
    public HashMap<LibraryDto,Integer> quantity(Model model) {
        HashMap<LibraryDto, Integer> librariesQuantity=new HashMap<>();
        List<LibraryDto> libraryDtos=list(model);
        for(int i=0;i<libraryDtos.size();i++){
            librariesQuantity.put(libraryDtos.get(i),libraryService.get(libraryDtos.get(i).getId()).getBooks().size());
        }
        return librariesQuantity;
    }

}
