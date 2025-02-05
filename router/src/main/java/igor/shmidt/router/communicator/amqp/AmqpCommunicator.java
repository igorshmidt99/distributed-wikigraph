package igor.shmidt.router.communicator.amqp;

import igor.shmidt.router.communicator.Communicator;
import igor.shmidt.router.routing.RequestProperties;
import igor.shmidt.router.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AmqpCommunicator implements Communicator {

    private final AmqpTemplate amqpTemplate;
    private final MessageConverter messageConverter;
    private final Scheduler scheduler;
    private final Map<String, Map<String, String>> ways;

    @Override
    public void send(RequestProperties property, Pair<String, String> message) {
        Message amqpMessage = messageConverter.serialize(message);
        putToWays(property.getReqId(), message.getFirst());
        String routingKey = scheduler.onSend();
        amqpTemplate.send(routingKey, amqpMessage);
    }

    @Override
    public List<String> receiveWay(String reqId) {
        Map<String, String> ways = this.ways.get(reqId);
        int targetAmount = 0;
        while (targetAmount != ways.size()) {
            targetAmount = ways.values().stream()
                    .filter(way -> !way.isEmpty())
                    .toList()
                    .size();
        }
        // Сохранять пути в репозиторий
        return ways.values()
                .stream().toList();
    }

    private void putToWays(String reqId, String firstPage) {
        Map<String, String> way = new ConcurrentHashMap<>();
        way.put(firstPage, "");
        ways.put(reqId, way);
    }

}