package com.project.application.board.repository;

import static com.project.application.board.domain.QBoard.board;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        long totalCnt = jpaQueryFactory.select(board.count())
                .from(board)
                .where(board.boardType.eq(boardType))
                .fetchFirst();

        // 시작
        List<OrderSpecifier<?>> orderSpecifier = new ArrayList<>();
        orderSpecifier.add(new OrderSpecifier<>(Order.ASC, board.topFix));

        // createdDate desc를 sort 기본으로
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
                            board.content,
                            user.id,
                            user.name,
                            board.recruitingCnt,

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
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                // asc true부터인지 확인
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
