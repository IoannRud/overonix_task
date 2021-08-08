package com.overonix.nominatim.entity;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String city;
    private String country;
}
