package com.project.webapp.service;

import com.project.webapp.entity.User;
import com.project.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        System.out.println("Loading user by username: " + username);
        if(user==null){
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), Collections.emptyList());
    }
}
