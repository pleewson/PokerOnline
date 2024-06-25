package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    private PlayerRepository playerRepository;

    public TestController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        return player.getId().toString() + "   - player session id";
    }

    @RequestMapping("game")
    public String test2() {
        return "game";
    }


    @RequestMapping("test3")
    @ResponseBody
    public String test3() {
        Player player = playerRepository.findById(11l).
                orElseThrow(() -> new EntityNotFoundException("entity not found"));
        return "nickname for player with id 11  -> " + player.getNickname();
    }
@RequestMapping("/test4")
    public String test4(){
        return "home";
    }
}
