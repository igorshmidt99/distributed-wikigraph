package igor.shmidt.router.scheduler.service;

import igor.shmidt.router.scheduler.Scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;

class TestUtils {
    static final int threads = 5;
    final ExecutorService executorService = Executors.newFixedThreadPool(threads);

    ConcurrentHashMap<String, Integer> initService(Scheduler scheduler) {
        ConcurrentHashMap<String, Integer> tasks = new ConcurrentHashMap<>();
        for (int i = 0; i < threads * 2; i++) {
            executorService.submit(() -> {
                String key = scheduler.onSend();
                synchronized (tasks) {
                    Integer load = tasks.get(key);
                    if (load == null) {
                        load = 0;
                    }
                    load++;
                    tasks.put(key, load);
                }
            });
        }
        while (checkThatFilled(tasks) != 3) {
            Thread.onSpinWait();
        }
        return tasks;
    }

    private int checkThatFilled(ConcurrentHashMap<String, Integer> tasks) {
        return tasks.values().stream()
                .reduce(findHighest())
                .orElse(0);
    }

    private BinaryOperator<Integer> findHighest() {
        return (load1, load2) -> {
            if (load1 > load2) {
                return load1;
            }
            return load2;
        };
    }

}
