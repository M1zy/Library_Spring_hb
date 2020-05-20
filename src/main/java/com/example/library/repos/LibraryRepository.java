package com.example.library.repos;
import com.example.library.domain.Library;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibraryRepository extends CrudRepository<Library, Long> {
List<Library> findLibrariesByNameContains(String name);
}
