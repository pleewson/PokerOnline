package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("/test")
    public String test(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        return player.getId().toString() + "   - player session id";
    }

    @RequestMapping("test2")
    public String test2(){
        return "game";
    }
}
