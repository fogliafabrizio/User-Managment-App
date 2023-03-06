package it.fogliafabrizio.UserManagmentApp.controller;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

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
}
