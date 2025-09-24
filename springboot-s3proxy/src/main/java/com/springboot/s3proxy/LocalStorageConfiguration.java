package com.springboot.s3proxy;

import org.gaul.s3proxy.S3Proxy;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Properties;

@Configuration
@Profile("local | test")
@EnableConfigurationProperties(StorageProperties.class)
public class LocalStorageConfiguration {

    private final StorageProperties storageProperties;

    public LocalStorageConfiguration(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Bean
    public BlobStore blobStore() {
        Properties properties = new Properties();
        String fileSystemDir = storageProperties.getLocalFileBaseDirectory();
        properties.setProperty("jclouds.filesystem.basedir", fileSystemDir);

//        return ContextBuilder
//                .newBuilder("filesystem")
//                .overrides(properties)
//                .build(BlobStoreContext.class)
//                .getBlobStore();

        return ContextBuilder
                .newBuilder("filesystem")
                .credentials("identity", "credential")
                .overrides(properties)
                .build(BlobStoreContext.class).getBlobStore();
//        BlobStore blobStore = context.getBlobStore();
    }

    @Bean
    public S3Proxy s3Proxy(BlobStore blobStore) {
        return S3Proxy
                .builder()
//                .awsAuthentication(AuthenticationType.NONE, null, null)
                .blobStore(blobStore)
                .endpoint(URI.create(storageProperties.getProxyEndpoint()))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client
                .builder()
                .region(Region.US_EAST_1) // doesn't matter for local storage
                .endpointOverride(URI.create(storageProperties.getProxyEndpoint()))
                .build();
    }

}
