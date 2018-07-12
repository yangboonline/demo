package com.bert.spring.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @Value("${foo}")
    private String foo;

    @Value("${foo1}")
    private String foo1;

    @RequestMapping(value = "/hi")
    public String hi() {
        log.info("###foo:{}", foo);
        log.info("###foo1:{}", foo1);
        return foo;
    }

}
