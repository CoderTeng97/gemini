package com.mm.gemini;

import com.mm.gemini.base.boot.ApplicationStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mm.gemini")
public class GeminiApplication extends ApplicationStarter{
    public static void main(String[] args){
        start(args);
        SpringApplication.run(GeminiApplication.class,args);
    }
}
