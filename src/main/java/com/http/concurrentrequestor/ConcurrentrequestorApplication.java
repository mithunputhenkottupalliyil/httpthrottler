package com.http.concurrentrequestor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.http.concurrentrequestor")
public class ConcurrentrequestorApplication extends CmdRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ConcurrentrequestorApplication.class).run(args);
	}

}
