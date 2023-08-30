package com.example.webshop.cart;

import com.example.webshop.dto.CartDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/cart")
@RequiredArgsConstructor
public class CartController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    @GetMapping(path = "/getAll") //@Param(value = "userId") Long userId   |
    public ResponseEntity<List<Cart>> getAll() {
        return ResponseEntity.ok(cartService.getAll());
    }

    @DeleteMapping (path = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        cartService.delete(id);
        return ResponseEntity.ok("del");
    }

    @PatchMapping(path = "/update")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> update(@RequestParam("cartsInfo") String cartsInfo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Cart> carts =mapper.readValue(cartsInfo, new TypeReference<>() {
        });

        cartService.update(carts);
        return ResponseEntity.ok("updated");
    }


    @PostMapping(path = "/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Cart>> add(@RequestParam("cartsInfo") String cartsInfo) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Cart> carts =mapper.readValue(cartsInfo, new TypeReference<>() {
            });

            return ResponseEntity.ok(cartService.add(carts));
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw e;
        }

    }
}
