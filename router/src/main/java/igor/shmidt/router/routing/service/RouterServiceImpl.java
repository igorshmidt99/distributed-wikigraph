package igor.shmidt.router.routing.service;

import igor.shmidt.router.communicator.Communicator;
import igor.shmidt.router.routing.RequestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouterServiceImpl implements RouterService {

    private final Communicator amqpCommunicator;

    @Override
    public String sendToWorker(Map<String, String> pair) {
        Map.Entry<String, String> entry = pair.entrySet().stream()
                .findAny()
                .orElseThrow();
        String startPage = entry.getKey();
        String endPage = entry.getValue();
        String reqId = startPage + endPage;

        List<String> innerPages = requestLinks(startPage);
        RequestProperties requestProperties = RequestProperties.of(reqId, innerPages.size());
        innerPages.forEach(pageName -> amqpCommunicator.send(requestProperties, Pair.of(pageName, endPage)));
        return reqId;
    }

    @Override
    public List<String> receiveWay(String reqId) {
        return amqpCommunicator.receiveWay(reqId);
    }

    private List<String> requestLinks(String startPage) {
        return new ArrayList<>();
    }

}