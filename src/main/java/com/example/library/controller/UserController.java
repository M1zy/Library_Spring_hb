package com.example.library.controller;
import com.example.library.domain.*;
import com.example.library.dto.OrderDto;
import com.example.library.dto.RentDto;
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
import java.util.HashSet;
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
    private BookRentService bookRentService;

    @Autowired
    private OrdersService ordersService;

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

    @RequestMapping(value = "/addRent", method = RequestMethod.PUT)
    public ResponseEntity<?> addRent(@Valid @RequestBody RentDto rentDto) {
        BookRent rent = mapper.convertToEntity(rentDto);
        if(!isLibraryContainingBooks(rent.getLibrary(),rent.getBooks())){
            return new ResponseEntity<>("Library doesn't contain this books", HttpStatus.OK);
        }
        BookRent bookRent;
        if(bookRentService.bookRent(rent.getLibrary(),rent.getUser()) != null){
            bookRent = bookRentService.bookRent(rent.getLibrary(),rent.getUser());
        }
        else {
            bookRent = new BookRent(new HashSet<>(),rent.getLibrary(),rent.getUser());
        }
        Set<Book> books = rent.getBooks();
        Library library = bookRent.getLibrary();
        User user = bookRent.getUser();
        for (Book book:
             books) {
            if(library.takeBook(book)){
            bookRent.addBook(book);
            }
        }
        bookRent.setLibrary(library);
        bookRent.setTotalPrice();
        user.addBookRent(bookRent);
        userService.save(user);
        libraryService.save(library);
        return new ResponseEntity<>("Book was rented", HttpStatus.OK);
    }

    @RequestMapping(value = "/returnRent", method = RequestMethod.PUT)
    public ResponseEntity<?> returnRent(@Valid @RequestBody RentDto rentDto) {
        try {
            BookRent bookRent = mapper.convertToEntity(rentDto);
            User user = bookRent.getUser();
            Set<Book> books = bookRent.getBooks();
            Library library = libraryService.get(rentDto.getLibraryId());
            BookRent newBookRent = bookRentService.bookRent(library,user);
            newBookRent.removeBooks(books);
            library.returnBooks(books);
            bookRentService.save(newBookRent);
            libraryService.save(library);
            return new ResponseEntity<>("Book was returned to the library", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
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

    @RequestMapping(value = "/addOrder", method = RequestMethod.PUT)
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderDto orderDto) {
        try {
            Orders orders = mapper.convertToEntity(orderDto);
            User user = orders.getUser();
            System.out.println(orders.getId()+" "+ orders.getUser().getId()+" "+ orders.getTotalPrice()+" "+ orders.getStatus()+" "+ orders.getDeliveryAddress()+" "+ orders.getBookRentSet().size());
            try {
                user.addOrder(orders);
            }
            catch (Exception ex){
                throw ex;
            }
            userService.save(user);
            return new ResponseEntity<>("Order was successfully added", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/listOrders", method= RequestMethod.GET)
    public List<OrderDto> listOrders() {
        List<Orders> orders = ordersService.listAll();
        return orders.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/changeStatusOrder/{id}_{status}", method = RequestMethod.PUT)
    public ResponseEntity<?> setOrderStatus(@PathVariable Long id, @PathVariable Status status) {
        try {
            Orders order = ordersService.get(id);
            order.setStatus(status);
            ordersService.save(order);
        }
        catch (RecordNotFoundException ex){
            throw new RecordNotFoundException("No such order number");
        }
        return new ResponseEntity<>("Order status was successfully changed", HttpStatus.OK);
    }
}
