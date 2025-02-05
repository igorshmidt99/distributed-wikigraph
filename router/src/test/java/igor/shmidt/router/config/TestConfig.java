package igor.shmidt.router.config;

import igor.shmidt.router.scheduler.Load;
import igor.shmidt.router.scheduler.QueueScheduler;
import igor.shmidt.router.scheduler.Scheduler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@TestConfiguration
public class TestConfig {
    @Bean
    public Queue<Load> schedule() {
        int amount = 4;
        String prefix = "queue.";
        Queue<Load> schedule = new PriorityBlockingQueue<>(amount, Comparator.comparingInt(o -> o.getLoad().get()));
        for (int i = 1; i <= amount; i++) {
            String name = prefix + i;
            schedule.offer(new Load(name, new AtomicInteger()));
        }
        return schedule;
    }

    @Bean
    public Scheduler scheduler(Queue<Load> schedule) {
        return new QueueScheduler(schedule, new ReentrantLock());
    }
}