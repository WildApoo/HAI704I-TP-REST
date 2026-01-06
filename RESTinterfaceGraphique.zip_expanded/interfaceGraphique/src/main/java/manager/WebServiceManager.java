// IMPORTANT : code généré à 90% par chat gpt
package manager;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import ui.main.panels.SearchPanel.SearchCriteria;
import ui.model.Client;
import web.service.client.ChambreDTO;

/**
 * Manager central côté client graphique.
 * - Connaît les agences REST
 * - Gère l'authentification utilisateur
 * - Sert de point d'accès unique pour l'UI
 */
@Component
public class WebServiceManager implements ApplicationRunner {

    private final Map<String, RestClient> agences = new HashMap<>();
    private final RestClient.Builder builder;

    private String pseudo;
    private String mdp;

    public WebServiceManager(RestClient.Builder builder) {
        this.builder = builder;
    }

    // -------------------------- INITIALISATION --------------------------

    @Override
    public void run(ApplicationArguments args) {
        RestClient clientAgence =
                builder.baseUrl("http://localhost:8081").build();

        agences.put("Agence des parisiens", clientAgence);
        agences.put("Agence du bout du rouleau", clientAgence);
        agences.put("Agence fin de partie", clientAgence);

        System.out.println("Agences chargées : " + agences.keySet());
    }

    // -------------------------- INSCRIPTION --------------------------

    public boolean inscrireSurToutes(String pseudo, String mdp) {
        try {
            for (Entry<String, RestClient> entry : agences.entrySet()) {
                String agenceName = entry.getKey();
                RestClient agenceClient = entry.getValue();

                agenceClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/agences/{agenceName}/inscription")
                                .queryParam("pseudo", pseudo)
                                .queryParam("mdp", mdp)
                                .build(agenceName)
                        )
                        .retrieve()
                        .toBodilessEntity();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Erreur inscription : " + e.getMessage());
            return false;
        }
    }

    // -------------------------- CONNEXION --------------------------

    public boolean connexion(String pseudo, String mdp) {

        for (Entry<String, RestClient> entry : agences.entrySet()) {
            try {
                @Nullable Client response = entry.getValue().get()
                    .uri(uriBuilder -> uriBuilder
                        .path("/agences/{agenceName}/connexion")
                        .queryParam("pseudo", pseudo)
                        .queryParam("mdp", mdp)
                        .build(entry.getKey())
                    )
                    .retrieve()
                    .body(Client.class);
                
                System.out.println("adrien : " + response);

                if (response!=null) {
                    this.pseudo = response.pseudo();
                    this.mdp = response.mdp();
                    System.out.println("Connexion réussie sur " + entry.getKey());
                    return true;
                }

            } catch (HttpClientErrorException.Unauthorized e) {
                return false;
            } catch (RestClientException e) {
                // agence down / erreur réseau
                System.err.println("Erreur agence " + entry.getKey());
            }
        }
        return false;
    }


 // -------------------------- RECHERCHE CHAMBRES --------------------------
    public List<ChambreDTO> rechercherChambres(String agenceName, SearchCriteria criteria) {
        RestClient client = agences.get(agenceName);
        if (client == null) throw new IllegalArgumentException("Agence inconnue : " + agenceName);

        // On récupère un tableau puis on convertit en List
        ChambreDTO[] chambresArray = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/agences/{nom}/disponibilites")
                        .queryParam("dateDeb", criteria.dateDeb)
                        .queryParam("dateFin", criteria.dateFin)
                        .queryParam("ville", criteria.ville.name())
                        .queryParam("prixMin", criteria.prixMin)
                        .queryParam("prixMax", criteria.prixMax)
                        .queryParam("nbPersonne", criteria.nbLits)
                        .queryParam("pseudo", pseudo)
                        .queryParam("mdp", mdp)
                        .build(agenceName)
                )
                .retrieve()
                .body(ChambreDTO[].class);

        return List.of(chambresArray);
    }

    // -------------------------- RESERVATION --------------------------
    public ChambreDTO reserverChambre(String agenceName, String nomHotel, String ID, SearchCriteria criteria, String pseudo, String mdp) {
        RestClient client = agences.get(agenceName);
        if (client == null) throw new IllegalArgumentException("Agence inconnue : " + agenceName);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/agences/{nom}/reservation")
                        .queryParam("dateDeb", criteria.dateDeb)
                        .queryParam("dateFin", criteria.dateFin)
                        .queryParam("nomHotel", nomHotel)
                        .queryParam("ID", ID)
                        .queryParam("pseudo", pseudo)
                        .queryParam("mdp", mdp)
                        .build(agenceName)
                )
                .retrieve()
                .body(ChambreDTO.class);
    }
    
    // -------------------------- PHOTO --------------------------    
    public ImageIcon loadChambreImage(String agenceName, String imageUri) {
        try {
            byte[] data = agences.get(agenceName).get()
                .uri(uriBuilder -> uriBuilder
                    .path("/agences/{agenceName}/photo")
                    .queryParam("pictureUri", imageUri)
                    .build(agenceName)
                )
                .retrieve()
                .body(byte[].class);

            if (data == null || data.length == 0) {
                return null;
            }

            ImageIcon icon = new ImageIcon(data);
            Image scaled = icon.getImage()
                    .getScaledInstance(150, 120, Image.SCALE_SMOOTH);

            return new ImageIcon(scaled);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // -------------------------- GETTERS --------------------------

    public Map<String, RestClient> getAgences() {
        return agences;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMdp() {
        return mdp;
    }
}
