package com.server.amething.global.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.global.exception.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 ExpiredJwtException 발생 ===================");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, "token의 유효기간이 만료 되었습니다.");
        } catch (SignatureException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 SignatureException 발생 ===================");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, "token의 secretKey가 변질 되었습니다.");
        } catch (MalformedJwtException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 MalformedJwtException 발생 ===================");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, "token이 위조 되었습니다.");
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 JwtException 발생 ===================");
            setErrorResponse(HttpStatus.FORBIDDEN, response, "요청에 사용된 token이 올바르지 않습니다.");
        } catch (Exception ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 Exception 발생 ===================");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, "알 수 없는 SERVER 에러가 발생하였습니다.");
        }
    }

    public void setErrorResponse(HttpStatus httpStatus, HttpServletResponse response, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(httpStatus.value())
                .error(httpStatus.name())
                .message(message)
                .build();
        String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseEntityToJson.toString());
    }

}
