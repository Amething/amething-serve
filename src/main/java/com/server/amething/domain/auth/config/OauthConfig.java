package com.server.amething.domain.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OauthConfig {

    @Value("${client-id}")
    private String clientId;

    @Value("${redirect-uri}")
    private String redirectUri;

    @Value("${client-secret}")
    private String clientSecret;
}
