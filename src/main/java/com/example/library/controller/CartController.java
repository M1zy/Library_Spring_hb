package com.example.library.controller;

import com.example.library.domain.*;
import com.example.library.dto.CartDto;
import com.example.library.exception.RecordNotFoundException;
import com.example.library.mapper.Mapper;
import com.example.library.service.CartService;
import com.example.library.service.LibraryService;

import com.example.library.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@Api(value="Cart", description="Operations with cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/addCart/{type}", method = RequestMethod.PUT)
    public ResponseEntity<?> addRent(@Valid @RequestBody CartDto cartDto,  @PathVariable TypeOperation type) {
        Cart cart = mapper.convertToEntity(cartDto);
        for (BookRegistration b:
             cart.getRegistrations()) {
            System.out.println(b.getId());
        }
        cart.setTypeOperation(type.toString());
        cart.setTotalPrice();
        User user = cart.getUser();
        for (BookRegistration registration:
                cart.getRegistrations()) {
            Library library = registration.getLibrary();
            library.takeBook(registration.getBook());
            libraryService.save(registration.getLibrary());
        }
        user.addBookRent(cart);
        userService.save(user);
        return new ResponseEntity<>("Books were rented", HttpStatus.OK);
    }

    @RequestMapping(value = "/returnCart", method = RequestMethod.PUT)
    public ResponseEntity<?> returnRent(@Valid @RequestBody CartDto cartDto) {
        try {
            Cart oldCart = mapper.convertToEntity(cartDto);
            Cart cart = cartService.get(oldCart.getId());
            Set<BookRegistration> registrations = oldCart.getRegistrations();
            for (BookRegistration registration:
                 registrations) {
                Library library = registration.getLibrary();
                library.returnBook(registration.getBook());
                cart.removeRegistration(registration);
                libraryService.save(library);
            }
            cart.setTotalPrice();
            if(cart.getTotalPrice()==0) {
                cart.setStatus(Status.CANCELLED);
            }
            cartService.save(cart);
            return new ResponseEntity<>("Books were returned to the library", HttpStatus.OK);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/listCarts", method = RequestMethod.GET)
    public List<CartDto> listOrders() {
        List<Cart> carts = cartService.listAll();
        return carts.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/changeStatusCart/{id}_{status}", method = RequestMethod.PUT)
    public ResponseEntity<?> setCartStatus(@PathVariable Long id, @PathVariable Status status) {
        try {
            Cart cart = cartService.get(id);
            cart.setStatus(status);
            cartService.save(cart);
        }
        catch (RecordNotFoundException ex){
            throw new RecordNotFoundException("No such cart number");
        }
        return new ResponseEntity<>("Cart status was successfully changed", HttpStatus.OK);
    }
}
