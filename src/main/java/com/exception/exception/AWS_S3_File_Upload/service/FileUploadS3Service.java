package com.exception.exception.AWS_S3_File_Upload.service;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
public class FileUploadS3Service {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String PATH_PREFIX = "public/img/";

    private final String[] imageTypes = {
            "png", "jpg", "jpeg", "webp", "gif", "bmp", "tiff", "svg"
    };

    private S3Client s3Client;

    @PostConstruct
    private void init() {
        //객체 생성시 접속
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        //IAM Role 방식으로 바꿀 때 아래 한 줄 지우고
                        //DefaultCredentialsProvider.create() 추가
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey,secretKey)
                        )
                )
                .build();
    }

    //요청 시 받아온 파일이 이미지인지 확인 : 아니면 오류
    public void imgTypeTest(MultipartFile file) throws IOException {
        if(file.isEmpty()) throw new IOException("file is empty");

        String[] contentType = file.getContentType().split("/");
        if(!contentType[0].equals("image"))
            throw new IOException("이미지만 저장 가능");

        boolean match = Arrays.stream(imageTypes).anyMatch(type ->
                contentType[1].equals(type));

        if(!match)
            throw new IOException("확장자 검증 실패, 이미지만 가능");
    }

    public String uploadProfileImage(MultipartFile file, String id) throws IOException {
        //이미지인지 확인
        imgTypeTest(file);
        String ext = file.getContentType().split("/")[1];
        //파일이름 작성 : public/사용자명_profile.png
        String fileName = PATH_PREFIX + id + "_profile." + ext;
        //S3에 저장한 파일 객체 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
        //S3에 저장요청
        s3Client.putObject(putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                ));

        //https://deveckm-img-test.s3.ap-northeast-2.amazonaws.com/public/develckm_profile.webp
        //https://[버킷명].s3.[버킷 위치].amazonaws.com/[파일 이름 및 경로]

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucket,region,fileName);
        return fileUrl;

    }

    public void delete(String fileUrl) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileUrl)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.info("s3 파일 삭제 : {}", fileUrl);
    }

    public ResponseInputStream<GetObjectResponse> download(String fileName) {
        String key = PATH_PREFIX + fileName;

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try {
            return s3Client.getObject(request);
        } catch (NoSuchKeyException e) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        } catch (S3Exception e) {
            throw new RuntimeException("S3 다운로드 실패");
        }
    }

}
