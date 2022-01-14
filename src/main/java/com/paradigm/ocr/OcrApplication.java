package com.paradigm.ocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author msl
 */
//@EnableCaching
//@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class OcrApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcrApplication.class, args);
    }

}
