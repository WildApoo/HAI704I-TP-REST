package REST.ressources;

public record ClientReservationDTO(String ID) {
	/*
	 * Cette classe est nécessaire afin de faire la liaison entre Hotel et Agence. Il est impossible de partager directement
	 * la classe client puisqu'il y'a une part de logique métier qui ne doit pas être divulgué. Cette classe de transition
	 * sert donc de format d'échange. Il convient de créer dans Agence et dans Hotel des méthodes de conversion vers le client
	 * de chacun.
	 */

}
