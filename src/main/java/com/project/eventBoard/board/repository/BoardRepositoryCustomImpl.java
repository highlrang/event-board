package com.project.eventBoard.board.repository;

import static com.project.eventBoard.board.domain.QBoard.board;
import static com.project.eventBoard.file.domain.QGenericFile.genericFile;
import static com.project.eventBoard.registration.domain.QRegistration.registration;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.QBoard;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        /* where endDate 기준 */
        LocalDate criteriaDate = LocalDate.now().minusDays(1L);

        /* totalCnt */
        long totalCnt = getTotalCnt(boardType, criteriaDate);

        /* order by */
        List<OrderSpecifier<?>> orderSpecifier = getOrderSpecifiers(pageable);

        /* list 쿼리 */
        List<BoardResponseDto> results = jpaQueryFactory.select(
                    Projections.constructor(BoardResponseDto.class,
                            board.id,
                            board.title,
                            genericFile.id.as("fileId"),
                            genericFile.originalName.as("fileName"),
                            genericFile.path.concat("/").concat(genericFile.name).as("filePath"),
                            board.topFix,
                            board.views,
                            board.startDate,
                            board.endDate,
                            board.createdDate
                            )
                )
                .from(board)
                .leftJoin(board.registrations, registration)
                .leftJoin(board.file, genericFile)
                .where(board.boardType.eq(boardType)
                        .and(board.endDate.after(criteriaDate)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .groupBy(board)
                .fetch();

         return new PageImpl<>(results, pageable, totalCnt);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifier = new ArrayList<>();
        orderSpecifier.add(new OrderSpecifier<>(Order.DESC, board.topFix));

        pageable.getSort().forEach(o -> {
            Order direction = o.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> pathBuilder = new PathBuilder(Board.class, "board");
            orderSpecifier.add(new OrderSpecifier(direction, pathBuilder.get(o.getProperty())));
        });
        return orderSpecifier;
    }

    private long getTotalCnt(BoardType boardType, LocalDate criteriaDate) {
        long totalCnt = jpaQueryFactory.select(board.count())
                .from(board)
                .where(board.boardType.eq(boardType)
                        .and(board.endDate.after(criteriaDate)))
                .fetchFirst();
        return totalCnt;
    }

    @Override
    public List<BoardResponseDto> findLimitOrderBy(int limit, String field){
        return jpaQueryFactory.select(
                Projections.fields(BoardResponseDto.class,
                        board.id,
                        board.title,
                        board.views,
                        board.createdDate
                )
        )
                .from(board)
                .orderBy(Expressions.stringPath(board, field).desc())
                .where(board.endDate.after(LocalDate.now()))
                .limit(limit)
                .fetch();
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
