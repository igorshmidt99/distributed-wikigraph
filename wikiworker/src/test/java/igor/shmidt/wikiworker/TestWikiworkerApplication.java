package igor.shmidt.wikiworker;

import org.springframework.boot.SpringApplication;

public class TestWikiworkerApplication {

	public static void main(String[] args) {
		SpringApplication.from(WikiworkerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
