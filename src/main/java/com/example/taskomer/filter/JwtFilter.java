package com.example.taskomer.filter;


import com.example.taskomer.security.JwtAuthentication;
import com.example.taskomer.security.JwtProvider;
import com.example.taskomer.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

  private final JwtProvider jwtProvider;

  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain)
          throws IOException, ServletException {

    final String token = getTokenFromRequest((HttpServletRequest) servletRequest);
    if (token != null && jwtProvider.validateAccessToken(token)) {
      final Claims claims = jwtProvider.getAccessClaims(token);
      final JwtAuthentication jwtAuthentication = JwtUtil.generate(claims);
      jwtAuthentication.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String AUTHORIZATION_HEADER = "Authorization";
    final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

}
