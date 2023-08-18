package org.jenjetsu.com.core.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface MinioService {

    public String putFile(String objectName, String bucketName, InputStream inputStream);
    public Resource getFile(String objectName, String bucketName);
    public void deleteFile(String objectName, String bucketname);
    //    TODO implements method
    //    public void moveFile(String objectName, String bucketName, String newObjectName, String destBucketName);
}
