package com.pleewson.poker.controller.restController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.entities.PlayerDetails;
import com.pleewson.poker.repository.PlayerDetailsRepository;
import com.pleewson.poker.repository.PlayerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @GetMapping("/change-password")
    public String changePassword() {
        return "myAccount/change-password";
    }


    //PATCH would be better
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        String playerPassword = player.getPassword();

        if (BCrypt.checkpw(oldPassword, playerPassword)) {
            log.info("wywala 1");
            if (newPassword.equals(confirmNewPassword)) {
                log.info("wywala 2");
                String pw_hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                player.setPassword(pw_hash);
                playerRepository.save(player);
                return "/home";
            }
        }
        log.info("wywala 0");

        return "myAccount/change-password";
    }


    @GetMapping("/change-details")
    public String changeDetails(HttpSession session, Model model) {
        Player player = (Player) session.getAttribute("player");
        model.addAttribute("player", player);
        return "myAccount/change-details";
    }


    @PostMapping("/change-details")
    public String changeDetails(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                @RequestParam String country,
                                HttpSession session) {
        Player player = (Player) session.getAttribute("player");

        if (player == null) {
            return "redirect:login";
        }

        PlayerDetails playerDetails = player.getPlayerDetails();

        playerDetails.setFirstName(firstName);
        playerDetails.setLastName(lastName);
        playerDetails.setCountry(country);

        player.setEmail(email);
        playerDetails.setPlayer(player);

        playerRepository.save(player);
        playerDetailsRepository.save(playerDetails);

        return "redirect:/my-account";
    }


}
