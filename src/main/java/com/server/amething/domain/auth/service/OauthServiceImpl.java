package com.server.amething.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.domain.auth.config.OauthConfig;
import com.server.amething.domain.auth.dto.TokenResponseDto;
import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.auth.service.facade.OauthServiceFacade;
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

    private final OauthServiceFacade oauthServiceFacade;
    private final OauthConfig oauthConfig;
    private final ObjectMapper objectMapper;

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private final String GRANT_TYPE = "authorization_code";
    private final String GET_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String GET_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public TokenResponseDto getAccessToken(String code) throws JsonProcessingException {
        RestTemplate restTemplate = oauthServiceFacade.createRestTemplate();
        HttpHeaders headers = oauthServiceFacade.setHeader(CONTENT_TYPE);
        MultiValueMap<String, String> parameters = oauthServiceFacade.setParameter(GRANT_TYPE, oauthConfig, code);
        HttpEntity<MultiValueMap<String, String>> httpRequest = oauthServiceFacade.createHttpRequest(headers, parameters);
        ResponseEntity<String> responseEntity = oauthServiceFacade.callKakaoApi(restTemplate, GET_TOKEN_URL, httpRequest, HttpMethod.POST);
        return oauthServiceFacade.getResponse(objectMapper, responseEntity, TokenResponseDto.class);
    }

    @Override
    public UserProfileResponseDto getUserProfile(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = oauthServiceFacade.createRestTemplate();
        HttpHeaders headers = oauthServiceFacade.setHeader(CONTENT_TYPE, accessToken);
        HttpEntity<HttpHeaders> httpRequest = oauthServiceFacade.createHttpRequest(headers);
        ResponseEntity<String> responseEntity = oauthServiceFacade.callKakaoApi(restTemplate, GET_PROFILE_URL, httpRequest, HttpMethod.POST);
        return oauthServiceFacade.getResponse(objectMapper, responseEntity, UserProfileResponseDto.class);
    }
}
