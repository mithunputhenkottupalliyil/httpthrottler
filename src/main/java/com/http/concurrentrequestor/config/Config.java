package com.http.concurrentrequestor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import lombok.Data;

import java.util.Map;

@Component
@ConfigurationProperties
@PropertySources({@PropertySource("file:${property.root}/${env}/requestconfig.properties"),})
@Data
public class Config {
    private String serviceCount;
    private Map<ServiceName, ServiceAttributes> serviceConfig;
}
