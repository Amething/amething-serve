package com.server.amething.domain.auth.service.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.domain.auth.config.OauthConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public interface OauthServiceFacade {
    RestTemplate createRestTemplate();
    HttpHeaders setHeader(String contentType);
    HttpHeaders setHeader(String contentType, String accessToken);
    MultiValueMap<String, String> setParameter(String grantType, OauthConfig oauthConfig, String code);
    HttpEntity createHttpRequest(HttpHeaders headers);
    HttpEntity createHttpRequest(HttpHeaders headers, MultiValueMap<String, String> parameters);
    ResponseEntity<String> callKakaoApi(RestTemplate restTemplate, String url, HttpEntity request, HttpMethod httpMethod);
    <T> T getResponse(ObjectMapper objectMapper, ResponseEntity<String> response, Class<T> dtoClass) throws JsonProcessingException;

}
