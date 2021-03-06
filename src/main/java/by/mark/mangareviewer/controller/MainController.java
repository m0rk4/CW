package by.mark.mangareviewer.controller;

import by.mark.mangareviewer.domain.Views;
import by.mark.mangareviewer.domain.user.User;
import by.mark.mangareviewer.dto.ItemPageDto;
import by.mark.mangareviewer.service.AuthService;
import by.mark.mangareviewer.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    private final ObjectWriter profileWriter;
    private final AuthService authService;

    @Autowired
    public MainController(AuthService authService, ObjectMapper objectMapper, ItemService itemService) {
        this.authService = authService;
        objectMapper.setConfig(objectMapper.getSerializationConfig());
        this.profileWriter = objectMapper.writerWithView(Views.IdText.class);
    }

    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal User basicUser
    ) throws JsonProcessingException {
        User currentUser = authService.getCurrentUser(basicUser, oAuth2User);
        String serializedUser = profileWriter.writeValueAsString(currentUser);

        model.addAttribute("profile", serializedUser);
        model.addAttribute("isDevMode", "dev".equals(activeProfile));
        return "index";
    }

}
