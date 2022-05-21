package com.server.amething.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.domain.auth.config.OauthConfig;
import com.server.amething.domain.auth.dto.TokenResponseDto;
import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService{

    private final OauthConfig oauthConfig;
    private final ObjectMapper objectMapper;

    @Override
    public TokenResponseDto getAccessToken(String code) {
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

        TokenResponseDto oauthResponseDto = new TokenResponseDto();

        try {
            oauthResponseDto = objectMapper.readValue(response.getBody(), TokenResponseDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthResponseDto;
    }

    @Override
    public UserProfileResponseDto getUserProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();

        try {
            userProfileResponseDto = objectMapper.readValue(response.getBody(), UserProfileResponseDto.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }

        return userProfileResponseDto;
    }
}
