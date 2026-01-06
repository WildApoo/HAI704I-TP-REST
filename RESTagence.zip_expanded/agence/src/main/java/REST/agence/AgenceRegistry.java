package REST.agence;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AgenceRegistry {
	
    private final Map<String, AgenceWebServiceImpl> agences = new HashMap<>();

    public void register(AgenceWebServiceImpl agence) {
        agences.put(agence.getNom(), agence);
        System.out.println("agence ajoutée : " + agence.getNom());
    }

    public AgenceWebServiceImpl get(String nom) {
        return agences.get(nom);
    }

    public Map<String, AgenceWebServiceImpl> getAll() {
    	System.out.println("les agences : " + agences);
        return agences;
    }

}
