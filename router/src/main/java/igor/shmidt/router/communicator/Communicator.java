package igor.shmidt.router.communicator;

import igor.shmidt.router.routing.RequestProperties;
import org.springframework.data.util.Pair;

import java.util.List;

public interface Communicator {

    void send(RequestProperties reqId, Pair<String, String> message);

    List<String> receiveWay(String reqId);

}