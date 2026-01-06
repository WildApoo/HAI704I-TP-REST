package REST.agence;

import java.util.ArrayList;

import org.springframework.core.io.Resource;

import REST.ressources.ChambreDTO;
import REST.ressources.Client;
import REST.ressources.Ville;

public interface AgenceWebService {
	
	String getNom();

	void inscriptionClient(String pseudo, String mdp);
	
	Client connexionClient(String pseudo, String mdp);
	
	ArrayList<ChambreDTO> rechercheDisponibilite(int dateDeb, int dateFin, double prixMin, double prixMax, Ville ville, int nbPersonne, String pseudo, String mdp);
	
	ChambreDTO reservationBis(int dateDeb, int dateFin, String nomHotel, String ID, String pseudo, String mdp);
	
	Resource askForRoomPicture(String pictureUri);
}
