package com.project.application.registration.repository;

import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.application.board.domain.QBoard.board;
import static com.project.application.registration.domain.QRegistration.registration;
import static com.project.application.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class RegistrationRepositoryCustomImpl implements RegistrationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RegistrationResponseDto> findAllByBoardId(Long boardId) {
        List<RegistrationResponseDto> result = jpaQueryFactory.select(Projections.constructor(
                        RegistrationResponseDto.class,
                        registration.id,
                        user.id.as("userId"),
                        user.name.as("userName"),
                        registration.status,
                        registration.createdDate
                ))
                .from(registration)
                .innerJoin(registration.user, user)
                .innerJoin(registration.board, board)
                .where(board.id.eq(boardId))
                .fetch();

        return result;
    }
}
