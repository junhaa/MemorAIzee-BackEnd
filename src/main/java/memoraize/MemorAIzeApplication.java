package memoraize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
public class MemorAIzeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemorAIzeApplication.class, args);
	}

}
