package com.example.library.repos;


import com.example.library.domain.Book;
import com.example.library.domain.Library;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibraryRepository extends org.springframework.data.repository.CrudRepository<Library, Long> {
Library findLibraryById(Integer index);
List<Library> findLibrariesByNameContains(String name);
List<Library> findLibrariesByBooksContains(Book book);
}
