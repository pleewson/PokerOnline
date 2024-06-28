package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.entities.PlayerDetails;
import com.pleewson.poker.repository.PlayerDetailsRepository;
import com.pleewson.poker.repository.PlayerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@Slf4j
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
            if (BCrypt.checkpw(password, player.getPassword())) {
                session.setAttribute("player", player);
                return "redirect:home";
            }
        }
        log.info("incorrect password");
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

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            return "redirect:register";
        }

        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());
        player.setPassword(pw_hash);
        playerRepository.save(player);

        PlayerDetails playerDetails = new PlayerDetails();
        playerDetails.setFirstName(request.getParameter("firstName"));
        playerDetails.setLastName(request.getParameter("lastName"));
        playerDetails.setCountry(request.getParameter("country"));
        playerDetails.setCreated(LocalDateTime.now());
        String isAdultParam = request.getParameter(request.getParameter("isAdult"));
        boolean isAdult = (isAdultParam != null && isAdultParam.equals("on"));
        playerDetails.setAdult(isAdult);

        playerDetails.setPlayer(player);

        playerDetailsRepository.save(playerDetails);

        return "redirect:home";
    }


    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();

        return "redirect:welcome";
    }
}
