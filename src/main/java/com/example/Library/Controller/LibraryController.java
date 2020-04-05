package com.example.Library.Controller;

import com.example.Library.domain.Library;
import com.example.Library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LibraryController {
    @Autowired
    private LibraryService libraryService;



    @RequestMapping("/libraries/")
    public String main(Map<String, Object> model) {
        Iterable<Library> libraries = libraryService.listAll();
        model.put("libraries", libraries);
        return "Library/main";
    }

    @RequestMapping("/libraries/search")
    public String filter(@RequestParam String keyword,
                         Map<String, Object> model) {
        Iterable<Library> libraries = libraryService.listByName(keyword);
        model.put("libraries", libraries);
        return "Library/main";
    }

   @RequestMapping("/libraries/new")
    public String newLibraryForm(Map<String, Object> model) {
        Library library = new Library();
        model.put("library", library);
        return "Library/new_library";}

    @RequestMapping(value = "/libraries/save", method = RequestMethod.POST)
    public String saveLibrary(@RequestParam String name, @RequestParam String address,
                           Map<String, Object> model) {
        Library library = new Library(name, address);
        libraryService.save(library);
        return "redirect:/libraries/";
    }

    @RequestMapping("/libraries/edit")
    public String editBookForm(@RequestParam Long id,
                               Map<String, Object> model) {
        Library library = libraryService.get(id);
        model.put("library", library);
        return "Library/edit_library";
    }

    @RequestMapping("/libraries/delete")
    public String deleteCustomerForm(@RequestParam long id) {
        libraryService.delete(id);
        return "redirect:/libraries/";
    }

    @RequestMapping(value = "/libraries/update", method = RequestMethod.POST)
    public String updateLibrary(@RequestParam Long id, @RequestParam String name,
                             @RequestParam String address,
                              Map<String, Object> model) {
        Library library = libraryService.get(id);
        library.setAddress(address);
        library.setName(name);
        libraryService.save(library);
        return "redirect:/libraries/";
    }

}
