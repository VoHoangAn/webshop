package com.example.webshop.product;

import com.example.webshop.cart.Cart;
import com.example.webshop.dto.ProductWithPageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    @GetMapping(path = "/getAll") //@Param(value = "userId") Long userId   |
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(path = "/get/{id}") //@Param(value = "userId") Long userId   |
    public ResponseEntity<Product> get(@PathVariable Long id) {

        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping(path = "/find") //@Param(value = "userId") Long userId   |
    public ResponseEntity<ProductWithPageDTO> find(@RequestParam int page, @RequestParam int size,
                                                   @RequestParam String filterCriteriaInfo, String sortInfo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> filterCriteria =mapper.readValue(filterCriteriaInfo,  new TypeReference<HashMap<String,String>>() {});
        Map<String,String> sort =mapper.readValue(sortInfo,  new TypeReference<HashMap<String,String>>() {});
        return ResponseEntity.ok(productService.find(page,size,filterCriteria,sort));
    }

    @DeleteMapping (path = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.remove(id);
        return ResponseEntity.ok("del");
    }
    @PatchMapping(path = "/update/{id}")
    public ResponseEntity<String> update(@RequestParam("productInfo") String productInfo,
                                         @RequestParam(value = "file") MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Product product =mapper.readValue(productInfo, Product.class);

        productService.update(product,file);
        return ResponseEntity.ok("updated");
    }


    @PostMapping(path = "/create")
    public ResponseEntity<Product> create(@RequestParam("productInfo") String productInfo,
                                       @RequestParam(value = "file")MultipartFile file) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Product product =mapper.readValue(productInfo, Product.class);

            return ResponseEntity.ok(productService.create(product,file));
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw e;
        }

    }
}
