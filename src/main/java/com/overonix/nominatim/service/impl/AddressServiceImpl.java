package com.overonix.nominatim.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.overonix.nominatim.entity.Address;
import com.overonix.nominatim.entity.AddressInfoStored;
import com.overonix.nominatim.entity.CoordinatesHolder;
import com.overonix.nominatim.repository.CoordinatesRepository;
import com.overonix.nominatim.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final CoordinatesRepository coordinatesRepository;

    @Override
    @Cacheable(cacheNames = "getAddressByCoordinates")
    public Object getAddressByCoordinates(String lat, String lon) {
        log.info("If it's not visible after sending get request, this means coordinate with lat {} and lon {} were loaded from cache", lat, lon);
        return coordinatesRepository.getAddressByCoordinates(lat, lon);
    }

    @Override
    public List<Object> getAllAddressesByRepositoryCoordinates() {
        List<Object> result = new ArrayList<>();
        List<CoordinatesHolder> coordinatesFromDb = coordinatesRepository.getCoordinates();
        for (CoordinatesHolder c : coordinatesFromDb) {
            final String uri = String.format("https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json&accept-language=en-US", c.getLat(), c.getLon());
            JsonNode jsonNode = restTemplate.getForObject(uri, JsonNode.class);
            Optional<JsonNode> optionalJsonNode = Optional.ofNullable(jsonNode);
            if (optionalJsonNode.isPresent()) {
                result.add(jsonNode.get("address"));
            }
        }
        return result;
    }

    @Override
    public ResponseEntity<Address> saveCoordinates(@NonNull Address address) {
        if (address.getCountry().isEmpty() || address.getCity().isEmpty() || address.getStreet().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            final String uri = String.format("https://nominatim.openstreetmap.org/search?city=%s&street=%s&country=%s&format=json&addressdetails=1&accept-language=en-US",
                    address.getCity(), address.getStreet(), address.getCountry());
            AddressInfoStored[] readValue = restTemplate.getForObject(uri, AddressInfoStored[].class);
            if (readValue == null || readValue.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Arrays.stream(readValue)
                    .filter(addressInfoStored -> addressInfoStored.getLat() != null
                            && addressInfoStored.getLon() != null)
                    .forEach(coordinatesRepository::saveCoordinates);
        } catch (Exception e) {
            log.error("Error while parsing json.", e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

   /* @Override
    public String parseAddressesFromJSON(String city, String street, String country) {
        try {
            final String uri = String.format("https://nominatim.openstreetmap.org/search?city=%s&street=%s&country=%s&format=json&addressdetails=1&accept-language=en-US", city, street, country);
            AddressInfoStored[] readValue = restTemplate.getForObject(uri, AddressInfoStored[].class);
            if (readValue != null) {
                Arrays.stream(readValue)
                        .filter(addressInfo -> addressInfo.getLat() != null
                                && addressInfo.getLon() != null)
                        .forEach(coordinatesRepository::saveCoordinates);
            }
        } catch (Exception e) {
            log.error("Error while parsing json.", e);
            return "Error:"+e.getMessage();
        }
        return "saved";
    }*/
}
