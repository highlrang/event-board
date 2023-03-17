package com.project.eventBoard.user.domain.dto;

import com.project.eventBoard.user.domain.Role;
import com.project.eventBoard.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotEmpty(message = "아이디는 필수값입니다.")
    private String userId;
    private String nickName;
    @NotEmpty(message = "비밀번호는 필수값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "비밀번호는 8자 이상의 숫자와 영문 조합이어야합니다.")
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
