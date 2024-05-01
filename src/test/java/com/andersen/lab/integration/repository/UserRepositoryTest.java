package com.andersen.lab.integration.repository;

import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.entity.enums.Role;
import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void whenFindByEmail_thenSuccess() {
        Optional<UserEntity> user = userRepository.findByEmail("test@test.com");

        assertFalse(user.isEmpty());
    }

    @Test
    void whenFindByEmail_thenOptionalEmpty() {
        Optional<UserEntity> user = userRepository.findByEmail("test1@test.com");

        assertTrue(user.isEmpty());
    }

    @Test
    void whenSave_thenSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .firstName("Test1")
                .lastName("Testov1")
                .email("test1@test.com")
                .role(Role.USER)
                .password("pass")
                .build();
        UserEntity save = userRepository.save(userEntity);

        assertNotNull(save);
    }

    @Test
    void whenSave_thenDataIntegrityViolationExceptionThrown() {
        UserEntity userEntity = UserEntity.builder()
                .firstName("Test")
                .lastName("Testov")
                .email("test@test.com")
                .role(Role.USER)
                .password("pass")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userEntity));
    }

}