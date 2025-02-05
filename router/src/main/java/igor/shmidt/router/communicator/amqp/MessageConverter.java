package igor.shmidt.router.communicator.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageConverter {

    private final ObjectMapper objectMapper;

    public Message serialize(Pair<String, String> message) {
        ObjectWriter writer = objectMapper.writer();
        Message amqpMessage;
        try {
            byte[] bytes = writer.writeValueAsBytes(Map.of(message.getFirst(), message.getSecond()));
            amqpMessage = new Message(bytes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return amqpMessage;
    }

    public Map<String, String> deserialize(Message message) {
        Map<String, String> resp = new HashMap<>();
        if (message != null) {
            byte[] body = message.getBody();
            try {
                resp = objectMapper.readerForMapOf(String.class).readValue(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return resp;
    }

}