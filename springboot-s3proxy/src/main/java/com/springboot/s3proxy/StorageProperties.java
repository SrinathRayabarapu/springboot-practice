package com.springboot.s3proxy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.s3proxy.storage")
public class StorageProperties {

    private String identity;

    private String credential;

    private String region;

    private String bucketName;

    private String proxyEndpoint;

    private String localFileBaseDirectory;

}
