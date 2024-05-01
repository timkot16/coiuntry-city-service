package com.andersen.lab.integration;


import com.andersen.lab.integration.annotation.IT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.UUID;

@IT
@Transactional
@AutoConfigureMockMvc
@Sql("classpath:sql/test-data.sql")
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres");
    private static final LocalStackContainer localStackContainer =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
                    .withServices(LocalStackContainer.Service.S3);
    private static final String BUCKET_NAME = UUID.randomUUID().toString();

    @BeforeAll
    public static void setUp() throws IOException, InterruptedException {
        postgresqlContainer.start();
        localStackContainer.start();
        localStackContainer.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.s3.bucket", () -> BUCKET_NAME);
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.cloud.aws.s3.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.cloud.aws.s3.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
    }

}
