package com.project.application.board.repository;

import static com.project.application.board.domain.QBoard.board;
import static com.project.application.user.domain.QUser.user;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        long totalCnt = jpaQueryFactory.select(board.count())
                .from(board)
                .where(board.boardType.eq(boardType))
                .fetchFirst();

        List<BoardResponseDto> results = jpaQueryFactory.select(
                    Projections.constructor(BoardResponseDto.class,
                            board.id,
                            board.boardType,
                            board.title,
                            board.content,
                            user.id,
                            user.name,
                            board.recruitingCnt,
                            board.isBest,
                            board.startDate,
                            board.endDate,
                            board.createdDate
                            )
                )
                .from(board)
                .innerJoin(board.writer, user)
                .where(board.boardType.eq(boardType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.isBest.asc(), board.createdDate.desc()) // asc true부터인지 확인
                .fetch();

         return new PageImpl<>(results, pageable, totalCnt);
    }

    @Transactional
    @Override
    public void updateAllToBest(List<Long> ids) {
        jpaQueryFactory.update(board)
                .set(board.isBest, true)
                .where(board.id.in(ids))
                .execute();

        em.flush();
        em.clear();
    }
}
