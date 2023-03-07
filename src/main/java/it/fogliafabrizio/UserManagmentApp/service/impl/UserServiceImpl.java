package it.fogliafabrizio.UserManagmentApp.service.impl;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import it.fogliafabrizio.UserManagmentApp.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public UserDtls createUser(UserDtls user, String url) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setAccountEnabled(false);
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString().substring(0,32);
        user.setVerificationCode(code);

        UserDtls userSaved = userRepo.save(user);

        sendVerificationMail(user, url);

        return userSaved;
    }

    @Override
    public Boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Boolean verifyUser(String code) {
        UserDtls userDtls = userRepo.findByVerificationCode(code);
        if(userDtls != null){
            userDtls.setAccountEnabled(true);
            userDtls.setVerificationCode(null);
            userRepo.save(userDtls);
            return true;
        }

        return false;
    }

    public void sendVerificationMail(UserDtls user, String url){
        String from = "info@rationence.eu";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Dear [[name]], <br> Please click the link below to verify your registration: <br> <h3><a href=\"[[URL]]\" target=\"_self\"> VERIFY </a></h3> Thank you, <br> Rationence EU.";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            content=content.replace("[[name]]", user.getFullName());

            String siteUrl = url+"/verify?code="+user.getVerificationCode();
            content=content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
