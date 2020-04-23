package com.example.library.controller;
import com.example.library.domain.*;
import com.example.library.service.BookService;
import com.example.library.service.LibraryService;
import com.example.library.service.ReportService;
import com.example.library.service.UserService;
import com.example.library.util.BookGenerator;
import com.example.library.util.LibraryGenerator;
import com.example.library.util.UserGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@Api(value="Reports")
@RequiredArgsConstructor
@Log4j2
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserService userService;

    private File byteArrayInputStreamToFile(ByteArrayInputStream byteArrayInputStream,
                                            String filename) throws IOException {
        File newFile=new File(filename+".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        IOUtils.copy(byteArrayInputStream, fileOutputStream);
        fileOutputStream.close();
        return newFile;
    }

    @RequestMapping(value = "/addUserReport/{id}", method = RequestMethod.GET)
    public ResponseEntity userReport(@PathVariable Long id) {
        try {
            User user = userService.get(id);
            UserGenerator userGenerator = new UserGenerator();
            ByteArrayInputStream in = userGenerator.toExcel(user);
            String date = new Date().toString();
            String name = "User_"+user.getName()+"_report_"
                    +date.replaceAll(":","_");;
            Report report =new Report(name,
                    byteArrayInputStreamToFile(in,name),date);
            reportService.save(report);
            return new ResponseEntity("User was reported", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/addBookReport/{id}", method = RequestMethod.GET)
    public ResponseEntity bookReport(@PathVariable Long id) {
        try {
            Book book = bookService.get(id);
            BookGenerator bookGenerator = new BookGenerator();
            ByteArrayInputStream in = bookGenerator.toExcel(book);
            String date = new Date().toString();
            String name = "Book_"+book.getName()+"_report_"
                    +date.replaceAll(":","_");;
            Report report =new Report(name+"_Author_"
                    +book.getAuthor(),byteArrayInputStreamToFile(in,name),date);
            reportService.save(report);
            return new ResponseEntity("Book was reported", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/addLibraryReport/{id}", method = RequestMethod.GET)
    public ResponseEntity libraryReport(@PathVariable Long id) {
        try {
            Library library = libraryService.get(id);
            LibraryGenerator libraryGenerator = new LibraryGenerator();
            ByteArrayInputStream in = libraryGenerator.toExcel(library);
            String date = new Date().toString();
            String name = "Library_" + library.getName() + "_report_"
                    + date.replaceAll(":", "_");
            Report report = new Report(name,
                    byteArrayInputStreamToFile(in, name), date);
            reportService.save(report);
            return new ResponseEntity("Library was reported", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Scheduled(cron = "*/30 * * * *")
    @RequestMapping(value = "/allLibrariesReport", method = RequestMethod.GET)
    public ResponseEntity librariesReport () throws IOException {
        LibraryGenerator libraryGenerator = new LibraryGenerator();
        List<Library> libraries=libraryService.listAll();
        String date = new Date().toString();
        String name = "Libraries_"+"_report_"
                    + date.replaceAll(":", "_");
        Report report = new Report(name,byteArrayInputStreamToFile(LibraryGenerator.toExcel(libraries),name),date);
        reportService.save(report);
        return new ResponseEntity("Libraries were reported", HttpStatus.OK);
        }
}
