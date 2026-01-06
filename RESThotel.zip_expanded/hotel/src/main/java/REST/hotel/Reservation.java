package REST.hotel;


/**
 * Représente les réservations existante pour une chambre et un client donné
 * 
 * <p> Représente une réservation sur une étendue de temps précise (dateDeb à dateFin) effectué par un client
 * pour une chambre spécifique.</p>
*/
public class Reservation {
	// Tous les attributs sont immuables. Une mauvaise réservation doit être détruite, et une nouvelle doit être créée à la place.
	private final int dateDebut;	// format AAAAMMJJ
	private final int dateFin;	// format AAAAMMJJ
	private final Chambre chambre;
	private final Client client;
	
	/**
	 * @throws IllegalArgumentException si date début > date fin OU si pas de chambre / client précisé
	 */
	public Reservation(int dateDebut, int dateFin, Chambre chambre, Client client) throws IllegalArgumentException {
		if (chambre.equals(null)) {
			throw new IllegalArgumentException("Erreur : réservation sans mention de la chambre");
		}
		
		if (client.equals(null)) {
			throw new IllegalArgumentException("Erreur : réservation sans mention de la chambre");
		}
		
		if(dateDebut > dateFin) {
			throw new IllegalArgumentException("Erreur : date de début de réservation postérieure à sa date de fin.");
		}
		this.chambre = chambre;
		this.client = client;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}

	// Accesseurs
	public int getDateDebut() {
		return dateDebut;
	}


	public int getDateFin() {
		return dateFin;
	}


	public Chambre getChambre() {
		return chambre;
	}


}
