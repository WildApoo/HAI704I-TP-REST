package REST.hotel;

import java.util.ArrayList;

import REST.ressources.ChambreDTO;
import REST.ressources.ClientReservationDTO;
import REST.ressources.Ville;

public class HotelWebServiceHotelImpl implements HotelWebService {
	
	private Hotel hotel;
	
	public HotelWebServiceHotelImpl(String nom, int nbEtoile, Ville ville, ArrayList<ArrayList<Integer>> chambres) {
		hotel = new Hotel(nom, nbEtoile, ville, chambres);
	}
	
	@Override
	public String getNom() {
		return hotel.getNom();
	}

	@Override
	public ArrayList<ChambreDTO> possedeChambre(int dateDeb, int dateFin, double prixMin, double prixMax, int nbPersonne, ClientReservationDTO clientDTO) {
		return hotel.possedeChambre(dateDeb, dateFin, prixMin, prixMax, nbPersonne, clientDTO);
	}
	
	@Override
	public boolean isLocated(Ville ville) {
		return hotel.isLocated(ville);
	}


	@Override
	public ChambreDTO reservationBis(int dateDebut, int dateFin, ClientReservationDTO clientDTO, String ID) {
		return hotel.reservationBis(dateDebut, dateFin, clientDTO, ID);
	}
	
}
