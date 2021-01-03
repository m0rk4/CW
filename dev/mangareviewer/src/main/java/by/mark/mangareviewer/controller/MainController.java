package by.mark.mangareviewer.controller;

import by.mark.mangareviewer.domain.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("isDevMode", true);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "index";
    }

}
