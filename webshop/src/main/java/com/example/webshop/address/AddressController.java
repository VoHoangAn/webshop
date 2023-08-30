package com.example.webshop.address;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressSerivce;
    private static Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    @GetMapping(path = "/getAll") //@Param(value = "userId") Long userId   |
    public ResponseEntity<List<Address>> getAll() {
        return ResponseEntity.ok(addressSerivce.getAll());
    }
    @DeleteMapping//(path = "/d")
    public String delete(@Param(value = "addressId") Long addressId) {
        addressSerivce.remove(addressId);
        return "del";
    }
    @PostMapping//(path = "/p")
    public ResponseEntity<Address> create(@RequestBody Address address) {
        var addressEntity = addressSerivce.add(address);
        return ResponseEntity.ok(addressEntity);
    }
}
