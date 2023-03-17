package com.project.eventBoard.registration.domain;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'APPLY'")
    private RegistrationStatus status;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Registration(User user, Board board){
        this.user = user;
        this.board = board;
        this.status = RegistrationStatus.APPLY;
    }

    public void setStatus(RegistrationStatus status){
        this.status = status;
    }
}
