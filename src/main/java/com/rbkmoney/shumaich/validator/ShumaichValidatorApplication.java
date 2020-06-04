package com.rbkmoney.shumaich.validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ShumaichValidatorApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShumaichValidatorApplication.class, args);
    }

}
