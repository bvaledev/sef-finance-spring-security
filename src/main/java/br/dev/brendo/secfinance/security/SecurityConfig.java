package br.dev.brendo.secfinance.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalAuthentication
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;

    public SecurityConfig(AuthenticationConfiguration configuration) { this.configuration = configuration; }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.GET,"/users").hasAnyRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST,"/users").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .addFilter(new JWTValidationFilter(configuration.getAuthenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // The default filter works only on path "/login", the above config allows to use a custom path
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(configuration.getAuthenticationManager());
        jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/signin"));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, JWTValidationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
