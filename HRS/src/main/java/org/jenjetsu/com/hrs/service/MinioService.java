package org.jenjetsu.com.hrs.service;

import java.io.InputStream;

public interface MinioService {

    public void putObject(String objectName, InputStream inputStream);
    public InputStream getObjectStream(String objectName);
}
