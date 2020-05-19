package com.example.library.service;

import com.example.library.domain.Author;
import com.example.library.repos.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public void save(Author author){
        authorRepository.save(author);
    }

    public List<Author> listAll() {
        return (List<Author>) authorRepository.findAll();
    }

    public Author get(Long id) {
        return authorRepository.findById(id).get();
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public boolean exist(Long id){
        return authorRepository.existsById(id);
    }

    public List<Author> listByName(String filter){
        return authorRepository.findAuthorsByNameContains(filter);
    }
}
