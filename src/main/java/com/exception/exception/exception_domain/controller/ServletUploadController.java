package com.exception.exception.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLOutput;

@Slf4j
@Controller
@RequestMapping("/fileUpload")
public class ServletUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @PostMapping("/test")
    @ResponseBody
    public void propertiesValueTest() {
        System.out.println("fileDir : " + fileDir);
    }

}
