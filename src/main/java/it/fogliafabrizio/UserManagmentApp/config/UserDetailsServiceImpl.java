package it.fogliafabrizio.UserManagmentApp.config;

import it.fogliafabrizio.UserManagmentApp.model.UserDtls;
import it.fogliafabrizio.UserManagmentApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDtls userDtls = userRepository.findByEmail(email);

        if(userDtls != null){
            return new CustomUserDetails(userDtls);
        }
        throw new UsernameNotFoundException("User not available!");
    }
}
