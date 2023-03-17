package com.project.eventBoard.board.domain;

import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @NotNull
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Registration> registrations = new ArrayList<>();

    @Column(columnDefinition = "tinyint default 0")
    private Boolean topFix;

    @Column(columnDefinition = "int default 0")
    private int views;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private int recruitingCnt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_file_id")
    private GenericFile file;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Board(BoardType boardType, String title, String content,
                 int recruitingCnt, LocalDate startDate, LocalDate endDate){
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.recruitingCnt = recruitingCnt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.topFix = false;
    }

    public void update(String title, String content, int recruitingCnt, LocalDate startDate, LocalDate endDate){
        this.title = title;
        this.content = content;
        this.recruitingCnt = recruitingCnt;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setWriter(User writer){
        this.writer = writer;
    }

    public Boolean isWriter(Long id){
        return writer.getId().equals(id);
    }

    public void setFile(GenericFile file){
        this.file = file;
        file.setBoard(this);
    }

    public void removeFile(){
        file.removeBoard();
        file = null;
    }

    public Boolean isClosed(){
        return recruitingCnt > 0 && recruitingCnt == registrations.size();
    }

    public void setTopFix(Boolean topFix){
        this.topFix = topFix;
    }

    public void increaseViews(){
        views += 1;
        if(views > 100 && !topFix) setTopFix(true);
    }

}
