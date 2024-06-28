package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.repository.PlayerDetailsRepository;
import com.pleewson.poker.repository.PlayerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MyAccountController {
    PlayerRepository playerRepository;
    PlayerDetailsRepository playerDetailsRepository;

    public MyAccountController(PlayerRepository playerRepository, PlayerDetailsRepository playerDetailsRepository) {
        this.playerRepository = playerRepository;
        this.playerDetailsRepository = playerDetailsRepository;
    }


    @GetMapping("/my-account")
    public String getMyAccount(Model model, HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        int playerTrophies = playerRepository.findTrophiesById(player.getId());
        log.info("trophies amount -> {}", playerTrophies);
        model.addAttribute("playerTrophies", playerTrophies);
        return "myAccount/my-account";
    }


    @GetMapping("/details")
    public String getDetails(Model model, HttpSession session) {
        Player player = (Player) session.getAttribute("player"); //TODO create DTO
        model.addAttribute("player", player);

        return "myAccount/details";
    }

}
