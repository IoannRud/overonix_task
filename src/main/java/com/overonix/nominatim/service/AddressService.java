package com.overonix.nominatim.service;

import com.overonix.nominatim.entity.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AddressService {

    List<Object> getAllAddressesByRepositoryCoordinates();

    Object getAddressByCoordinates(String lat, String lon);

    ResponseEntity<Address> saveCoordinates(@NonNull Address address);
}
