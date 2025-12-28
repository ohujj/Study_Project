package com.exception.exception.controller;

import com.exception.exception.CommonModel;
import com.exception.exception.global.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class RequestController {

    @ResponseBody
    @GetMapping("/request/{id}")
    public ResponseEntity<?> request(@RequestBody CommonModel param)  {

        System.out.println(param.getUrl());
        System.out.println(param.getPort());

        if(param.getUrl().equals("127.0.0.0")) {
            throw new RuntimeException("Invalid Url Error");
        }

        ReturnDto returnDto = new ReturnDto();

        returnDto.setResultCode("CODE-0001");
        returnDto.setResultMessage("정상요청");

         return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

}
