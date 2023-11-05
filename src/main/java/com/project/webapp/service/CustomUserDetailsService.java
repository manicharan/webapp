package com.project.webapp.service;

import com.project.webapp.entity.User;
import com.project.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        logger.debug("loadUserByUsername method entered");
        if(user==null){
            logger.error("User not found with username {}",username);
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
        logger.debug("exiting loadUserByUsername");
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), Collections.emptyList());
    }
}
