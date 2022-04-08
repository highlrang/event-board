package com.project.application.board.repository;

import static com.project.application.board.domain.QBoard.board;
import static com.project.application.registration.domain.QRegistration.registration;
import static com.project.application.user.domain.QUser.user;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        LocalDate today = LocalDate.now().minusDays(1L);
        today = LocalDate.of(2022, 4, 1);

        long totalCnt = jpaQueryFactory.select(board.count())
                .from(board)
                .where(board.boardType.eq(boardType)
                        .and(board.endDate.after(today)))
                .fetchFirst();

        // 시작
        List<OrderSpecifier<?>> orderSpecifier = new ArrayList<>();
        orderSpecifier.add(new OrderSpecifier<>(Order.DESC, board.topFix));

        // views date test하기
        pageable.getSort().forEach(o -> {
            Order direction = o.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> pathBuilder = new PathBuilder(Board.class, "board");
            orderSpecifier.add(new OrderSpecifier(direction, pathBuilder.get(o.getProperty())));
        });
        // 끝

        List<BoardResponseDto> results = jpaQueryFactory.select(
                    Projections.constructor(BoardResponseDto.class,
                            board.id,
                            board.boardType,
                            board.title,
                            user.id.as("writerId"),
                            user.name.as("writerName"),
                            board.recruitingCnt,
                            registration.count().as("registrationCnt"),
                            board.topFix,
                            board.views,
                            board.startDate,
                            board.endDate,
                            board.createdDate
                            )
                )
                .from(board)
                .innerJoin(board.writer, user)
                .leftJoin(board.registrations, registration)
                .where(board.boardType.eq(boardType)
                        .and(board.endDate.after(today)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .groupBy(board)
                .fetch();

         return new PageImpl<>(results, pageable, totalCnt);
    }

    @Override
    public void updateAllToBest(List<Long> ids) {
        jpaQueryFactory.update(board)
                .set(board.topFix, true)
                .where(board.id.in(ids))
                .execute();

        em.flush();
        em.clear();
    }
}
