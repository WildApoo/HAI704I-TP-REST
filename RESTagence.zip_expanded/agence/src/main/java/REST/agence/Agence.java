package REST.agence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestClient;

import REST.ressources.ChambreDTO;
import REST.ressources.Client;
import REST.ressources.ClientReservationDTO;
import REST.ressources.Ville;

public class Agence {
	private String nom;
	private double comFix;
	private double comVar;
	private Map<String, RestClient> hotels;
	private Map<String, Client> clients;
	

	
	// Constructeur
	public Agence(String nom, double comFix, double comVar, Map<String, RestClient> hotels) {
		super();
		this.nom = nom;
		this.comFix = comFix;
		this.comVar = comVar;
		this.clients = new HashMap<>();
		this.hotels = hotels;		
		
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getComFix() {
		return comFix;
	}

	public void setComFix(double comFix) {
		this.comFix = comFix;
	}

	public double getComVar() {
		return comVar;
	}

	public void setComVar(double comVar) {
		this.comVar = comVar;
	}

	public Map<String, RestClient> getHotels() {
		return hotels;
	}

	public void setHotels(Map<String, RestClient> hotels) {
		this.hotels = hotels;
	}
	
	public void inscriptionClient(String pseudo, String mdp) {
		Client client = new Client(pseudo, mdp);
		this.clients.put(pseudo, client);
		System.out.println("Inscription avec succès du client : "+ client.pseudo());
	}
	
	public Client connexionClient(String pseudo, String mdp) {

		Client client;
		Client found = null;

		client = this.clients.get(pseudo);
		if (client != null && client.mdp().equals(mdp)) {
			found = client;
			System.out.println("connexion client réussie");
		}else {
			System.err.println("clé de connexion non reconnue");
		}

		System.out.println("found : "+ found);
		return found;
		
	}
	



	// Méthodes
	public ArrayList<ChambreDTO> rechercheDisponibilite(int dateDeb, int dateFin, double prixMin, double prixMax, Ville ville, int nbPersonne, String pseudo, String mdp) {
		
		ArrayList<ChambreDTO> chambres = new ArrayList<ChambreDTO>();
		ArrayList<ChambreDTO> hotelChambre;
		
		
		Client client = this.connexionClient(pseudo, mdp);
		if (client!=null) {
			ClientReservationDTO clientDTO = this.transformClientToDTO(client);
			
			RestClient hotel;
			for (String hotelName : this.hotels.keySet()) {
				
				hotel = this.hotels.get(hotelName);
				
				Boolean hotelIsLocated = hotel.get()
											.uri(uriBuilder -> uriBuilder
													.path("/hotels/{hotelId}/located")
													.queryParam("ville", ville)
													.build(hotelName))
											.retrieve()
											.body(Boolean.class);
				System.out.println("est localisé dans la ville : " + hotelIsLocated);
				
				
				if(hotelIsLocated){
					final double prixMinssCom = ((prixMin - this.comFix) / (1+this.comVar));
					
					final double prixMaxssCom = ((prixMax - this.comFix) / (1+this.comVar));
					System.out.println("recherche des chambres de l'hotel : " + hotelName);
					hotelChambre = hotel.get()
										.uri(uriBuilder -> uriBuilder
											.path("/hotels/{hotelId}/chambres")
											.queryParam("dateDeb", dateDeb)
											.queryParam("dateFin", dateFin)
											.queryParam("prixMin", prixMinssCom)
											.queryParam("prixMax", prixMaxssCom)
											.queryParam("nbPersonne", nbPersonne)
											.queryParam("clientDTO", clientDTO)
											.build(hotelName)
											)
										.retrieve()
										.body(new ParameterizedTypeReference<ArrayList<ChambreDTO>>() {});
					System.out.println("chambres trouvées : "+hotelChambre);
					if(!hotelChambre.isEmpty()) {
						BigDecimal bd;
						for (ChambreDTO c : hotelChambre) {
							double finalPrix = (c.prix() * (1+this.comVar)) + this.comFix;
							bd = new BigDecimal(finalPrix).setScale(2, RoundingMode.HALF_UP);
							finalPrix = bd.doubleValue();
							
							ChambreDTO chambreApresCommission = new ChambreDTO(c.ID(),
																				c.nbLit(),
																				finalPrix,
																				c.nomHotel(),
																				c.ville(),
																				c.imageUri());
							chambres.add(chambreApresCommission);
							
						}
					}
				}
	
			}
		}
		
		return chambres;
	}
	
	
	// criteria.dateDeb, criteria.dateFin, dto.getNomHotel(), dto.getID(), pseudo, mdp
	public ChambreDTO reservationBis(int dateDeb, int dateFin, String nomHotel, String ID, String pseudo, String mdp) {

		RestClient hotel = this.hotels.get(nomHotel);
		
		ChambreDTO chambre = null;
		
		
		if(hotel!=null) {
			Client client = this.connexionClient(pseudo, mdp);
			ClientReservationDTO clientDTO = this.transformClientToDTO(client);
			
			chambre = hotel.post()
							.uri(uriBuilder -> uriBuilder
								.path("hotels/{hotelId}/reservation")
								.queryParam("dateDebut", dateDeb)
								.queryParam("dateFin", dateFin)
								.queryParam("clientDTO", clientDTO)
								.queryParam("chambreID", ID)
								.build(nomHotel)
								)
							.retrieve()
							.body(ChambreDTO.class);
		
		}
		
		return chambre;
	}
	

	private ClientReservationDTO transformClientToDTO(Client client) {
		String id = this.nom  + "/" + client.pseudo();
		ClientReservationDTO clientDTO = new ClientReservationDTO(id);
		
		return clientDTO;
	}
	
	public Resource askForRoomPicture(String pictureUri) {
		// Par facilité, et étant donné que tous les hotels ont accès aux mêmes ressources, je fais l'appel au premier hotel
		Entry<String, RestClient> entry = this.hotels.entrySet().iterator().next();
		RestClient hotel = entry.getValue();
		return hotel.get()
				.uri("/hotels"+pictureUri)
				.retrieve()
				.body(Resource.class);
	}
	

}
