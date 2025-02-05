package igor.shmidt.router.scheduler.service;

import igor.shmidt.router.scheduler.Load;
import igor.shmidt.router.scheduler.QueueScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SchedulerTest
class QueueSchedulerTest {

    @Autowired
    private QueueScheduler scheduler;

    private ConcurrentHashMap<String, Integer> tasks;

    private static final TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
        tasks = testUtils.initService(scheduler);
    }

    @AfterAll
    static void tearDown() {
        testUtils.executorService.shutdown();
    }

    @Test
    void scheduleLoadCorrectSpreadingTest() {
        Queue<Load> schedule = scheduler.getSchedule();
        List<Integer> usedTwice = schedule.stream()
                .map(Load::getLoad)
                .map(AtomicInteger::get)
                .filter(load -> load == 3)
                .toList();

        assertFalse(usedTwice.isEmpty());
    }

    @Test
    void onSendUsesCorrectQueueWith10Reqs() {
        Queue<Load> schedule = scheduler.getSchedule();
        int lowestLoad = 2;
        Load actual = schedule.poll();

        assertEquals(lowestLoad, actual.getLoad().get());
    }

    @Test
    void onSendUsesCorrectQueueOn50Reqs() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            testUtils.initService(scheduler);
        }
        Thread.sleep(1000);
        Queue<Load> schedule = scheduler.getSchedule();
        Load lowest = schedule.poll();
        schedule.poll();
        Load higherLoad = schedule.poll();

        assertTrue(lowest.getLoad().get() < higherLoad.getLoad().get());
    }

    @Test
    void onReceive() throws InterruptedException {
        ConcurrentHashMap.KeySetView<String, Integer> queues = this.tasks.keySet();
        String queue = tasks.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 3)
                .findAny()
                .get()
                .getKey();

        for (int i = 0; i < 2; i++) {
            testUtils.executorService.submit(() -> scheduler.onReceive(queue));
        }
        Thread.sleep(500);
        Queue<Load> schedule = scheduler.getSchedule();
        Load poll = schedule.poll();

        assertEquals(poll.getQueue(), queue);
        assertEquals(1, poll.getLoad().get());
    }

}