package REST.agence;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import REST.ressources.Client;
import REST.ressources.Ville;


@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final AgenceRegistry registry;

    public AgenceController(AgenceRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listAgences() {
        return ResponseEntity.ok(
            registry.getAll().keySet().stream().toList()
        );
    }
    
    @PostMapping("/{nom}/inscription")
    public ResponseEntity<?> inscription(
    		@PathVariable String nom,
    		@RequestParam String pseudo,
    		@RequestParam String mdp
    		) {
        AgenceWebServiceImpl agence = registry.get(nom);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
            agence.inscriptionClient(pseudo, mdp);
            return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{nom}/connexion")
    public ResponseEntity<?> connexion(
    		@PathVariable String nom,
    		@RequestParam String pseudo,
    		@RequestParam String mdp
    		) {
        AgenceWebServiceImpl agence = registry.get(nom);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
            Client response = agence.connexionClient(pseudo, mdp);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/{nom}/disponibilites")
    public ResponseEntity<?> disponibilites(
            @PathVariable String nom,
            @RequestParam int dateDeb,
            @RequestParam int dateFin,
            @RequestParam Ville ville,
            @RequestParam double prixMin,
            @RequestParam double prixMax,
            @RequestParam int nbPersonne,
            @RequestParam String pseudo,
            @RequestParam String mdp
    ) {
        AgenceWebServiceImpl agence = registry.get(nom);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            agence.rechercheDisponibilite(dateDeb, dateFin, prixMin, prixMax, ville, nbPersonne, pseudo, mdp)
        );
    }
    
    @GetMapping("/{nom}/reservation")
    public ResponseEntity<?> reservation(
            @PathVariable String nom,
            @RequestParam int dateDeb,
            @RequestParam int dateFin,
            @RequestParam String nomHotel,
            @RequestParam String ID,
            @RequestParam String pseudo,
            @RequestParam String mdp
    ) {
        AgenceWebServiceImpl agence = registry.get(nom);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            agence.reservationBis(dateDeb, dateFin, nomHotel, ID, pseudo, mdp)
        );
    }
    
    @GetMapping("/{agenceName}/photo")
    public ResponseEntity<?> getPhoto(
    		@PathVariable String agenceName,
    		@RequestParam String pictureUri
    		) {
        AgenceWebServiceImpl agence = registry.get(agenceName);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = agence.askForRoomPicture(pictureUri);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }
    
}
