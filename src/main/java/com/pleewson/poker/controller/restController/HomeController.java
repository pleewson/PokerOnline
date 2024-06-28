package com.pleewson.poker.controller.restController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/home")
    public String getHome(){
        return "home";
    }


    @GetMapping("/info")
    public String getInfo(){
        return "info";
    }

}
