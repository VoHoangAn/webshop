package com.example.webshop.address;

import com.example.webshop.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepositoryImpl addressRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    public Address add(Address address) {
        User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        address.setUser(user);
        return addressRepository.save(address);
    }
    public List<Address> getAll() {
        var user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return addressRepository.findAllByUserId(user.getId());
    }
    public void remove(Long addressId) {
        User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new IllegalStateException("address not exist"));
        if(address.getUser().getId()== user.getId()) {
            addressRepository.deleteById(addressId);
        } else {
            throw new IllegalStateException("you dont have permission");
        }
    }
}
