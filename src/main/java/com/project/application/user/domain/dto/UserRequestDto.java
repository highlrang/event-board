package com.project.application.user.domain.dto;

import com.project.application.user.domain.Role;
import com.project.application.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotEmpty(message = "아이디는 필수값입니다.")
    private String userId;
    private String nickName;
    @NotEmpty(message = "비밀번호는 필수값입니다.")
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
