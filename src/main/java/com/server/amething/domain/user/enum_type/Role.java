package com.server.amething.domain.user.enum_type;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_MEMBER;

    @Override
    public String getAuthority() {
        return name();
    }
}
