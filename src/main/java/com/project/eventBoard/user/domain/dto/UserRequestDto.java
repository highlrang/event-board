package com.project.eventBoard.user.domain.dto;

import com.project.eventBoard.user.domain.Role;
import com.project.eventBoard.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String id;
    @NotEmpty(message = "아이디는 필수값입니다.")
    private String email;
    private String nickName;
    @NotEmpty(message = "비밀번호는 필수값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "비밀번호는 8자 이상의 숫자와 영문 조합이어야합니다.")
    private String password;
    private Role role;

    private void createId(){
        this.id = UUID.randomUUID().toString();
    }

    public void encodePassword(String password){
        this.password = password;
    }

    public User toEntity(){
        if(id == null) createId();
        
        return User.builder()
                .id(id)
                .email(email)
                .nickName(nickName)
                .password(password)
                .role(role)
                .build();
    }
}
