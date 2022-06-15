package com.server.amething.global.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.amething.global.exception.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
public class ExceptionHandleFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 ExpiredJwtException 발생 ===================");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 JwtException 발생 ===================");
            setErrorResponse(HttpStatus.FORBIDDEN, response);
        } catch (Exception ex) {
            log.debug("================= [ ExceptionHandlerFilter ] 에서 Exception 발생 ===================");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response);
        }
    }

    public void setErrorResponse(HttpStatus httpStatus, HttpServletResponse response) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(httpStatus.value())
                .error(httpStatus.name())
                .build();
        String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseEntityToJson.toString());
    }

}
