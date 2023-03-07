package it.fogliafabrizio.UserManagmentApp.controller;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import it.fogliafabrizio.UserManagmentApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    private void userDetails(
            Model model,
            Principal principal
    ){
        if(principal != null){
            String email = principal.getName();
            UserDtls user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
        }

    }

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/signin")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/createUser")
    public String createUser(
            @ModelAttribute UserDtls user,
            HttpSession session
    ){
        boolean f = userService.checkEmail(user.getEmail());

        if (f){
            //System.out.println("Email already exist!");
            session.setAttribute("msg", "Email already exist!");
        } else {
            UserDtls user_new = userService.createUser(user);
            if(user_new != null){
                //System.out.println("Registration successfully");

                session.setAttribute("msg", "Registration successfully");
            } else {
                //System.out.println("Server error");

                session.setAttribute("msg", "Server error");
            }
        }

        return "redirect:/register";
    }

    @GetMapping("/forgotpsw")
    public String loadForgotPassword(){
        return "forgot_password";
    }

    @GetMapping("/resetpsw")
    public String resetForgotPassword(){
        return "reset_password";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(
            @RequestParam String email,
            @RequestParam String mobileNumber,
            HttpSession session
    ){
        UserDtls user = userRepository.findByEmailAndMobileNumber(email, mobileNumber);
        if(user != null){
            return "reset_password";
        } else {
            session.setAttribute("msg", "Invalid mail or/and phoneNumber");
            return "forgot_password";
        }
    }
}
