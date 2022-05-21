package com.server.amething.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.domain.auth.config.AuthConfig;
import com.server.amething.domain.auth.dto.OauthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthConfig oauthConfig;
    private final ObjectMapper objectMapper;

    @GetMapping("auth/code")
    private OauthResponseDto getAuthorizationCode(@RequestParam String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", oauthConfig.getClientId());
        parameters.add("redirect_uri", oauthConfig.getRedirectUri());
        parameters.add("code", code);
        parameters.add("client_secret", oauthConfig.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpRequest,
                String.class
        );

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OauthResponseDto oauthResponseDto = null;

        try {
            oauthResponseDto = objectMapper.readValue(response.getBody(), OauthResponseDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthResponseDto;
    }
}
