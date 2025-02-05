package igor.shmidt.router.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class Load {
    private String queue;
    private AtomicInteger load;

    public void increase() {
        load.incrementAndGet();
    }

    public void decrease() {
        load.decrementAndGet();
    }
}
