package web.service.client;

import ui.model.Ville;

public record ChambreDTO(
		String ID,
		int nbLit,
		double prix,
		String nomHotel,
		Ville ville,
		String imageUri) {}
