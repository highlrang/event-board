package com.project.application.board.domain;

import com.project.application.registration.domain.Registration;
import com.project.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.engine.profile.Fetch;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board")
    private List<Registration> registrations = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private Boolean isBest;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private Integer recruitingCnt;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Board(User writer, BoardType boardType, String title, String content,
                 Integer recruitingCnt, LocalDateTime startDate, LocalDateTime endDate){
        this.writer = writer;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.recruitingCnt = recruitingCnt;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String title, String content, Integer recruitingCnt, LocalDateTime startDate, LocalDateTime endDate){
        this.title = title;
        this.content = content;
        this.recruitingCnt = recruitingCnt;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
