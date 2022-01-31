package com.http.concurrentrequestor;

import com.http.concurrentrequestor.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Autowired
    Config config;
}
