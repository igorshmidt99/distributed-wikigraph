package igor.shmidt.router.communicator.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.data.util.Pair;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageConverterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageConverter messageConverter = new MessageConverter(new ObjectMapper());
    private Pair<String, String> req;

    @BeforeEach
    void setUp() {
        req = Pair.of("Albert Einstein", "GitHub");
    }

    @Test
    void serializeNotEmpty() throws JsonProcessingException {
        byte[] body = objectMapper.writeValueAsBytes(Map.of(req.getFirst(), req.getSecond()));
        Message expected = new Message(body);
        Message actual = messageConverter.serialize(req);

        assertEquals(expected, actual);
    }

    @Test
    void deserialize() throws JsonProcessingException {
        Map<String, String> expected = Map.of(req.getFirst(), req.getSecond());
        byte[] body = objectMapper.writeValueAsBytes(expected);
        Message received = new Message(body);
        Map<String, String> actual = messageConverter.deserialize(received);

        assertEquals(expected, actual);
    }

}