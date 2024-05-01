package com.andersen.lab.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    S3Service s3Service;

    @Mock
    S3Template s3Template;
    @Mock
    S3Resource s3Resource;

    @BeforeEach
    void setUp() {
        s3Service = new S3Service("bucket-cityName", s3Template);
    }

    @Test
    void whenPutContent_thenSuccess() throws IOException {
        String filename = UUID.randomUUID().toString();
        MockMultipartFile multipartFile = new MockMultipartFile("test.png", "test".getBytes());
        when(s3Template.upload(eq("bucket-cityName"), eq(filename), any())).thenReturn(s3Resource);

        s3Service.putContent(filename, multipartFile);

        verify(s3Template).upload(eq("bucket-cityName"), eq(filename), any());
    }

    @Test
    void whenGetContent_thenSuccess() throws IOException {
        String filename = UUID.randomUUID().toString();
        byte[] fileData = "test data".getBytes();
        when(s3Resource.getContentAsByteArray()).thenReturn(fileData);
        when(s3Template.download("bucket-cityName", filename)).thenReturn(s3Resource);

        byte[] content = s3Service.getContent(filename);

        assertArrayEquals(fileData, content);
    }

}