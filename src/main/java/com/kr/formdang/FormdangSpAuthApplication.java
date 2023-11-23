package com.kr.formdang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FormdangSpAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormdangSpAuthApplication.class, args);
    }

}
