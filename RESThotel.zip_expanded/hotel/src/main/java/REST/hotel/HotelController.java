package REST.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import REST.ressources.ChambreDTO;
import REST.ressources.ClientReservationDTO;
import REST.ressources.Ville;

@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	// map contenant l'ensemble des instances HotelWebServiceHotelImpl
	private final Map<String, HotelWebServiceHotelImpl> hotels = new HashMap<>();
	
	public HotelController() {
		
		
		HotelWebServiceHotelImpl hotel1 = new HotelWebServiceHotelImpl("La garrigue", 3, Ville.MARSEILLE, new ArrayList<ArrayList<Integer>>(List.of(
				new ArrayList<Integer>(List.of(4,50)),
				new ArrayList<Integer>(List.of(4,50)),
				new ArrayList<Integer>(List.of(2,35)),
				new ArrayList<Integer>(List.of(8,500)),
				new ArrayList<Integer>(List.of(2,10000)),
				new ArrayList<Integer>(List.of(4,75)),
				new ArrayList<Integer>(List.of(6,70)),
				new ArrayList<Integer>(List.of(2,20))
				
				)));
		hotels.put("La garrigue", hotel1);
		
		HotelWebServiceHotelImpl hotel2 = new HotelWebServiceHotelImpl("Parigow", 3, Ville.PARIS, new ArrayList<ArrayList<Integer>>(List.of(
				new ArrayList<Integer>(List.of(4,50)),
				new ArrayList<Integer>(List.of(4,100))
				)));
		hotels.put("Parigow", hotel2);
		
		HotelWebServiceHotelImpl hotel3 = new HotelWebServiceHotelImpl("L'Yonne pas la seine", 3, Ville.PARIS, new ArrayList<ArrayList<Integer>>(List.of(
				new ArrayList<Integer>(List.of(4,50)),
				new ArrayList<Integer>(List.of(4,100))
				)));
		hotels.put("L'Yonne pas la seine", hotel3);
		
		HotelWebServiceHotelImpl hotel4 = new HotelWebServiceHotelImpl("Toulousaing", 3, Ville.TOULOUSE, new ArrayList<ArrayList<Integer>>(List.of(
				new ArrayList<Integer>(List.of(4,50)),
				new ArrayList<Integer>(List.of(4,100))
				)));
		hotels.put("Toulousaing", hotel4);
		
	}
	
	public boolean isInHotels(String id) {
		return hotels.get(id)!=null;
	}
	
    @GetMapping("/{id}/nom")
    public ResponseEntity<String> getNom(@PathVariable String id) {
    	if (this.isInHotels(id)){
            return ResponseEntity.ok(hotels.get(id).getNom());
    	}else {
    		return ResponseEntity.notFound().build();
    	}

    }

    @GetMapping("/{id}/chambres")
    public ResponseEntity<ArrayList<ChambreDTO>> possedeChambre(
            @PathVariable String id,
            @RequestParam int dateDeb,
            @RequestParam int dateFin,
            @RequestParam double prixMin,
            @RequestParam double prixMax,
            @RequestParam int nbPersonne,
            @RequestParam ClientReservationDTO clientDTO
            // "/hotels/hotel1/chambres?dateDeb=0&dateFin=0&prixMin=0&prixMax=999999&nbPersonne=0&clientId=test"
    ) {
    	
    	if (this.isInHotels(id)){
            return ResponseEntity.ok(hotels.get(id).possedeChambre(dateDeb, dateFin, prixMin, prixMax, nbPersonne, clientDTO));
    	}else {
    		return ResponseEntity.notFound().build();
    	}

    }

    @GetMapping("/{id}/located")
    public ResponseEntity<Boolean> isLocated(@PathVariable String id, @RequestParam Ville ville) {
    	if (this.isInHotels(id)){
            return ResponseEntity.ok(hotels.get(id).isLocated(ville));
    	}else {
    		return ResponseEntity.notFound().build();
    	}

    }

    @PostMapping("/{id}/reservation")
    public ResponseEntity<ChambreDTO> reservationBis(
            @PathVariable String id,
            @RequestParam int dateDebut,
            @RequestParam int dateFin,
            @RequestParam ClientReservationDTO clientDTO,
            @RequestParam String chambreID
    ) {
    	if (this.isInHotels(id)){
            return ResponseEntity.ok(hotels.get(id).reservationBis(dateDebut, dateFin, clientDTO, chambreID));
    	}else {
    		return ResponseEntity.notFound().build();
    	}

    }
    
    @GetMapping("/list")
    public ResponseEntity<ArrayList<String>> hotelsName() {
    	ArrayList<String> hotelsName = new ArrayList<>();
    	
    	for (var entry : this.hotels.entrySet()) {
    		hotelsName.add(entry.getValue().getNom());
    	}
    	return ResponseEntity.ok(hotelsName);
    }
    
    @GetMapping("/photo/{pictureName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String pictureName) {
        Resource resource = new ClassPathResource("images/" + pictureName); // chemin relatif
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pictureName)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .eTag("\""+pictureName.hashCode()+"\"")
                .body(resource);
    }

}
