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

    @Override
    public TokenResponseDto getAccessToken(String code) throws JsonProcessingException {
        RestTemplate restTemplate = oauthServiceFacade.createRestTemplate();
        HttpHeaders headers = oauthServiceFacade.setHeader("application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> parameters = oauthServiceFacade.setParameter("authorization_code", oauthConfig, code);
        HttpEntity<MultiValueMap<String, String>> httpRequest = oauthServiceFacade.createHttpRequest(headers, parameters);
        ResponseEntity<String> responseEntity = oauthServiceFacade.callKakaoApi(restTemplate, "https://kauth.kakao.com/oauth/token", httpRequest, HttpMethod.POST);
        return oauthServiceFacade.getResponse(objectMapper, responseEntity, TokenResponseDto.class);
    }

    @Override
    public UserProfileResponseDto getUserProfile(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = oauthServiceFacade.createRestTemplate();
        HttpHeaders headers = oauthServiceFacade.setHeader("application/x-www-form-urlencoded;charset=utf-8", accessToken);
        HttpEntity<HttpHeaders> httpRequest = oauthServiceFacade.createHttpRequest(headers);
        ResponseEntity<String> responseEntity = oauthServiceFacade.callKakaoApi(restTemplate, "https://kapi.kakao.com/v2/user/me", httpRequest, HttpMethod.POST);
        return oauthServiceFacade.getResponse(objectMapper, responseEntity, UserProfileResponseDto.class);
    }
}
