package com.exception.exception.AWS_S3_File_Upload;

import com.exception.exception.AWS_S3_File_Upload.service.FileUploadS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadS3Service fileUploadS3Service;

    // 프로필 이미지 업로드
    @PostMapping("/profile/{id}")
    public ResponseEntity<String> uploadProfile(
            @RequestParam("file") MultipartFile file,
            @PathVariable String id
    ) {
        try {
            System.out.println("id = " + id);
            String fileUrl = fileUploadS3Service.uploadProfileImage(file, id);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 파일 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        fileUploadS3Service.delete(fileUrl);
        return ResponseEntity.ok("삭제 완료: " + fileUrl);
    }

    //TODO : IAM Role 방식, Presigned 방식으로 개편 가능
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @RequestParam String fileName) {

        ResponseInputStream<GetObjectResponse> s3stream =
                fileUploadS3Service.download(fileName);

        String contentType = s3stream.response().contentType();
        if(contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        InputStreamResource resource = new InputStreamResource(s3stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}