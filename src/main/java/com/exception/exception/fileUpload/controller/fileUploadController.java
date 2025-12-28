package com.exception.exception.fileUpload.controller;

import com.exception.exception.global.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.*;

@Controller
@Slf4j
public class fileUploadController {

    // 업로드 디렉토리 없으면 생성
    // 이건 빈 생성 시 실행 되는 생성자이다.
    // 경로가 없을 시 저장이 안 되기 때문에 그걸 방지하고자 추가한 메서드이다.(선택)
    public fileUploadController() {
        File uploadDir = new File(fileDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Value("${file.dir}")
    private String fileDir;


    @GetMapping("/fileUploadForm")
    public String fileUploadForm() {
        return "fileUploadForm";
    }

    @GetMapping("/spring/upload")
    public String newFile() {
        return "upload-form";
    }

    //실제로 해당 경로에 파일을 저장 시키는 api
    //getOriginalFilename : 업로드 한 파일 원본 이름
    //transferTo(파일 업로드)
    @PostMapping("/spring/upload")
    public String postUpload(@RequestParam String itemName,
                             @RequestParam MultipartFile file) throws IOException {
        System.out.println("fileDir = " + fileDir);

        if(!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("fullPath : {}", fullPath);
            file.transferTo(new File(fullPath));
        }

        return "upload-form";
    }

    @PostMapping("/requestFilesUpload")
    public ResponseEntity<?> requestFilesUpload(@RequestParam("files") List<MultipartFile> files) {
        ReturnDto returnDto = new ReturnDto();

        for(MultipartFile file : files) {

            String UUID = randomUUID().toString();
            String finalName = UUID + "_" + file.getOriginalFilename();

            try {
                file.transferTo(new File(fileDir + finalName));
            } catch (Exception e) {
                //위 로직을 메서드 단위로 옮겨서 transcational(rollbackFor = Exception.class)
            }

        }

        return ResponseEntity.ok(returnDto);
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
            throws MalformedURLException {
//        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();
        UrlResource resource = new UrlResource("file:" +
                fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName,
                StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}


}


