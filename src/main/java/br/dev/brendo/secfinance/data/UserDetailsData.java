package br.dev.brendo.secfinance.data;

import br.dev.brendo.secfinance.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

public class UserDetailsData implements UserDetails {
    private final Optional<UserEntity> user;

    public UserDetailsData(Optional<UserEntity> user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.orElse(null).getRoles();
    }

    @Override
    public String getPassword() {
        return this.user.orElse(null).getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.orElse(null).getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
