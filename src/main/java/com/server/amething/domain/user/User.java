package com.server.amething.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.amething.domain.user.enum_type.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity @Table(name = "user")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_oauth_id", unique = true)
    private Long oauthId;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_profile_picture")
    private String profilePicture;

    @Column(name = "user_bio")
    private String bio;

    @Column(name = "user_refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = @JoinColumn(name = "member_id"))
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = this.roles.stream().map(Enum::name).collect(Collectors.toList());
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.oauthId.toString();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changeProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
