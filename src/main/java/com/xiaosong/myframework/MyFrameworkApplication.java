package com.xiaosong.myframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author msl
 */
//@EnableCaching
@SpringBootApplication
//@EnableTransactionManagement(proxyTargetClass = true)
public class MyFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFrameworkApplication.class, args);
    }

}
