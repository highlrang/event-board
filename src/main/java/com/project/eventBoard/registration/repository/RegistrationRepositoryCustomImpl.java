package com.project.eventBoard.registration.repository;

import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.registration.domain.RegistrationStatus;
import com.project.eventBoard.registration.domain.dto.RegistrationRequestDto;
import com.project.eventBoard.registration.domain.dto.RegistrationResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.project.eventBoard.board.domain.QBoard.board;
import static com.project.eventBoard.registration.domain.QRegistration.registration;
import static com.project.eventBoard.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class RegistrationRepositoryCustomImpl implements RegistrationRepositoryCustom{

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RegistrationResponseDto> findAllByBoardId(Long boardId) {
        List<RegistrationResponseDto> result = jpaQueryFactory.select(Projections.constructor(
                        RegistrationResponseDto.class,
                        registration.id,
                        user.id.as("userId"),
                        user.email.nullif(user.nickName).as("userName"),
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

    @Override
    public void saveAll(List<RegistrationRequestDto> dtos) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO registration(board_id, created_date, status, user_id)"
                + " VALUES(?, ?, ?, ?)",
                new BatchPreparedStatementSetter(){

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        RegistrationRequestDto dto = dtos.get(i);
                        ps.setLong(1, dto.getBoardId());
                        ps.setString(2, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        ps.setString(3, RegistrationStatus.APPLY.toString());
                        ps.setLong(4, dto.getUserId());
                    }

                    @Override
                    public int getBatchSize() {
                        return dtos.size();
                    }
                }
        );
    }
}
