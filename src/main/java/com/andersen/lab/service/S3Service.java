package com.andersen.lab.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
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
    public void putContent(String fileName, MultipartFile multipartFile) {
        log.info("Put new city logo with fileName [{}] to bucket [{}]", fileName, bucket);
        s3Template.upload(bucket, fileName, multipartFile.getInputStream());
    }

    public byte[] getContent(String fileName) {
        log.info("Try to get city logo with fileName [{}] from bucket [{}]", fileName, bucket);
        S3Resource resource;
        try {
            resource = s3Template.download(bucket, fileName);
            return Optional.of(resource.getContentAsByteArray())
                    .orElseThrow(NoSuchElementException::new);
        } catch (Exception ex) {
            throw new NoSuchElementException(ex);
        }
    }

}
