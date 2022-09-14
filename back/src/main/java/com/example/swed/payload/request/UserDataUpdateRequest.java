package com.example.swed.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserDataUpdateRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private Date birthdate;

    private String oldPassword;

    @Min(8)
    private String newPassword;

}
