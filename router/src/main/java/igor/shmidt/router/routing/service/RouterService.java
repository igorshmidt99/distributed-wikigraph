package igor.shmidt.router.routing.service;

import java.util.List;
import java.util.Map;

public interface RouterService {

    String sendToWorker(Map<String, String> pair);

    List<String> receiveWay(String reqId);

}