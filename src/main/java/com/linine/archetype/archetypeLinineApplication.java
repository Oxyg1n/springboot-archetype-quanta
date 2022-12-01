package com.linine.archetype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.quanta.archetype.mapper") // 注册mapper(必须)
//@EnableAsync // 允许异步任务(可选)
@SpringBootApplication
public class archetypeLinineApplication {

    public static void main(String[] args) {
        SpringApplication.run(archetypeLinineApplication.class, args);
    }

}
