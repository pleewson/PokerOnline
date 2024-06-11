package com.pleewson.poker.controller;

import com.pleewson.poker.entities.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        return player.getId().toString() + "   - player session id";
    }

}
