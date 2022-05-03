package com.server.amething.domain.member;

import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "member")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_bio")
    private String bio;

    @Column(name = "member_profile_picture")
    private String profilePicture;

}

