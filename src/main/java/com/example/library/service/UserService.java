package com.example.library.service;
import com.example.library.domain.Book;
import com.example.library.domain.User;
import com.example.library.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean exist(long id){return userRepository.existsById(id);}

    public void allUsersToConsole(){
        System.out.println("USERS:");
        for (User user:
                listAll()) {
            System.out.print("User-");
            user.toConsole();
        }
    }

    public void commandToConsole(String[] args){
        for(int i=0;i<args.length;i++) {
            switch (args[i]) {
                case "all": {
                    allUsersToConsole();
                    break;
                }
            }
        }
    }
}
