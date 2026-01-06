package REST.agence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class AgenceApplication {
	
	// to send output to the log (the console, in this example)
	private static final Logger log = LoggerFactory.getLogger(AgenceApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(AgenceApplication.class, args);
	}


}
