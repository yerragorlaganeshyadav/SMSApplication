package com.smsapplication.Mapper;

import com.smsapplication.Entity.User;
import com.smsapplication.RequestDTO.UserRequestDTO;
import com.smsapplication.ResponseDTO.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface UserMapper {
    User convertToEntity(UserRequestDTO userRequestDTO);
    UserResponseDTO convertToDTO(User userEntity);
}
