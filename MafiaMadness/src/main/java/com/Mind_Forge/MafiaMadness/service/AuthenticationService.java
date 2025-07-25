package com.Mind_Forge.MafiaMadness.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mind_Forge.MafiaMadness.dto.loginUserDto;
import com.Mind_Forge.MafiaMadness.dto.registerUserDto;
import com.Mind_Forge.MafiaMadness.dto.verifyUserDto;
import com.Mind_Forge.MafiaMadness.model.User;
import com.Mind_Forge.MafiaMadness.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signUp(registerUserDto input) {
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User authenticate(loginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified! Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(), input.getPassword()));
        return user;

    }

    public void verifyUser(verifyUserDto input) {
        Optional<User> optionalUsers = userRepository.findByEmail(input.getEmail());
        if (optionalUsers.isPresent()) {
            User user = optionalUsers.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Uh oh! This Verification code has expired :< ");
            }

            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("User not found :C ");
            }
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified!");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found D:");
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String VerificationCode = user.getVerificationCode();
        String htmlMsg = "<html>"
                + "<body style=\"background-color:#f3f4f6; font-family:'Segoe UI', sans-serif; margin:0; padding:0;\">"
                + "<div style=\"max-width:600px; margin:40px auto; background-color:#ffffff; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); padding:30px; text-align:center;\">"
                + "<div style=\"font-size:26px; color:#3b82f6; font-weight:600; margin-bottom:10px;\">Mafia Madness</div>"
                + "<div style=\"font-size:22px; font-weight:500; color:#111827;\">Verify Your Email</div>"
                + "<div style=\"font-size:16px; color:#4b5563; margin:20px 0;\">Hello! We're excited to have you. Please use the verification code below to complete your registration.</div>"
                + "<div style=\"font-size:32px; font-weight:bold; letter-spacing:4px; color:#10b981; background-color:#ecfdf5; padding:10px 20px; border-radius:8px; display:inline-block; margin-bottom:20px;\">"
                + VerificationCode + "</div>"
                + "<div style=\"font-size:16px; color:#4b5563; margin:20px 0;\">This code is valid for the next 15 minutes.</div>"
                + "<div style=\"font-size:14px; color:#9ca3af;\">If you did not request this code, feel free to ignore this message.<br />– Mind-Forge LLC</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMsg);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random rand = new Random();
        int code = rand.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
