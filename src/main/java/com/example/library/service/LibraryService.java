package com.example.library.service;

import com.example.library.domain.Library;
import com.example.library.repos.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibraryService {
    @Autowired
    LibraryRepository libraryRepository;

    public void save(Library library){
        libraryRepository.save(library);
    }

    public List<Library> listAll() {
        return (List<Library>) libraryRepository.findAll();
    }

    public Library get(Long id) {
        return libraryRepository.findById(id).get();
    }

    public void delete(Long id) {
        libraryRepository.deleteById(id);
    }

    public List<Library> listByName(String name){
        return libraryRepository.findLibrariesByNameContains(name);
    }

}
