package com.exception.exception.fileUpload.model;

import lombok.Data;

import java.util.List;

@Data
public class FileUploadModel {
    private Long itemId;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
