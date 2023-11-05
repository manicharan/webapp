package com.project.webapp.service;

import com.project.webapp.entity.User;
import com.project.webapp.repository.AssignmentRepository;
import com.project.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserService.class);
    public User findByEmail(String email) {
        logger.debug("UserService.findByEmail method hit");
        return userRepository.findByEmail(email);
    }

    public void InitializeUsersFromCSV(){
        logger.debug("InitializeUsersFromCSV method entered");
        try (BufferedReader reader = new BufferedReader(new FileReader("/opt/users.csv"))) {
            String line;
            String header= reader.readLine();
            while ((line=reader.readLine()) != null) {
                String[] parts = line.split(",");
                String firstname=parts[0];
                String lastname=parts[1];
                String email = parts[2];
                String password = parts[3];

                // Check if the user already exists
                User existingUser = userRepository.findByEmail(email);
                if (existingUser == null) {
                    User newUser = new User();
                    newUser.setFirst_name(firstname);
                    newUser.setLast_name(lastname);
                    newUser.setEmail(email);
                    String hashedPassword =passwordEncoder.encode(password);
                    newUser.setPassword(hashedPassword);
                    logger.info("saving user with email {}",email);
                    userRepository.save(newUser);
                }else {
                    logger.debug("A user with email {} exists already",email);
                }
            }
        } catch (IOException e) {
            logger.error("encountered the following error while saving user {}",e.getMessage());
            System.out.println("Exception: "+e.getMessage());
        }
        logger.debug("InitializeUsersFromCSV method exited");
    }
}
