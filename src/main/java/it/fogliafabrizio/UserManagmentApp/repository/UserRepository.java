package it.fogliafabrizio.UserManagmentApp.repository;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    public boolean existsByEmail(String email);

    public UserDtls findByEmail(String email);

    public UserDtls findByEmailAndMobileNumber (String email, String mobileNumber);
}
