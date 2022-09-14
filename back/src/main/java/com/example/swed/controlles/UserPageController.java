package com.example.swed.controlles;


import com.example.swed.models.DeactivatedUser;
import com.example.swed.models.User;
import com.example.swed.payload.request.CurrencyExchangeRequest;
import com.example.swed.payload.request.UserDataUpdateRequest;
import com.example.swed.payload.response.CurrencyExchangeResponse;
import com.example.swed.payload.response.MessageResponse;
import com.example.swed.payload.response.UserDataResponse;
import com.example.swed.repo.DeactivatedUserRepo;
import com.example.swed.repo.UserRepo;
import com.example.swed.security.services.UserDetailsImpl;
import com.example.swed.utils.LoggerUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserPageController {

    @Autowired
    UserRepo userRepository;

    @Autowired
    DeactivatedUserRepo deactivatedUserRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    LoggerUtil loggerUtil;

    @Value("${ApiAccessKey}")
    private String accessKey;


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(details.getEmail()).get();

        UserDataResponse response = new UserDataResponse(
                user.getId(),
                details.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getBirthdate(), true);

        loggerUtil.calculatedApiLog("Edit user", "GET", response.toString());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserDataUpdateRequest request, Authentication authentication) {
        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(details.getEmail()).get();

        String firstName = Optional.ofNullable(request.getFirstName()).orElse(user.getFirstName());
        String lastName = Optional.ofNullable(request.getLastName()).orElse(user.getLastName());
        Date birthdate = Optional.ofNullable(request.getBirthdate()).orElse(user.getBirthdate());
        String address = Optional.ofNullable(request.getAddress()).orElse(user.getAddress());
        String email = details.getEmail();

        if (request.getNewPassword() != null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getOldPassword()));

            userRepository.updateUserByEmail(firstName, lastName, birthdate, address,
                    encoder.encode(request.getNewPassword()), email);

        } else {
            userRepository.updateUserByEmail(firstName, lastName, birthdate, address, email);
        }

        UserDataResponse response = new UserDataResponse(
                user.getId(),
                email,
                firstName,
                lastName,
                address,
                birthdate,
                true);
        loggerUtil.calculatedApiLog("Edit user", request.toString(), response.toString());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public ResponseEntity<?> disableUser(Authentication authentication) {
        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        String email = details.getEmail();
        User user = userRepository.findByEmail(email).get();
        userRepository.delete(user);
        deactivatedUserRepo.save(new DeactivatedUser(user));

        String response = "User has been disabled: " + email;
        loggerUtil.calculatedApiLog("DisableUser", email, response);
        return ResponseEntity.ok(new MessageResponse(response));
    }

    @RequestMapping(value = "/currency", method = RequestMethod.POST)
    public ResponseEntity<?> currencyExchange(@Valid @RequestBody CurrencyExchangeRequest request) {
        String baseCurrency = request.getBaseCurrency();
        StringBuilder sb = new StringBuilder("https://v6.exchangerate-api.com/v6/");
        sb.append(accessKey);
        sb.append("/latest/");
        sb.append(baseCurrency);

        final String uri = sb.toString();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String json = restTemplate.getForObject(uri, String.class);
        try {
            Map map = mapper.readValue(json, Map.class);
            LinkedHashMap rates = (LinkedHashMap) map.get("conversion_rates");
            Double rate = (Double) rates.get(request.getNewCurrency());
            Double newAmount = request.getBaseAmount() * rate;

            loggerUtil.calculatedApiLog("DisableUser", request.toString(), newAmount.toString());
            return ResponseEntity.ok(new CurrencyExchangeResponse(newAmount));

        } catch (JsonProcessingException e) {
            return ResponseEntity.ok(new MessageResponse("Not Supported currency"));
        }

    }
}
