package com.youlai.mall.sms;

import com.youlai.mall.sms.service.IdempotentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiezhiyan
 * 2022/04/18 19:46
 */
@SpringBootTest
public class IdempotentTest {

    @Autowired
    private IdempotentService testService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                testService.lockFailFast();
            });
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                testService.lockByWait();
            });
        }
    }

    @Test
    public void test3() {
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                testService.lockMysql();
            });
        }

    }

    @Test
    public void test4() {
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                testService.idempotent("");
            });
        }
    }

    @Test
    public void test5() {
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                testService.idempotentCode2("1001");
            });
        }
    }
}
