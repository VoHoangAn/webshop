/*
package com.example.webshop;

import com.example.webshop.product.Category;
import com.example.webshop.product.CategoryField;
import com.example.webshop.product.CategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping(path = "/tst")
@RequiredArgsConstructor
public class tst {
    private final static Logger LOGGER = LoggerFactory.getLogger(tst.class);
    private final CategoryService categoryService;
    @GetMapping(path = "/ga") //@Param(value = "userId") Long userId   |
    public ResponseEntity<List<Category>> ga() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping(path = "/g/{id}") //@Param(value = "userId") Long userId   |
    public ResponseEntity<Category> g(@PathVariable Long id) {

        return ResponseEntity.ok(categoryService.get(id));
    }

    @DeleteMapping (path = "/d/{id}")
    public ResponseEntity<String> d(@PathVariable Long id) {
        categoryService.remove(id);
        return ResponseEntity.ok("del");
    }
    @PatchMapping(path = "/u/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                           @RequestParam("categoryInfo") String categoryInfo,
                                           @RequestParam(value = "file")MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Category category=mapper.readValue(categoryInfo, Category.class);

        categoryService.update(id,category,file);
        return ResponseEntity.ok("updated");
    }


   @PostMapping(path = "/p")
   public ResponseEntity<Category> create(@RequestParam("categoryInfo") String categoryInfo,
                                          @RequestParam("categoryFields") String categoryFields,
                                          @RequestParam(value = "file")MultipartFile file) throws IOException {
       try {
           ObjectMapper mapper = new ObjectMapper();
           Category category=mapper.readValue(categoryInfo, Category.class);
           List<CategoryField> categoryFieldList = mapper.readValue(categoryFields,
                   new TypeReference<>() {});

           return ResponseEntity.ok(categoryService.create(category,categoryFieldList,file));
       } catch (Exception e) {
           LOGGER.error(e.toString());
           throw e;
       }

   }
}
*/
