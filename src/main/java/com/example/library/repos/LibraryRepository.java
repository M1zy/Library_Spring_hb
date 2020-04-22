package com.example.library.repos;

import com.example.library.domain.Book;
import com.example.library.domain.Library;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibraryRepository extends CrudRepository<Library, Long> {
Library findLibraryById(Long index);
List<Library> findLibrariesByNameContains(String name);
List<Library> findLibrariesByBooksContains(Book book);
}
