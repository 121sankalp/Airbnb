package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.exception.ResourceNotFoundException;
import com.sankalp.projects.airBnbApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor

public class UserServiceImpl implements  UserService, UserDetailsService {

    private  final UserRepository userRepository ;
    @Override
    public User getUserById(Long id) {
       return  userRepository .findById(id).orElseThrow(()-> new ResourceNotFoundException("user does not exists")) ;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("user not found with email {}"+username)) ;
    }
}
