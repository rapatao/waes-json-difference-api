package com.wearewaes.rapatao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class ApiApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiApp.class, args);
    }

    /**
     * Retrieve the name and the version of the running application.
     *
     * @return the application name and version
     */
    public static String getRuntimeAppName() {
        final Optional<String> version = Optional.ofNullable(ApiApp.class.getPackage().getImplementationVersion());

        return "JSON Differences API / " + version.orElse("development");
    }

}
