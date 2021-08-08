package com.overonix.nominatim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NominatimApplication {

    public static void main(String[] args) {
        SpringApplication.run(NominatimApplication.class, args);
    }

}
