package REST.agence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AgenceRunner implements ApplicationRunner {
	
    private final RestClient.Builder builder;
    private final AgenceRegistry registry;

    public AgenceRunner(RestClient.Builder builder, AgenceRegistry registry) {
        this.builder = builder;
        this.registry = registry;
    }

	@Override
	public void run(ApplicationArguments args) throws Exception {
		/*
		 * Il n'existe qu'un seul RestClient puisque nous avons créé plusieurs
		 * instances d'hotelImpl dans un seul controller.
		 * Nous allons cependant injecter ce RestClient autant de fois que le
		 * nombre d'instances d'hotel pour "simuler" un comportement normal
		 * si chaque hotel avait été généré dans son propre projet
		 */
		RestClient clientHotel =
            builder.baseUrl("http://localhost:8080").build();

        var hotelsName = clientHotel.get()
            .uri("/hotels/list")
            .retrieve()
            .body(new ParameterizedTypeReference<ArrayList<String>>() {});

        Map<String, RestClient> hotels = new HashMap<>();
        for (String name : hotelsName) {
            hotels.put(name, clientHotel);
        }
        
        Map<String, RestClient> hotelsDeParisien = new HashMap<>();
        hotelsDeParisien.put(hotelsName.get(0), clientHotel);
        hotelsDeParisien.put(hotelsName.get(1), clientHotel);
        
		AgenceWebServiceImpl agence1 = new AgenceWebServiceImpl("Agence des parisiens", 5, 0.1, hotelsDeParisien);

		AgenceWebServiceImpl agence2 = new AgenceWebServiceImpl("Agence du bout du rouleau", 0.5, 0.5, hotels);
		
		AgenceWebServiceImpl agence3 = new AgenceWebServiceImpl("Agence fin de partie", 10, 0.01, hotels);
		
        registry.register(agence1);
        registry.register(agence2);
        registry.register(agence3);
        
        
        
	}

}
