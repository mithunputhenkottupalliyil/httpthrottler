package com.http.concurrentrequestor;

import com.http.concurrentrequestor.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class CmdRunner implements CommandLineRunner {
    @Autowired
    Config config;
    @Override
    public void run(String... args) throws Exception {
        config.getServiceCount();
    }
}
