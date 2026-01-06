package REST.hotel;

import java.util.ArrayList;

public class Client {
	private String id;
	private ArrayList<Reservation> reservations;

	
	public Client(String id) {
		this.id = id;
		reservations = new ArrayList<Reservation>();
	}

	// Accesseurs
	public String getId() {
		return id;
	}


	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	
	public void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}
	
	@Override
	public String toString() {
		return "client id : " + this.id;
	}
	
}
