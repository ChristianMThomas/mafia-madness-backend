package com.Mind_Forge.MafiaMadness.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class verifyUserDto {
    private String email;
    private String verificationCode;
}
