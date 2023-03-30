package com.linine.archetype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@MapperScan("com.quanta.archetype.mapper") // 注册mapper(必须)
//@EnableAsync // 允许异步任务(可选)
@EnableTransactionManagement
@SpringBootApplication
public class ArchetypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchetypeApplication.class, args);
    }


}
