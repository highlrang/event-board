package com.project.eventBoard.user.service;

import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.domain.dto.UserRequestDto;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.project.eventBoard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.mapstruct.ObjectFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.eventBoard.common.StatusCode.USER_ALREADY_EXIST;
import static com.project.eventBoard.common.StatusCode.USER_NOT_FOUND;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Boolean existById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserResponseDto findById(String userId){
        User userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException());

        return new UserResponseDto(userEntity);
    }

    @Override
    public UserResponseDto findByEmail(String email){
        User userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException());

        return new UserResponseDto(userEntity);
    }

    @Transactional
    @Override
    public String join(UserRequestDto dto) {
        if(userRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(USER_ALREADY_EXIST.getCode(), USER_ALREADY_EXIST.getMessage());

        if(dto.getPassword() != null) dto.encodePassword(passwordEncoder.encode(dto.getPassword()));
        User result = userRepository.save(dto.toEntity());

        return result.getId();
    }

    @Transactional
    @Override
    public void updateRefreshToken(String id, String token){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException());
        user.setRefreshToken(token);
    }
}
