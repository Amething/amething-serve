package com.server.amething.domain.user;

import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "user")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String memberName;

    @Column(name = "user_bio")
    private String bio;

    @Column(name = "user_profile_picture")
    private String profilePicture;

}

