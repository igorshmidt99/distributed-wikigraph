package igor.shmidt.router.communicator.amqp.receiver;

import igor.shmidt.router.communicator.amqp.MessageConverter;
import igor.shmidt.router.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static igor.shmidt.router.communicator.amqp.receiver.ResponseKeys.*;

@Component
@RequiredArgsConstructor
public class ReceiverImpl implements Receiver {

    private final MessageConverter messageConverter;
    private final Scheduler scheduler;
    private final Map<String, Map<String, String>> ways;

    @Override
    public void receive(Message received) {
        String routingKey = getRoutingKey(received);
        scheduler.onReceive(routingKey);
        insertWay(received);
    }

    private String getRoutingKey(Message received) {
        MessageProperties messageProperties = received.getMessageProperties();
        return messageProperties.getReceivedRoutingKey();
    }

    private void insertWay(Message received) {
        Map<String, String> deserialized = messageConverter.deserialize(received);

        String reqId = deserialized.get(REQ_ID.getKey());
        String startPage = deserialized.get(START_PAGE.getKey());
        String way = deserialized.get(WAY.getKey());

        Map<String, String> startPageToWay = ways.get(reqId);
        startPageToWay.put(startPage, way);
    }

}