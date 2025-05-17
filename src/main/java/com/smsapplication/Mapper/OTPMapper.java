package com.smsapplication.Mapper;

import com.smsapplication.Entity.OTPEntity;
import com.smsapplication.RequestDTO.OtpVerificationDTO;
import com.smsapplication.ResponseDTO.OtpVerificationResponseDTO;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface OTPMapper {

    OTPEntity convertToEntity(OtpVerificationDTO otpVerificationDTO);

    OtpVerificationResponseDTO convertToDTO(OTPEntity otpEntity);

}
