package com.example.swed.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "deactivatedUsers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Data
@NoArgsConstructor
public class DeactivatedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;

    @Size(max = 20)
    private Date birthdate;

    @Size(max = 20)
    private String address;

    @Column(nullable = false)
    @Size(max = 50)
    @Email
    private String email;

    private Timestamp deactivationTime;

    public DeactivatedUser(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthdate = user.getBirthdate();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.deactivationTime = new Timestamp(System.currentTimeMillis());
    }
}
