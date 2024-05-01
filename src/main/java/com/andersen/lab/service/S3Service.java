package com.andersen.lab.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3Service {

    String bucket;
    S3Template s3Template;

    public S3Service(@Value("${spring.cloud.aws.s3.bucket}") String bucket, S3Template s3Template) {
        this.bucket = bucket;
        this.s3Template = s3Template;
    }

    @SneakyThrows
    @Transactional
    public void putContent(String fileName, MultipartFile multipartFile) {
        s3Template.upload(bucket, fileName, multipartFile.getInputStream());
    }

    public byte[] getContent(String fileName) {
        S3Resource resource;
        try {
            resource = s3Template.download(bucket, fileName);
            return Optional.of(resource.getContentAsByteArray())
                    .orElseThrow(NoSuchElementException::new);
        } catch (Exception exception) {
            throw new NoSuchElementException(exception);
        }
    }

}
