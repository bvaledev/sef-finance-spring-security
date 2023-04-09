package br.dev.brendo.secfinance.dto;

import br.dev.brendo.secfinance.entity.RoleEntity;
import br.dev.brendo.secfinance.entity.UserEntity;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserViewDTO {
    private UUID userId;
    private String email;
    private List<RoleEntity> roles;

    public UserViewDTO(UserEntity user){
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }


}
