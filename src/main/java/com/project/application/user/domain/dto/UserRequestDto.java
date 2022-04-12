package com.project.application.user.domain.dto;

import com.project.application.user.domain.Role;
import com.project.application.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserRequestDto {

    private String userId;
    private String nickName;
    private String password;
    private Boolean isAdmin;

    public void encodePassword(String password){
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .nickName(nickName)
                .password(password)
                .role(isAdmin ? Role.ADMIN : Role.NORMAL)
                .build();
    }
}
