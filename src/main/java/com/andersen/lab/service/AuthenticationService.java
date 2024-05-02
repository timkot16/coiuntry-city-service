package com.andersen.lab.service;

import com.andersen.lab.config.security.JwtService;
import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.exception.RegistrationException;
import com.andersen.lab.mapper.UserMapper;
import com.andersen.lab.model.AuthenticationRequest;
import com.andersen.lab.model.AuthenticationResponse;
import com.andersen.lab.model.RegisterRequest;
import com.andersen.lab.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    JwtService jwtService;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    UserMapper userMapper;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Try to authenticate user with email [{}]", request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserEntity userEntity = userRepository.findByEmail(request.email())
                .orElseThrow(EntityNotFoundException::new);
        log.debug("User with email [{}] successfully authenticated", request.email());
        String jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    log.warn("User with email [{}] already exist.", request.email());
                    throw new RegistrationException("Registration failed. Please check your data and try again.");
                });
        log.debug("Try to register user with email [{}]", request.email());
        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity newUserEntity = userRepository.save(userEntity);
        log.debug("User with email [{}] successfully registered", newUserEntity.getEmail());
        String jwtToken = jwtService.generateToken(newUserEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
