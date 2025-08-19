package com.sankalp.projects.airBnbApp.controller;

import com.sankalp.projects.airBnbApp.dto.LoginDto;
import com.sankalp.projects.airBnbApp.dto.LoginResponseDto;
import com.sankalp.projects.airBnbApp.dto.SignUpRequestDto;
import com.sankalp.projects.airBnbApp.dto.UserDto;
import com.sankalp.projects.airBnbApp.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController

@RequestMapping("/auth")

@RequiredArgsConstructor


public class AuthController {

    private final AuthService authService ;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto > signUp(@RequestBody SignUpRequestDto signUpRequestDto )
    {
        return  new ResponseEntity<>(authService.signUp(signUpRequestDto), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")

    public  ResponseEntity<LoginResponseDto> login (@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse)
    {
         String  [] token = authService.login(loginDto) ;

         Cookie cookie = new Cookie("refreshToken",token[1]) ;

         cookie.setHttpOnly(true);

         httpServletResponse.addCookie(cookie);


         return ResponseEntity.ok( new LoginResponseDto(token[0])) ;

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto > refresh(HttpServletRequest httpServletRequest )
    {
       String refreshToken  = Arrays.stream(httpServletRequest.getCookies())

               . filter(cookie -> "refreshToken".equals(cookie.getName()))
               .findFirst()
               .map(Cookie::getValue)
               .orElseThrow(()->new AuthorizationServiceException("refresh token not found in the cookie")) ;


       String accessToken =authService.refreshToken(refreshToken) ;

       return  ResponseEntity.ok(new LoginResponseDto(accessToken)) ;



    }



}
