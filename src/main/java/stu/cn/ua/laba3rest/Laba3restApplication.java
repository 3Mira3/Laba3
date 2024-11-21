package stu.cn.ua.laba3rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class Laba3restApplication {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    public static void main(String[] args) {
        SpringApplication.run(Laba3restApplication.class, args);
    }
}
