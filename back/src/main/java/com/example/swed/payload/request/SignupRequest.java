package com.example.swed.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 40)
    private String lastName;


    @NotBlank
    @Size(max = 50)
    @Email
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 3, max = 40)
    private Date birthdate;

    @NotBlank
    @Size(min = 3, max = 40)
    private String address;

    @NotBlank
    @NotNull
    @Size(min = 6, max = 40)
    private String password;

}