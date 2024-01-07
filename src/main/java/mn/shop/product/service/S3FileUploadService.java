package mn.shop.product.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.Calendar;

@Service
@Slf4j
public class S3FileUploadService {

    private final S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucketName;

    public S3FileUploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getKey() {

        Calendar cal = Calendar.getInstance();
        long uniqId = (cal.get(Calendar.YEAR) % 100) * 366 + cal.get(Calendar.DAY_OF_YEAR);
        uniqId = (((uniqId * 24 +
                    cal.get(Calendar.HOUR_OF_DAY)) * 60 +
                   cal.get(Calendar.MINUTE)) * 60 +
                  cal.get(Calendar.SECOND)) * 1000 +
                 cal.get(Calendar.MILLISECOND);
        return String.valueOf(uniqId);

    }

    public String uploadFile(File file) {

        String key = getKey();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(request, file.toPath());

        log.info("upload image from s3 fileName: {}", key);

        return "https://" + bucketName + ".s3.ap-southeast-1.amazonaws.com/" + key;
    }

    public String deleteFile(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(request);
        log.info("delete image from s3 fileName: {}", fileName);
        return "deleted";
    }

}