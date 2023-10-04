package com.project.webapp.service;

import com.project.webapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String[] getCredentialsFromRequest(HttpServletRequest request) {
        String header=request.getHeader("Authorization");
        if(header!=null && header.startsWith("Basic ")){
            try{
                String base64Credentials=header.substring(6);
                byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
                String decodedString = new String(decodedBytes);
                return decodedString.split(":");
            }catch (Exception e){
                System.out.println("Exception: "+e.getMessage());
            }
        }
        return null;
    }

//    public boolean validUser(String[] credentials) {
//        User user = userRepository.findByEmail(credentials[0]);
//        if(user!=null){
//            return passwordEncoder.matches(credentials[1],user.getPassword());
//        }
//        return false;
//    }

}
