package com.example.MotoBG.User;

import com.example.MotoBG.Motorcycle.*;
import com.example.MotoBG.ImageEncoder;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private UserRepository userRepository;
    private MotorcycleRepository motorcycleRepository;

    public UserController(UserService userService, UserRepository userRepository, MotorcycleRepository motorcycleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @GetMapping({"/", "/home"})
    public String getHome(Model model) {
        List<Motorcycle> motorcycles = (List<Motorcycle>) motorcycleRepository.findAll();
        model.addAttribute("motorcycles", motorcycles.size() > 3 ? motorcycles.subList(0, 3) : motorcycles);
        model.addAttribute("encoder", new ImageEncoder());
        return "home";
    }

    @GetMapping("/sign-in")
    public String getSignIn(Principal principal) {
        if (principal != null) {
            return "redirect:/access-denied";
        }
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "sign-up";
    }

    @PostMapping("/submit")
    public String submitUser(@Valid User user, BindingResult bindingResult, Model model) {
        return userService.submitUser(user, bindingResult, model);
    }

    @GetMapping("/profile")
    public String getShowProfile(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "error/accessDenied";
    }
}