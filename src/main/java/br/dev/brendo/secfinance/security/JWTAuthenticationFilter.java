package br.dev.brendo.secfinance.security;


import br.dev.brendo.secfinance.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final int TOKEN_EXPIRES_IN = (60 * 1000) * 10; // 10 minutos
    public static final String TOKEN_JWT_SECRET = "hermanoteu-micalateia-isie-monaliza";

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

            var authentication = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword(),
                    new ArrayList<>());

            return this.authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Authentication failed",e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("isEnabled",userDetails.isEnabled())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES_IN))
                .sign(Algorithm.HMAC512(TOKEN_JWT_SECRET));

        response.getWriter().write("{\"access_token\": \"Bearer " + token + "\"}");
        response.getWriter().flush();
    }
}
