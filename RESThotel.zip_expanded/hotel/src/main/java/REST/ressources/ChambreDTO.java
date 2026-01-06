package REST.ressources;

// Ressource chambreDTO
public record ChambreDTO(
		String ID,
		int nbLit,
		double prix,
		String nomHotel,
		Ville ville,
		String imageUri) {}
