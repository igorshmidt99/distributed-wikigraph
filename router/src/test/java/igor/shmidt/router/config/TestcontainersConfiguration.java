package igor.shmidt.router.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    private static final Integer PORT = 5672;
//    private static final String QUEUE_NAME = "queue.worker";
//    private static final String USER = "myuser";
//    private static final String PASS = "secret";

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.0-management"));
//                .withQueue(QUEUE_NAME)
//                .withUser(USER, PASS)
//                .withExposedPorts(PORT);
    }

    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:latest"))
                .withExposedPorts(6379);
    }

//    @Bean
//    @DependsOn(value = "rabbitContainer")
//    ConnectionFactory factory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setUsername(USER);
//        connectionFactory.setPassword(PASS);
//        connectionFactory.setHost("localhost");
//        connectionFactory.setPort(PORT);
//        connectionFactory.setAddresses(connectionFactory.getHost() + ":" + connectionFactory.getPort());
//        return connectionFactory;
//    }

//    @Bean
//    RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
////        rabbitTemplate.setConnectionFactory(factory);
//        rabbitTemplate.setDefaultReceiveQueue(QUEUE_NAME);
//        rabbitTemplate.setRoutingKey(QUEUE_NAME);
//        return rabbitTemplate;
//    }
//
//    @Bean
//    RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
//        return new RabbitAdmin(rabbitTemplate);
//    }

//    @Bean
//    AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        new RabbitAdmin((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory);
//        return;
//    }

}
