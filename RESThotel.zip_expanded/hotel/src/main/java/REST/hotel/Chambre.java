package REST.hotel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import REST.ressources.ChambreDTO;

/**
 * Représente une chambre appartenant à un Hotel et pouvant être réservé par un client
 * 
 * <p> Représente un modèle de chambre appartenant à un hotel et pouvant être réservé par un client.
 * Ces objets chambres sont créés durant la création d'un Hotel. Une chambre de peut pas exister sans hotel
 * et ne peut faire l'objet d'un transfert vers un autre hotel.
 * 
 * Les réservations de de la chambre sont des objets Reservation stockés dans la collection reservations.</p>
 * 
 */
public class Chambre {
	// L'ID préciser explicitement ici permet de débugguer plus facilement en associant un nom unique et clair à chaque chambre.
	// Son affectation est automatique via compteurID qui est incrémenté de 1 à chaque création d'objet chambre.
	private static int compteurID=0;
	private String ID;
	private int nbLit;
	private double prix;
	private ArrayList<Reservation> reservations;
	private String pathImage;
	
	// Immuable. La chambre est toujours lié à cet hotel. 
	private final Hotel hotel;
	
	// Constructeur
	public Chambre(int nbLit, double prix, Hotel hotel) {
		ID = String.valueOf(Chambre.compteurID);
		Chambre.compteurID++;
		this.nbLit = nbLit;
		this.prix = prix;
		this.reservations = new ArrayList<Reservation>();
		this.hotel = hotel;
			
		int x = (int) (Math.random() * 5) + 1;
		this.pathImage = "/photo/chambre" + x + ".jpg";
	}
	
	// Accesseurs
	public double getPrix() {
		return this.prix;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public int getNbLit() {
		return this.nbLit;
	}
	
	public Hotel getHotel() {
		return hotel;
	}
	
	// Méthodes
	
	// méthode spécifique à REST
	public ChambreDTO getDTO() {
		return new ChambreDTO(this.ID, this.nbLit, this.prix, this.getHotel().getNom(), this.getHotel().getVille(), this.pathImage );
	}
	public ChambreDTO getDTO(double prix) {
		return new ChambreDTO(this.ID, this.nbLit, prix, this.getHotel().getNom(), this.getHotel().getVille(), this.pathImage );
	}
	
	
	// Retourne Vrai si la chambre est disponible > aucune de ces réservations n'est dans l'intervalle de temps renseigné en paramètre.
	public boolean estDisponible(int dateDebut, int dateFin) {
		boolean estDispo = true;
		int index = 0;
		
		while(index < this.reservations.size() && estDispo) {
			Reservation resa = this.reservations.get(index);
			// est ce que ma date proposé est en dehor des période déjà existantes
			estDispo = ( dateDebut > resa.getDateFin() || dateFin < resa.getDateDebut() );
			index++;
		}
		
		return estDispo;
	}
	
	// Retourne Vrai si le prix de la chambre est compris dans l'intervalle de prix renseigné en paramètre.
	public boolean prixComprisEntre(double prixMin, double prixMax) {
		return (this.prix >= prixMin && this.prix <= prixMax);
	}
	
	// Retourne Vrai si le prix de la chambre peut accueillir le nombre de personne renseigné en paramètre.
	public boolean peutAcccueillir(int nbPersonne) {
		return (this.nbLit>= nbPersonne);
	}
	
	// Permet de créer une réservation de la chambre pour la période indiquée en paramètre SI la chambre et disponible SINON IllegalArgumentException
	public void reservation(int dateDebut, int dateFin, Client client) throws IllegalArgumentException {
		if(!this.estDisponible(dateDebut, dateFin)) {
			throw new IllegalArgumentException("Erreur : la chambre ne peut pas être réservée car non disponible à cette période.");
		}
		this.reservations.add(new Reservation(dateFin, dateFin, this, client));

	}
	
	@Override
	public String toString() {
		return "Chambre n°" + this.ID + ". nbLit : " + this.nbLit + " et prix : " +this.prix;
		
	}
	

}
