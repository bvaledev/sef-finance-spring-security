package br.dev.brendo.secfinance.controller;

import br.dev.brendo.secfinance.dto.CreateUserDTO;
import br.dev.brendo.secfinance.entity.RoleEntity;
import br.dev.brendo.secfinance.entity.RoleName;
import br.dev.brendo.secfinance.entity.UserEntity;
import br.dev.brendo.secfinance.repository.RoleRepository;
import br.dev.brendo.secfinance.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<UserEntity>> index(@PageableDefault(page = 0, size = 20, sort = "email", direction = Sort.Direction.ASC) Pageable pagination) {
        Page<UserEntity> userPaged = this.userRepository.findAll(pagination);
        return ResponseEntity.ok(userPaged);
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(@RequestBody @Valid CreateUserDTO userDTO, UriComponentsBuilder uriBuilder) throws Exception {
        Optional<UserEntity> userExists = userRepository.findByEmail(userDTO.email());
        if(userExists.isPresent()){
            throw new Exception("User already exists");
        }
        RoleEntity newRole = new RoleEntity();
        newRole.setName("Administrador");
        newRole.setAuthority(RoleName.ROLE_ADMIN);
        newRole = this.roleRepository.save(newRole);

        UserEntity newUser = new UserEntity();
        newUser.setEmail(userDTO.email());
        newUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.password()));

        newUser.setRoles(Arrays.asList(newRole));

        newUser = this.userRepository.save(newUser);
        URI uriPath = uriBuilder.path("/users/" + newUser.getUserId()).build().toUri();
        return ResponseEntity.created(uriPath).body(newUser);
    }
}
