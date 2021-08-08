package com.overonix.nominatim.controller;

import com.overonix.nominatim.entity.Address;
import com.overonix.nominatim.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<Address> saveCoordinates(@RequestBody Address address) {
        return addressService.saveCoordinates(address);
    }

    /*@GetMapping("/street/{street}/city/{city}/country/{country}")
    public String getAllAddressesByCoordinates(@PathVariable String street,
                                               @PathVariable String city,
                                               @PathVariable String country) throws JsonProcessingException {
        return addressService.parseAddressesFromJSON(city, street, country);
    }*/

    @GetMapping("/all")
    public List<Object> getAllAddressesByRepositoryCoordinates() {
        return addressService.getAllAddressesByRepositoryCoordinates();
    }

    @GetMapping("/cache/{lat}/{lon}")
    public Object getAddressByCoordinates(@PathVariable String lat,
                                          @PathVariable String lon) {
        return addressService.getAddressByCoordinates(lat, lon);
    }
}
