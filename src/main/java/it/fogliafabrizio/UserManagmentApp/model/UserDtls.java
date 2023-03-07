package it.fogliafabrizio.UserManagmentApp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserDtls {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(
            name = "full_name"
    )
    private String fullName;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "address"
    )
    private String address;

    @Column(
            name = "qualification"
    )
    private String qualification;

    @Column(
            name = "password"
    )
    private String password;

    @Column(
            name = "role"
    )
    private String role;

    @Column(
            name = "mobile_number"
    )
    private String mobileNumber;
}
