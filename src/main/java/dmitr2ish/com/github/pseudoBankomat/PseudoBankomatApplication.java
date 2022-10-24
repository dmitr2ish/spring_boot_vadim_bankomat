package dmitr2ish.com.github.pseudoBankomat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PseudoBankomatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PseudoBankomatApplication.class, args);
	}

	@Bean
	public Logger getLogger(){
		return LoggerFactory.getLogger(PseudoBankomatApplication.class);
	}

}
