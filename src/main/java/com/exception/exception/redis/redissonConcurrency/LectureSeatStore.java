package com.exception.exception.redis.redissonConcurrency;

import org.springframework.stereotype.Component;

@Component
public class LectureSeatStore {

    private static final int MAX_SEAT = 50;
    private static int currentSeat = 0;

    private static int totalInwon = 0;

    public void increaseTotal() {
        totalInwon++;
    }

    public int getTotalInwon() {
        return totalInwon;
    }


    public boolean hasSeat() {
        return currentSeat < MAX_SEAT;
    }

    public void increase() {
        currentSeat++;
    }

    public int getCurrentSeat() {
        return currentSeat;
    }
}
