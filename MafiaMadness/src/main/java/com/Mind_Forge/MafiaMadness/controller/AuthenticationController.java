package com.Mind_Forge.MafiaMadness.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mind_Forge.MafiaMadness.dto.loginUserDto;
import com.Mind_Forge.MafiaMadness.dto.registerUserDto;
import com.Mind_Forge.MafiaMadness.dto.verifyUserDto;
import com.Mind_Forge.MafiaMadness.model.User;
import com.Mind_Forge.MafiaMadness.responses.LoginResponse;
import com.Mind_Forge.MafiaMadness.service.AuthenticationService;
import com.Mind_Forge.MafiaMadness.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody registerUserDto rUserDto) {
        User registeredUser = authenticationService.signUp(rUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody loginUserDto lUserDto) {
        User authenticatedUser = authenticationService.authenticate(lUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody verifyUserDto vUserDto) {
        try {
            authenticationService.verifyUser(vUserDto);
            return ResponseEntity.ok("Succesfully Verified! :3");
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code has been sent :D ");
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
