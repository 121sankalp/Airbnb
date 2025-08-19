package com.sankalp.projects.airBnbApp.security;

import com.sankalp.projects.airBnbApp.dto.LoginDto;
import com.sankalp.projects.airBnbApp.dto.SignUpRequestDto;
import com.sankalp.projects.airBnbApp.dto.UserDto;
import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.entity.enums.Roles;
import com.sankalp.projects.airBnbApp.exception.ResourceNotFoundException;
import com.sankalp.projects.airBnbApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service

@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository ;

    private  final PasswordEncoder passwordEncoder ;

    private  final AuthenticationManager authenticationManager ;

    private  final  JwtService jwtService ;

    private  final  ModelMapper modelMapper ;



    public UserDto signUp(SignUpRequestDto signUpRequestDto)
    {
        User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null) ;
        if(user!=null)
        {
            throw new RuntimeException("User is already Present") ;
        }

          user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                  .roles(Set.of(Roles.GUEST))
                .name(signUpRequestDto.getName())
                .build();

        userRepository.save( user) ;

        return  modelMapper.map(user , UserDto.class) ;

    }

    public  String[] login(LoginDto loginDto)
    {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail() ,
                loginDto.getPassword()
        )) ;

        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
         User user = (User) authentication.getPrincipal();

      String[] arr = new  String[2] ;
      arr[0] = jwtService.generateToken(user) ;
      arr[1]= jwtService.generateRefreshToken(user) ;
      return  arr  ;

    }

    public  String refreshToken(String refreshToken)
    {

        Long id = jwtService.getUserIdFromToken(refreshToken) ;

        User user =  userRepository.findById(id) .orElseThrow
                (()->new ResourceNotFoundException("user not found with id : " +id));

        return jwtService.generateToken(user) ;




    }


}
