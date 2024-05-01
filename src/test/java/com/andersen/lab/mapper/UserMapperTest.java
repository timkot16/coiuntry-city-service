package com.andersen.lab.mapper;

import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.entity.enums.Role;
import com.andersen.lab.model.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    PasswordEncoder passwordEncoder;

    private final RegisterRequest registerRequest = RegisterRequest.builder()
            .firstName("Bob")
            .lastName("Bobov")
            .email("bob@mail.com")
            .password("MySecretPaSS")
            .build();

    @Test
    void whenToEntity_thenSuccess() {
        String encodedPassword = "encodedMySecretPaSS";
        ReflectionTestUtils.setField(userMapper, "passwordEncoder", passwordEncoder);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn(encodedPassword);

        UserEntity entity = userMapper.toEntity(registerRequest);

        assertEquals(encodedPassword, entity.getPassword());
        assertEquals(Role.USER, entity.getRole());

        verify(passwordEncoder).encode(registerRequest.password());
    }

    @Test
    void whenToEntity_thenNullReturned() {
        assertNull(userMapper.toEntity(null));

        verifyNoInteractions(passwordEncoder);
    }


}