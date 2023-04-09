package br.dev.brendo.secfinance.controller;

import br.dev.brendo.secfinance.dto.SignInDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody @Valid SignInDTO signInDTO){
        var token = new UsernamePasswordAuthenticationToken(signInDTO.email(), signInDTO.password());
        authenticationManager.authenticate(token);
        return ResponseEntity.ok().build();
    }
}
