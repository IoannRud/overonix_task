package com.overonix.nominatim.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

   @Bean
   public CacheManager cacheManager(Ticker ticker) {
       CaffeineCache getAddressByCoordinates = buildCache("getAddressByCoordinates",
               ticker,10, 200);
       SimpleCacheManager manager = new SimpleCacheManager();
       manager.setCaches(Collections.singletonList(getAddressByCoordinates));
       return manager;
   }

    private CaffeineCache buildCache(String name ,
                                     Ticker ticker ,
                                     int secondsToExpire ,
                                     int size){

        return new CaffeineCache(name , Caffeine.newBuilder()
                .expireAfterWrite(secondsToExpire , TimeUnit.SECONDS)
                .maximumSize(size)
                .ticker(ticker)
                .build());
    }

    @Bean
    public Ticker getCurrentTimeReaderTicker(){
        return Ticker.systemTicker();
    }

}
