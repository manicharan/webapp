package com.project.webapp.service;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.repository.AssignmentRepository;
import com.project.webapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@Service
public class UserLoadService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AssignmentRepository assignmentRepository;
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void InitializeUsersFromCSV(){
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
                    userRepository.save(newUser);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
