package com.project.eventBoard.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity @Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDate createdDate;

    @Builder
    public User(String userId, String password, String nickName, Role role){
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.nickName = nickName;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }
}
