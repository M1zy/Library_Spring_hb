package com.example.library.dto;

import com.example.library.domain.Book;
import com.example.library.domain.BookRegistration;
import com.example.library.domain.Library;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LibraryDto {
    private Long id;

    private String name;

    private String address;

    private Set<Long> bookIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(Set<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
