package igor.shmidt.router;

import igor.shmidt.router.communicator.Communicator;
import igor.shmidt.router.scheduler.Load;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RoutingConfiguration {

    private static final String PREFIX = "${spring.queue.";

    @Bean
    public Queue<Load> schedule(AmqpAdmin amqpAdmin,
                                @Value(PREFIX + "amount}") Integer amount,
                                @Value(PREFIX + "prefix}") String prefix) {
        PriorityBlockingQueue<Load> schedule = new PriorityBlockingQueue<>(amount, Comparator.comparingInt(o -> o.getLoad().get()));
        for (int i = 1; i <= amount; i++) {
            String name = prefix + i;
            org.springframework.amqp.core.Queue queue = new org.springframework.amqp.core.Queue(name);
            amqpAdmin.declareQueue(queue);
            schedule.offer(new Load(name, new AtomicInteger()));
        }
        return schedule;
    }

    @Bean
    public SimpleMessageListenerContainer container(MessageListenerAdapter listenerAdapter,
                                                    ConnectionFactory connectionFactory,
                                                    SortedMap<String, AtomicInteger> schedule) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrency(String.valueOf(schedule.size()));
        String[] keys = schedule.keySet().toArray(new String[0]);
        container.addQueueNames(keys);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Communicator receiver) {
        return new MessageListenerAdapter(receiver, "receive");
    }

    @Bean
    public Map<String, Map<String, String>> ways() {
        return new ConcurrentHashMap<>();
    }

}