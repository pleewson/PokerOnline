package com.pleewson.poker.controller.restController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHome(Model model){
        model.addAttribute("something", "this is coming form the controller");
        return "home";
    }

}
