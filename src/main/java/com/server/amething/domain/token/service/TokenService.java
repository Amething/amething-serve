package com.server.amething.domain.token.service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    String reissueAccessToken(HttpServletRequest request, Long oauthId);
}
