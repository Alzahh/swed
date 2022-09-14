package com.example.swed.controlles;

import com.example.swed.models.DeactivatedUser;
import com.example.swed.models.User;
import com.example.swed.payload.request.UserDataUpdateRequest;
import com.example.swed.payload.response.AdminPageResponse;
import com.example.swed.payload.response.UserDataResponse;
import com.example.swed.repo.DeactivatedUserRepo;
import com.example.swed.repo.UserRepo;
import com.example.swed.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    LoggerUtil loggerUtil;

    @Autowired
    UserRepo userRepository;
    @Autowired
    DeactivatedUserRepo deactivatedUserRepo;

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserDetails(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            List<User> users = userRepository.findAll();
            List<DeactivatedUser> deactivatedUsers = deactivatedUserRepo.findAll();

            List<UserDataResponse> responses =
                    users.stream().map(UserDataResponse::new).collect(Collectors.toList());

            responses.addAll(deactivatedUsers.stream().map(UserDataResponse::new).collect(Collectors.toList()));


            loggerUtil.calculatedApiLog("UsersPage", "GET", "Page fetched successfully");


            return ResponseEntity.ok(new AdminPageResponse(responses));
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    @CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "")
    @RequestMapping(value = "/users/{id}/info", method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails(@PathVariable("id") Long id, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            User user = userRepository.findById(id).get();

            UserDataResponse response = new UserDataResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress(),
                    user.getBirthdate(),
                    true);

            loggerUtil.calculatedApiLog("Admin-UsersPage", "GET", response.toString());

            return ResponseEntity.ok(response);
        } else {
            loggerUtil.calculatedApiLog("Admin-UsersPage", "GET", "Bad Request error");
            return ResponseEntity.badRequest().build();
        }


    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
//    only for active users
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserDataUpdateRequest request,
                                               Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            String email = request.getEmail();
            User user = userRepository.findByEmail(email).get();

            String firstName = Optional.ofNullable(request.getFirstName()).orElse(user.getFirstName());
            String lastName = Optional.ofNullable(request.getLastName()).orElse(user.getLastName());
            Date birthdate = Optional.ofNullable(request.getBirthdate()).orElse(user.getBirthdate());
            String address = Optional.ofNullable(request.getAddress()).orElse(user.getAddress());

            userRepository.updateUserByEmail(firstName, lastName, birthdate, address, email);

            UserDataResponse response = new UserDataResponse(
                    user.getId(),
                    email,
                    firstName,
                    lastName,
                    address,
                    birthdate,
                    true);

            loggerUtil.calculatedApiLog("Admin-UsersPage", request.toString(), response.toString());

            return ResponseEntity.ok(response);
        } else {
            loggerUtil.calculatedApiLog("Admin-UsersPage", request.toString(), "Bad request");
            return ResponseEntity.badRequest().build();
        }


    }

}
