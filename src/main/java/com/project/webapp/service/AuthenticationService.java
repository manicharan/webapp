package com.project.webapp.service;

import com.project.webapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    public String[] getCredentialsFromRequest(HttpServletRequest request) {
        logger.debug("gerCredentialsFromRequest method entered");
        String header=request.getHeader("Authorization");
        if(header!=null && header.startsWith("Basic ")){
            try{
                String base64Credentials=header.substring(6);
                byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
                String decodedString = new String(decodedBytes);
                logger.info("decoded the credentials from request");
                return decodedString.split(":");
            }catch (Exception e){
                logger.error("Encountered the following exception {} while decoding credentials from request",e.getMessage());
                System.out.println("Exception: "+e.getMessage());
            }
        }
        logger.debug("exiting getCredentialsFromRequest method");
        return null;
    }
}
