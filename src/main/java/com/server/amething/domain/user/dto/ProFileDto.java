package com.server.amething.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProFileDto {
    private String userName;

    private String profilePicture;
}
