package igor.shmidt.router.scheduler;

public interface Scheduler {

    String onSend();

    void onReceive(String routingKey);

}
