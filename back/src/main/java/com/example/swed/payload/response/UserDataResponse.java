package com.example.swed.payload.response;

import com.example.swed.models.DeactivatedUser;
import com.example.swed.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class UserDataResponse {

    private long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private Date birthdate;

    private Boolean isActive;


    public UserDataResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.birthdate = user.getBirthdate();
        this.isActive = true;
    }

    public UserDataResponse(DeactivatedUser user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.birthdate = user.getBirthdate();
        this.isActive = false;
    }

}
