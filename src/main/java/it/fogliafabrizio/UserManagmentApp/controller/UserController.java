package it.fogliafabrizio.UserManagmentApp.controller;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute
    private void userDetails(
            Model model,
            Principal principal
            ){
        String email = principal.getName();
        UserDtls user = userRepository.findByEmail(email);
        model.addAttribute("user", user);

    }

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

    @GetMapping("/changePass")
    public String loadChangePassword(){
        return "user/change_password";
    }

    @PostMapping("/updatePassword")
    public String changePassword(
            Principal principal,
            @RequestParam("oldPass") String oldPass,
            @RequestParam("newPass") String newPass,
            HttpSession session
    ){
        String email = principal.getName();
        UserDtls userLogin = userRepository.findByEmail(email);
        boolean matches = passwordEncoder.matches(oldPass, userLogin.getPassword());
        if(matches){
            //System.out.println("Correct");
            userLogin.setPassword(passwordEncoder.encode(newPass));
            UserDtls updateUser = userRepository.save(userLogin);
            if(updateUser != null){
                session.setAttribute("successMsg", "Password Change Success");
            } else {
                session.setAttribute("errorMsg", "Something Wrong on Server");
            }
        } else{
            session.setAttribute("errorMsg", "Old Password incorrect wright");
            //System.out.println("Wrong Password");
        }

        return "redirect:/user/changePass";
    }
}
