package igor.shmidt.router.integrational;

import igor.shmidt.router.config.TestcontainersConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.RabbitMQContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RabbitTests {

    @Autowired
    private RabbitTemplate worker;

    @Autowired
    private AmqpAdmin rabbitAdmin;

    @Autowired
    private RabbitMQContainer rabbitContainer;

    @BeforeEach
    void setUp() {
        rabbitContainer.start();
    }

    @AfterEach
    void tearDown() {
        rabbitContainer.close();
    }

    @Test
    void contextLoads() {
        worker.setRoutingKey("queue.worker");
        worker.setDefaultReceiveQueue("queue.worker");
        worker.convertAndSend("Message to worker");

        Message worker1 = worker.receive("queue.worker");
        assertNotNull(worker1);
        assertNotNull(worker1.getBody());

    }

}
