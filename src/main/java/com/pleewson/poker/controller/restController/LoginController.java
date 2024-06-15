package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.entities.PlayerDetails;
import com.pleewson.poker.repository.PlayerDetailsRepository;
import com.pleewson.poker.repository.PlayerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class LoginController {
    PlayerRepository playerRepository;
    PlayerDetailsRepository playerDetailsRepository;

    LoginController(PlayerRepository playerRepository, PlayerDetailsRepository playerDetailsRepository) {
        this.playerRepository = playerRepository;
        this.playerDetailsRepository = playerDetailsRepository;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String loginUser(HttpSession session, @RequestParam String email, @RequestParam String password) {
        Player player = playerRepository.findByEmail(email);
        if (player != null) {
            if (player.getPassword().equals(password)) {
                session.setAttribute("player", player);
                return "redirect:home";
            }
        }
        return "redirect:login";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(HttpServletRequest request) {
        Player player = new Player();
        player.setNickname(request.getParameter("nickname"));
        player.setEmail(request.getParameter("email"));
        player.setPassword(request.getParameter("password"));
        playerRepository.save(player);

        PlayerDetails playerDetails = new PlayerDetails();
        playerDetails.setFirstName(request.getParameter("firstName"));
        playerDetails.setLastName(request.getParameter("lastName"));
        playerDetails.setCity(request.getParameter("city"));
        playerDetails.setCountry(request.getParameter("country"));
        playerDetails.setCreated(LocalDateTime.now());
        playerDetails.setPlayer(player);


        playerDetailsRepository.save(playerDetails);

        return "redirect:home";
    }
}
