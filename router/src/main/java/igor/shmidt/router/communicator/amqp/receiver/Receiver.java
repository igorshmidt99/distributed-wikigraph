package igor.shmidt.router.communicator.amqp.receiver;

import org.springframework.amqp.core.Message;

public interface Receiver {

    void receive(Message received);

}
