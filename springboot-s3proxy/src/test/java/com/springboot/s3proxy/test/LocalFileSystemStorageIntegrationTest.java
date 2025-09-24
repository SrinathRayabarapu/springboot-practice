package com.springboot.s3proxy.test;

import com.springboot.s3proxy.StorageProperties;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"local", "test"})
@EnableConfigurationProperties(StorageProperties.class)
class LocalFileSystemStorageIntegrationTest {
/*
    @Autowired
    private S3Client s3Client;

    @Autowired
    private StorageProperties storageProperties;

    @BeforeAll
    void setup() {
        File directory = new File(storageProperties.getLocalFileBaseDirectory());
        directory.mkdir();

        String bucketName = storageProperties.getBucketName();
        try {
            s3Client.createBucket(request -> request.bucket(bucketName));
        } catch (BucketAlreadyOwnedByYouException exception) {
            // do nothing
        }
    }

    @AfterAll
    void teardown() throws IOException {
        File directory = new File(storageProperties.getLocalFileBaseDirectory());
        FileUtils.forceDelete(directory);
    }

    @Test
    void whenFileUploaded_thenFileSavedInFileSystem() throws IOException {
        // Prepare test file to upload
        String key = RandomString.make(10) + ".txt";
        String fileContent = RandomString.make(50);
        MultipartFile fileToUpload = createTextFile(key, fileContent);

        // Save file to file system
        s3Client.putObject(request ->
                        request
                                .bucket(storageProperties.getBucketName())
                                .key(key)
                                .contentType(fileToUpload.getContentType()),
                RequestBody.fromBytes(fileToUpload.getBytes()));

        // Verify that the file is saved successfully by checking if it exists in the file system
        List<S3Object> savedObjects = s3Client.listObjects(request ->
                request.bucket(storageProperties.getBucketName())
        ).contents();

        assertThat(savedObjects).anyMatch(savedObject -> savedObject.key().equals(key));
    }

    private MultipartFile createTextFile(String fileName, String content) throws IOException {
        byte[] fileContentBytes = content.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        return new MockMultipartFile(fileName, fileName, "text/plain", inputStream);
    }
*/

}
