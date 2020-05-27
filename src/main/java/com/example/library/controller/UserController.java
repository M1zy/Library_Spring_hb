package com.example.library.controller;
import com.example.library.domain.*;
import com.example.library.dto.UserDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.*;
import com.example.library.util.UserGenerator;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Api(value="Users", description="Operations to users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public List<UserDto> list() {
        List<User> users = userService.listAll();
        return users.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        if(!userService.exist(id)){
            throw new RecordNotFoundException("Invalid user id : "+id);
        }
        User user = userService.get(id);
        return new ResponseEntity<>(mapper.convertToDto(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) throws ParseException {
            User user = mapper.convertToEntity(userDto);
            userService.save(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity("User was deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RecordNotFoundException("Invalid user id : "+id);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) throws ParseException {
        if(!bookService.exist(userDto.getId())){
            throw new RecordNotFoundException("Invalid user id : " + userDto.getId());
        }
        User newUser = mapper.convertToEntity(userDto);
        userService.save(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public boolean isLibraryContainingBooks(Library library,Set<Book> books){
        return (libraryService.bookSet(library.getId()).containsAll(books));
    }

    @RequestMapping(value = "/toFile/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelUserReport(@PathVariable Long id) throws IOException {
        if(!userService.exist(id)){
            throw new RecordNotFoundException("Invalid user id : " + id);
        }
            User user = userService.get(id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-disposition", "attachment;filename=user.xlsx");
            UserGenerator userGenerator = new UserGenerator();
            ByteArrayInputStream in = userGenerator.toExcel(user);
            in.close();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(in));
    }
}
