package com.example.swed.controlles;

import com.example.swed.models.User;
import com.example.swed.payload.request.LoginRequest;
import com.example.swed.payload.request.SignupRequest;
import com.example.swed.payload.response.JwtResponse;
import com.example.swed.payload.response.MessageResponse;
import com.example.swed.repo.UserRepo;
import com.example.swed.security.jwt.JwtUtils;
import com.example.swed.security.services.UserDetailsImpl;
import com.example.swed.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    LoggerUtil loggerUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        JwtResponse response = new JwtResponse(jwt, userDetails.getEmail(), roles);
        loggerUtil.calculatedApiLog("Login", loginRequest.toString(), response.toString());


        return ResponseEntity.ok(response);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            String response = "Error: Email is already in use!";
            loggerUtil.calculatedApiLog("SignUp", signUpRequest.toString(), response);
            return ResponseEntity.badRequest().body(new MessageResponse(response));
        }

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getBirthdate(), signUpRequest.getAddress(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));


        userRepository.save(user);
        String response = "User registered successfully!";
        loggerUtil.calculatedApiLog("SignUp", signUpRequest.toString(), response);
        return ResponseEntity.ok(new MessageResponse(response));

    }


}