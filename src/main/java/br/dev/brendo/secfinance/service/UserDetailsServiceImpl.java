package br.dev.brendo.secfinance.service;

import br.dev.brendo.secfinance.data.UserDetailsData;
import br.dev.brendo.secfinance.entity.UserEntity;
import br.dev.brendo.secfinance.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<UserEntity> userFound = this.userRepository.findByEmail(userEmail);
        if(userFound.isEmpty()){
            throw new UsernameNotFoundException("User does not exists");
        }
        return new UserDetailsData(userFound);
    }
}
