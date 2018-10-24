package com.kvp.thinkjeonju.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:database.properties", ignoreResourceNotFound = true)
public class DatabaseConfig {
}
