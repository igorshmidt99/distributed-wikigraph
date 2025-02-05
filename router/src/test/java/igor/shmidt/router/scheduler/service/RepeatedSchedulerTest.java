package igor.shmidt.router.scheduler.service;

import igor.shmidt.router.scheduler.QueueScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SchedulerTest
public class RepeatedSchedulerTest {

    @Autowired
    private QueueScheduler scheduler;

    private static TestUtils testUtils = new TestUtils();

    @AfterAll
    static void tearDown() {
        testUtils.executorService.shutdown();
    }

    @RepeatedTest(250)
    void keysSpreadsCorrectly() {
        ConcurrentHashMap<String, AtomicInteger> queues = new ConcurrentHashMap<>();
        for (int j = 0; j < 16; j++) {
            testUtils.executorService.submit(() -> {
                String queue = scheduler.onSend();
                synchronized (queues) {
                    AtomicInteger load = queues.get(queue);
                    if (load == null) {
                        load = new AtomicInteger(1);
                    } else {
                        load.incrementAndGet();
                    }
                    queues.put(queue, load);
                }
            });
        }
        while (!eachQueueIsLoadedFourTimes(queues)) {
            Thread.onSpinWait();
        }
        assertTrue(eachQueueIsLoadedFourTimes(queues));
    }

    private boolean eachQueueIsLoadedFourTimes(ConcurrentHashMap<String, AtomicInteger> queues) {
        return queues.values().stream().filter(load -> load.get() == 4).toList().size() == 4;
    }

}
