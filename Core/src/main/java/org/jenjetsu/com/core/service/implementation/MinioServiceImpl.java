package org.jenjetsu.com.core.service.implementation;

import io.minio.*;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String putFile(String objectName, String bucketName, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                                 .object(objectName)
                                 .stream(inputStream, -1, 10485760)
                                 .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Impossible to save object in bucket.", e);
        }
    }

    @Override
    public Resource getFile(String objectName, String bucketName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName)
                                 .object(objectName)
                                 .build()
            );
            return new InputStreamResource(inputStream) {
                @Override
                public String getFilename() {
                    return objectName;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Impossible to get file from bucket.", e);
        }
    }

    public void deleteFile(String objectName, String bucketName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName)
                                    .object(objectName)
                                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Impossible to delete file from bucket.", e);
        }
    }

}
