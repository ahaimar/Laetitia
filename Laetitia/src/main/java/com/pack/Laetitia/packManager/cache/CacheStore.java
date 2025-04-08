package com.pack.Laetitia.packManager.cache;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CacheStore<K, V>{

    private final Cache<K, V> cache;

    // Default constructor for Spring
    public CacheStore() {
        this(60, TimeUnit.MINUTES);  // Default expiry: 60 minutes
    }

//@Value("${cache.expiry.duration:60}")
    public CacheStore(int expiryDuration, TimeUnit timeUnit) {

        cache = CacheBuilder.newBuilder()
                  .expireAfterWrite(expiryDuration, timeUnit)
                  .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                  .build();
    }
    
    public V get(@NotNull K key) {

        log.info("Retrieving from cache with key {} " , key.toString());
        return cache.getIfPresent(key);
    }
    
    public void put(@NotNull K key, @NotNull V value) {

        log.info("Storing Record in cache for key {} " , key.toString());
        cache.put(key, value);
    }

    public void evict(@NotNull K key) {
        log.info("Removing from cache with key {} " , key.toString());
        cache.invalidate(key);
    }
}