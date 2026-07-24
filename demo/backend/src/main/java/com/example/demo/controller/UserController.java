package com.example.demo.controller;
import com.example.demo.service.UserService;
import com.example.demo.config.AppSettings;
import com.example.demo.model.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AppSettings appSettings;

    @Value("${app.custom.welcome.message}")
    private String welcomeMessage;

    public UserController(UserService userService,AppSettings appSettings) {
        this.userService = userService;
        this.appSettings = appSettings;
    }

    @GetMapping("/message")
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    @GetMapping("/version")
    public String getVersion() {
        return appSettings.getVersion();
    }
    

    @GetMapping
    public List<User> getUsers() {

        return userService.getAllUsers();
    }
}
