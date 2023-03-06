package it.fogliafabrizio.UserManagmentApp.service.impl;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import it.fogliafabrizio.UserManagmentApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDtls createUser(UserDtls user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }

    @Override
    public Boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
