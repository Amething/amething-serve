package com.server.amething.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String userName;

    private String profilePicture;

    private String bio;
}
