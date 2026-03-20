package com.exception.exception.redis.redissonConcurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LectureApplyService {

    private final RedissonClient redissonClient;
    private final LectureSeatStore seatStore;

    public String apply(int requestId) {

        RLock lock = redissonClient.getLock("lecture:lock");

        try {
            boolean locked = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!locked) {
                seatStore.increaseTotal();
                System.out.println("현재 총 인원 = " + seatStore.getTotalInwon());
                log.info("[FAIL][{}] 락 획득 실패", requestId);
                return "FAIL - LOCK";
            }

            // ===== 임계 영역 =====
            if (!seatStore.hasSeat()) {
                seatStore.increaseTotal();
                System.out.println("현재 총 인원 = " + seatStore.getTotalInwon());
                log.info("[FAIL][{}] 정원 초과 (현재: {})",
                        requestId, seatStore.getCurrentSeat());
                return "FAIL - FULL";
            }
            seatStore.increaseTotal();
            System.out.println("현재 총 인원 = " + seatStore.getTotalInwon());

            seatStore.increase();

            log.info("[SUCCESS][{}] 신청 성공 (현재 인원: {})",
                    requestId, seatStore.getCurrentSeat());

            return "SUCCESS";

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
