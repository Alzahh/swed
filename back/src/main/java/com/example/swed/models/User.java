package com.example.swed.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Size(max = 20)
    private String address;

    @Column(nullable = false)
    @Size(max = 50)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(max = 120)
    private String password;

    private String role;


    public User() {
    }


    public User(String firstName, String lastName, Date birthdate, String address, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = "USER";
    }

    public User(String firstName, String email, String password, String role) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}