package org.jenjetsu.com.hrs.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class MinioServiceImpl implements MinioService{

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioServiceImpl(MinioClient minioClient,
                            @Value("${spring.minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public void putObject(String objectName, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(inputStream, -1, 10485760).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("impossible to put object in minio");
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    throw new RuntimeException("impossible to close file stream");
                }
            }
        }
    }

    @Override
    public InputStream getObjectStream(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible to get file from minio");
        }
    }
}
