package igor.shmidt.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RouterApplication {

    // Роутер должен понимать сколько инстансов воркеров будет поднято
    // Распределение будет устроено через счетчик - отправление сообщения в очередь -> инкремент счетчика, получение декремент
    // Сначала определю кол-во очередей и воркеров статически внутри роутера


    // Будет контроллер, принимающий два параметра: начало - конец
    // Увеличиваю значение счетчика для конкретной очереди и отправляю сообщение воркеру


    public static void main(String[] args) {

        SpringApplication.run(RouterApplication.class, args);
    }

}
