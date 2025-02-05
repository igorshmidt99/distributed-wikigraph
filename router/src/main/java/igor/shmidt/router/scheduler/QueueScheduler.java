package igor.shmidt.router.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class QueueScheduler implements Scheduler {

    private final Queue<Load> schedule;

    private final ReentrantLock lock;

    @Override
    public String onSend() {
        try {
            lock.lock();
            Load load = schedule.poll();
            load.increase();
            String routingKey = load.getQueue();
            schedule.offer(load);
            return routingKey;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onReceive(String routingKey) {
        try {
            lock.lock();
            Load load = schedule.stream().filter(l -> l.getQueue().equals(routingKey)).findAny().orElseThrow();
            schedule.remove(load);
            load.decrease();
            schedule.offer(load);
        } finally {
            lock.unlock();
        }

    }

    public Queue<Load> getSchedule() {
        try {
            lock.lock();
            Queue<Load> loads = new PriorityBlockingQueue<>(schedule.size(), Comparator.comparingInt(o -> o.getLoad().get()));
            loads.addAll(schedule);
            return loads;
        } finally {
            lock.unlock();
        }
    }

}