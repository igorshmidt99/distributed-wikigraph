package igor.shmidt.router.communicator.amqp.receiver;

import lombok.Getter;

@Getter
public enum ResponseKeys {

    REQ_ID("reqId"),
    START_PAGE("startPage"),
    WAY("way");

    private final String key;

    ResponseKeys(String key) {
        this.key = key;
    }

}
