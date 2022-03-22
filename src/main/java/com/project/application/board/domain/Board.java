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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board")
    private List<Registration> registrations = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private Boolean isBest;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Board(String title, String content){
        this.title = title;
        this.content = content;
    }
}
