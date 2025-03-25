package com.example.coffeeshop.config;

import com.example.coffeeshop.dto.DisplayOrderDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Config. */
@Configuration
public class CacheConfig {

    /** Bean. */
    @Bean
    public Map<String, List<DisplayOrderDto>> orderFilterCache() {
        return new LinkedHashMap<>(16, 0.75f, true) {
            private static final int MAX_ENTRIES = 10;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<DisplayOrderDto>> eldest) {
                return size() > MAX_ENTRIES;
            }
        };
    }
}
