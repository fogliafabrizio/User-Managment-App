package it.fogliafabrizio.UserManagmentApp.controller;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import it.fogliafabrizio.UserManagmentApp.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
            HttpSession session,
            HttpServletRequest request
    ){

        //String url = request.getRequestURI().toString();
        //  http://localhost:8080/createUser
        //url=url.replace(request.getServletPath(), "");
        //  http://localhost:8080
        String url = "http://localhost:8080";
        boolean f = userService.checkEmail(user.getEmail());

        if (f){
            //System.out.println("Email already exist!");
            session.setAttribute("msg", "Email already exist!");
        } else {
            UserDtls user_new = userService.createUser(user, url);
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

    @GetMapping("/verify")
    public String verifyCode(
            @Param("code") String code
    ){
        if(userService.verifyUser(code)){
            return "verify_success";
        } else {
          return "verify_fail";
        }
    }

    @GetMapping("/forgotpsw")
    public String loadForgotPassword(){
        return "forgot_password";
    }

    @GetMapping("/resetpsw/{id}")
    public String resetForgotPassword(
            @PathVariable int id,
            Model model
    ){
        model.addAttribute("id", id);
        System.out.println(id);
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
            return "redirect:/resetpsw/" + user.getId();
        } else {
            session.setAttribute("msg", "Invalid mail or/and phoneNumber");
            return "forgot_password";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam String psw,
            @RequestParam String cpsw,
            @RequestParam Integer id,
            HttpSession session
    ){
        UserDtls user = userRepository.findById(id).get();
        String encryptPsw = passwordEncoder.encode(psw);
        user.setPassword(encryptPsw);

        UserDtls updateUser = userRepository.save(user);
        if (updateUser != null){
            session.setAttribute("msg", "Password Change!");
        } else {
            session.setAttribute("msg", "Some error appen");
        }

        return "redirect:/forgotpsw";
    }
}
