package com.example.library.repos;
import com.example.library.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
