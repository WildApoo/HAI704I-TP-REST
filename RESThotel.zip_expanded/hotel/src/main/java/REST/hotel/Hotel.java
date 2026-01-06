package REST.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import REST.ressources.ChambreDTO;
import REST.ressources.ClientReservationDTO;
import REST.ressources.Ville;

public class Hotel {
	private String nom;
	private int nbEtoile;
	private Ville ville;
	private ArrayList<Chambre> chambres;
	private Map<String, Client> clients;
	private Map<String, String> tarifParAgence;
	
	
	public Hotel(String nom, int nbEtoile, Ville ville, ArrayList<ArrayList<Integer>> chambres) {
		this.nom = nom;
		this.nbEtoile = nbEtoile;
		this.ville = ville;
		this.chambres = new ArrayList<Chambre>();
		this.addManyChambre(chambres);
		this.clients = new HashMap<>();
		this.tarifParAgence = new HashMap<>();
		

	}
	
	public String getNom() {
		return this.nom;
	}
	
	public Ville getVille() {
		return this.ville;
	}
	
	
	// Method
	public void AddOneChambre(int nbLit, int prix) {
		// nbLit, prix, hotel
		this.chambres.add(new Chambre(nbLit, prix, this));
	}
	
	public void addManyChambre(ArrayList<ArrayList<Integer>> chambres) {
		for (ArrayList<Integer> chambre : chambres) {
			this.AddOneChambre(chambre.get(0), chambre.get(1));
		}
	}
	
	// recherche par ID ou directement en renseignant l'objet
	public void removeManyChambre(List<?> items) {
	    if (items.isEmpty()) return;

	    Object first = items.get(0);

	    // ID
	    if (first instanceof String) {
	        List<String> ids = (List<String>) items;
	        this.chambres.removeIf(c -> ids.contains(c.getID()));
	    } 
	    // objet Chambre
	    else if (first instanceof Chambre) {
	        List<Chambre> chambresToRemove = (List<Chambre>) items;
	        this.chambres.removeAll(chambresToRemove);
	    }
	}

	
	public boolean isLocated(Ville ville) {
		return (this.ville == ville);
	}
	
	public ArrayList<ChambreDTO> possedeChambre(int dateDeb, int dateFin, double prixMin, double prixMax, int nbPersonne, ClientReservationDTO clientDTO) {
		double tarifAgence = this.getTarifAgenceFromClientDTO(clientDTO);
		
		ArrayList<ChambreDTO> chambresCorrespondantes = new ArrayList<ChambreDTO>();
		for (Chambre chambre : this.chambres) {
			if(chambre.estDisponible(dateDeb, dateFin) && chambre.peutAcccueillir(nbPersonne) && chambre.prixComprisEntre(prixMin, prixMax)) {
				ChambreDTO dto = chambre.getDTO(chambre.getPrix()*tarifAgence);	
				chambresCorrespondantes.add(dto);
			}
		}
		
		return chambresCorrespondantes;
	}
	
	public ChambreDTO reservationBis(int dateDebut, int dateFin, ClientReservationDTO clientDTO, String ID) {
		Client client = this.getClientFromDTO(clientDTO);
		Chambre chambre = getChambreFromID(ID);
		ChambreDTO response = null;
		if (chambre!=null) {
			if(chambre.estDisponible(dateDebut, dateFin)) {
				chambre.reservation(dateDebut, dateFin, client);
				ChambreDTO dto = chambre.getDTO();	
				response = dto;
			}
		}
		
		
		return response;
	}
	
	public Chambre getChambreFromDTO(ChambreDTO chambreDTO) {
		String id = chambreDTO.ID();
		
		int index = 0;
		Chambre chambre = null;
		while (chambre==null && index < this.chambres.size()) {
			if (this.chambres.get(index).getID().equals(id)) {
				chambre = this.chambres.get(index);
			}
			index++;
		}
		
		return chambre;
	}
	
	public Chambre getChambreFromID(String ID) {
		
		int index = 0;
		Chambre chambre = null;
		while (chambre==null && index < this.chambres.size()) {
			if (this.chambres.get(index).getID().equals(ID)) {
				chambre = this.chambres.get(index);
			}
			index++;
		}
		
		return chambre;
	}
	
	// Méthode permettant de passer d'un client TDO à un client classique.
	private Client getClientFromDTO(ClientReservationDTO clientDTO) {
		Client client = this.clients.get(clientDTO.ID());	// recherche de l'id dans le H-map
		
		if (client == null) {	// création du client s'il n'existe pas
			client = new Client(clientDTO.ID());
			clients.put(client.getId(), client);
		}
		return client;
	}
	
	private double getTarifAgenceFromClientDTO(ClientReservationDTO clientDTO) {
		Client client = this.getClientFromDTO(clientDTO);
		String agence = client.getId().split("/")[0];
		
		String tarif = this.tarifParAgence.get(agence);
		
		if (tarif == null) {
			this.tarifParAgence.put(agence, String.valueOf(0.8 + Math.random() * (1.-0.8)));
			tarif = this.tarifParAgence.get(agence);
		}
		
		return Double.parseDouble(tarif);
	}

}
