package it.fogliafabrizio.UserManagmentApp.service;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import org.springframework.stereotype.Service;

public interface UserService {

    public UserDtls createUser(UserDtls user, String url);

    public Boolean checkEmail(String email);

    public Boolean verifyUser(String code);
}
