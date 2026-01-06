package REST.agence;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.client.RestClient;

import REST.ressources.ChambreDTO;
import REST.ressources.Client;
import REST.ressources.Ville;


public class AgenceWebServiceImpl implements AgenceWebService {

	private Agence agence;
	
	public AgenceWebServiceImpl(String nom, double comFix, double comVar, Map<String, RestClient> hotels) {
		agence = new Agence(nom, comFix, comVar, hotels);
	}
	
	@Override
	public String getNom() {
		return agence.getNom();
	}
	
	@Override
	public void inscriptionClient(String pseudo, String mdp) {
		agence.inscriptionClient(pseudo, mdp);
	}
	
	@Override
	public Client connexionClient(String pseudo, String mdp) {
		return agence.connexionClient(pseudo, mdp);
	}
	
	@Override
	public ArrayList<ChambreDTO> rechercheDisponibilite(int dateDeb, int dateFin, double prixMin, double prixMax, Ville ville, int nbPersonne, String pseudo, String mdp){
		return agence.rechercheDisponibilite(dateDeb, dateFin, prixMin, prixMax, ville, nbPersonne, pseudo, mdp);
	}

	
	@Override
	public ChambreDTO reservationBis(int dateDeb, int dateFin, String nomHotel, String ID, String pseudo, String mdp) {
		return agence.reservationBis(dateDeb, dateFin, nomHotel, ID, pseudo, mdp);
	}
	
	@Override
	public Resource askForRoomPicture(String pictureUri) {
		return agence.askForRoomPicture(pictureUri);
	}
	
	
	
}
