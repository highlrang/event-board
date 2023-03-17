package com.project.eventBoard.user.service;

import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.domain.dto.UserRequestDto;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.project.eventBoard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.eventBoard.common.StatusCode.USER_ALREADY_EXIST;
import static com.project.eventBoard.common.StatusCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Boolean existUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public Long join(UserRequestDto dto) {
        if(userRepository.existsByUserId(dto.getUserId()))
            throw new CustomException(USER_ALREADY_EXIST.getCode(), USER_ALREADY_EXIST.getMessage());

        dto.encodePassword(passwordEncoder.encode(dto.getPassword()));
        User result = userRepository.save(dto.toEntity());

        return result.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
        return new UserResponseDto(user);
    }
}
