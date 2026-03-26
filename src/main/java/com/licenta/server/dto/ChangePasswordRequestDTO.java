package com.licenta.server.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChangePasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
}