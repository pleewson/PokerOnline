package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class ScoreboardController {

    private final PlayerRepository playerRepository;

    public ScoreboardController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping("/scoreboard")
    public String getScoreboard(Model model) {
        List<Player> players = playerRepository.findAllByOrderByTrophiesDesc(); //TODO create DTO
        log.info("players from DB {} ", players);
        model.addAttribute("players", players);
        return "scoreboard";
    }
}
