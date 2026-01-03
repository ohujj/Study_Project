package com.exception.exception;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostConstructExClass {

    @Value("${cloud.aws.region.static}")
    private String region;

    public PostConstructExClass() {
        System.out.println("PostConstructExClass 생성자 실행");
        System.out.println("region = " + region);
        System.out.println("PostConstructExClass 생성자 종료");
    }

    @PostConstruct
    public void init() {
        System.out.println("init 메서드 실행");
        System.out.println("region = " + region);
        System.out.println("init 메서드 종료");
    }



//    @Autowired
//    private sampleClass sampleClass;

    //생성자 : ver1 (@Autowired)
    //public PostConstructExClass() {
//        sampleClass.sampleMethod();
//    }

}
