package com.server.amething.domain.auth.service.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.domain.auth.config.OauthConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class OauthServiceFacadeImpl implements OauthServiceFacade {

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return restTemplate;
    }

    @Override
    public HttpHeaders setHeader(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType);

        return headers;
    }

    @Override
    public HttpHeaders setHeader(String contentType, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType);
        headers.add("Authorization", "Bearer " + accessToken);

        return headers;
    }

    @Override
    public MultiValueMap<String, String> setParameter(String grantType, OauthConfig oauthConfig, String code) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", grantType);
        parameters.add("client_id", oauthConfig.getClientId());
        parameters.add("redirect_uri", oauthConfig.getRedirectUri());
        parameters.add("code", code);
        parameters.add("client_secret", oauthConfig.getClientSecret());

        return parameters;
    }

    @Override
    public HttpEntity createHttpRequest(HttpHeaders headers) {
        return new HttpEntity<>(headers);
    }

    @Override
    public HttpEntity createHttpRequest(HttpHeaders headers, MultiValueMap<String, String> parameters) {
        return new HttpEntity<>(parameters, headers);
    }

    @Override
    public ResponseEntity<String> callKakaoApi(RestTemplate restTemplate, String url, HttpEntity request, HttpMethod httpMethod) {
        ResponseEntity<String> exchange = restTemplate.exchange(
                url,
                httpMethod,
                request,
                String.class
        );

        return exchange;
    }

    @Override
    public <T> T getResponse(ObjectMapper objectMapper, ResponseEntity<String> response, Class<T> dtoClass) throws JsonProcessingException {
        Object dto = objectMapper.readValue(response.getBody(), dtoClass);
        return dtoClass.cast(dto);
    }
}
