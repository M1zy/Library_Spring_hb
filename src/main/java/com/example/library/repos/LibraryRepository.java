package com.example.library.repos;


import com.example.library.domain.Book;
import com.example.library.domain.Library;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibraryRepository extends CrudRepository<Library, Long> {
Library findLibraryById(Integer index);
List<Library> findLibrariesByNameContains(String name);
List<Library> findLibrariesByBooksContains(Book book);
}
