package com.example.Library.repos;


import com.example.Library.domain.Library;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibraryRepository extends CrudRepository<Library, Long> {
Library findLibraryById(Integer index);
List<Library> findLibrariesByNameContains(String name);
}
