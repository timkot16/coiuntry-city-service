package com.andersen.lab.integration.service;

import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
class S3ServiceIntegrationTest extends IntegrationTestBase {

    private static final String fileName = UUID.randomUUID().toString();
    private final S3Service s3Service;

    @Test
    @Order(1)
    void whenPutContent_thenSuccess() {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("test", inputArray);

        assertDoesNotThrow(() -> s3Service.putContent(fileName, multipartFile));
    }

    @Test
    @Order(2)
    void whenGetContent_thenSuccess() throws IOException {
        byte[] content = s3Service.getContent(fileName);

        assertNotNull(content);
    }

}