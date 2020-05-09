package com.example.library;

import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class LibraryApplication implements CommandLineRunner {
	@Autowired
	BookService bookService;

	@Autowired
	LibraryService libraryService;

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(LibraryApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("EXECUTING : command line runner");
		if(args.length==0) {
			System.out.println("You have not provided any arguments!");
		}else {
			for (String arg:
					args) {
				switch(arg){
					case "allBooks" : {
						bookService.allBooksToConsole();
					}
					case "allLibraries":{
						libraryService.allLibrariesToConsole();
					}
					case "allUsers":{
						userService.allUsersToConsole();
					}
				}
			}
		}
	}
}
