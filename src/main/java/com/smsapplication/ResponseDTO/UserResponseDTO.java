package com.smsapplication.ResponseDTO;


import com.smsapplication.Entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String mobileNumber;
    private String lastName;
    private String email;
    private boolean isProfileComplete;
    private List<Address> addressList;

}
