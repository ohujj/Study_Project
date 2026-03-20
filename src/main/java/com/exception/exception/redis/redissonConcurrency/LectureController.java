package com.exception.exception.redis.redissonConcurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureApplyService service;

    @PostMapping("/apply/{id}")
    public String apply(@PathVariable int id) {
        return service.apply(id);
    }
}
