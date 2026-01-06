package REST.hotel;

import java.util.ArrayList;

import REST.ressources.ChambreDTO;
import REST.ressources.ClientReservationDTO;
import REST.ressources.Ville;

public interface HotelWebService {

	String getNom();
	
	ArrayList<ChambreDTO> possedeChambre(int dateDeb, int dateFin, double prixMin, double prixMax, int nbPersonne, ClientReservationDTO clientDTO);
	
	ChambreDTO reservationBis(int dateDebut, int dateFin, ClientReservationDTO clientDTO, String ID);
	
	boolean isLocated(Ville ville);

}
