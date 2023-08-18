package org.jenjetsu.com.hrs.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    private final String minioUrl;
    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final Boolean minioSecure;

    public MinioConfiguration(@Value("${spring.minio.url}") String minioUrl,
                              @Value("${spring.minio.access-key}") String accessKey,
                              @Value("${spring.minio.secret-key}") String secretKey,
                              @Value("${spring.minio.bucket-name}") String bucketName,
                              @Value("${spring.minio.secure}") Boolean minioSecure) {
        this.minioUrl = minioUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.minioSecure = minioSecure;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl, 9000, minioSecure)
                .build();
    }
}
