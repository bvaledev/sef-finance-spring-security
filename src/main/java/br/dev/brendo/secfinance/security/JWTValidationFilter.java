package br.dev.brendo.secfinance.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTH_HEADER);
        if(authorizationHeader == null){
            chain.doFilter(request, response);
            return;
        }

        if(!authorizationHeader.startsWith(AUTH_TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(AUTH_TOKEN_PREFIX, "");
        UsernamePasswordAuthenticationToken authenticationToken = this.getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JWTAuthenticationFilter.TOKEN_JWT_SECRET))
                .build().verify(token);

        String userEmail = decodedJWT.getSubject();

        return new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
    }
}
