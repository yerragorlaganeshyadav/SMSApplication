package com.smsapplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table (name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String mobileNumber;
    private String lastName;
    private String email;
    private boolean isProfileComplete;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addressList;


}
