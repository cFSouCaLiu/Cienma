package net.examplifiers.java.base;

import java.lang.reflect.InvocationTargetException;

/**
 * Les cinémas SimplonTheater vous demandent de mettre au point un logiciel simple pour permettre aux utilisateurs de
 * choisir des places de cinéma dans une salle au format fixe : 8 rangées avec dans chaque rangée 9 sièges.
 * Les utilisateurs doivent pouvoir spécifier la rangée dans laquelle ils veulent être placés ainsi que le nombre de places
 * à réserver.
 * Une fois la rangée choisie et le nombre de places spécifiées, vous devez vérifier s'il y a effectivement assez de place
 * sur la rangée :
 * - Si tel est le cas, vous affichez la représentation de la salle à l'utilisateur et proposez une nouvelle saisie.
 * - Sinon, vous spécifiez à l'utilisateur qu'il n'y a plus de place sur la rangée ou qu'il n'y en a pas assez.
 */
public class CinemaReservation extends AbstractCinemaReservation {

	/**
	 * Can be executed with an empty array of arguments <code>args</code>
	 * or with an array of 3 <code>String</code> values: <ul>
	 * <li>
	 * 2 <code>int</code>s (the number of rows in the cinema, respectively the number of places per row in the cinema)
	 * </li>
	 * <li>
	 * and a <code>boolean</code> for indicating whether we want an initial population of the occupied places in the cinema
	 * </li>
	 * </ul>
	 */
	public static void main(String[] args)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		/*
		 * TODO: reduce the code duplication in main
		 * Simply calling AbstractCinemaReservation.main(args) will throw an InstantiationException in AbstractCinemaReservation.main().
		 */
		if (args.length >= 3) {
			MAX_ROWS = Integer.parseInt(args[0]);
			PLACES_PER_ROW = Integer.parseInt(args[1]);
			POPULATES_CINEMA_PLACES = Boolean.parseBoolean(args[2]);
		}

		final AbstractCinemaReservation cinemaReservation = getConcreteSubclass().getDeclaredConstructor().newInstance();
		final boolean[][] allPlaces = cinemaReservation.createEmptyCinemaPlacesMatrix();
		if (POPULATES_CINEMA_PLACES) {
			cinemaReservation.randomlyPopulateCinemaPlacesMatrix(allPlaces);
		}

		System.out.println("Initial configuration of cinema:");
		cinemaReservation.displayCinemaPlacesLayout(allPlaces);
		System.out.println();

		cinemaReservation.findIfReservationIsPossible(allPlaces);

		System.out.println("Final configuration of cinema:");
		cinemaReservation.displayCinemaPlacesLayout(allPlaces);
	}

	protected static Class<? extends AbstractCinemaReservation> getConcreteSubclass() {
		return CinemaReservation.class;
	}
	
	protected void findIfReservationIsPossible(final boolean[][] allPlaces) {
		final int[] rowNumAndNumPlaces = fetchReservationNumbers();
		final int rowNum = rowNumAndNumPlaces[0];
		final int numPlaces = rowNumAndNumPlaces[1];

		if (numPlaces == PLACES_PER_ROW_EXIT_VALUE) {
			System.out.println("User indicated that no more reservations are desired, exiting.");
			return;
		}
		final boolean[] rowPlaces = allPlaces[rowNum];
		boolean flagFoundReservation = false;
		for (int i = 0; i < PLACES_PER_ROW; i++) {
			if (isThisPlaceEligible(i, numPlaces, rowPlaces)) {
				displayAffirmativeReservationMessage(rowNum, numPlaces, i);
				// mark these places as occupied
				for (int j = 0; j < numPlaces; j++) {
					rowPlaces[i + j] = false;
				}
				flagFoundReservation = true; // we have found (&committed) a reservation...
				break; // ... so we do not test any more other positions as eligible!
			}
		}

		if (!flagFoundReservation) {
			displayNegativeReservationMessage(rowNum, numPlaces);
		}

		// now we run this method again, displaying a new reservation invitation and so on
		findIfReservationIsPossible(allPlaces);
	}

	protected boolean isThisPlaceEligible(final int crntPlaceIndex, final int numPlaces, final boolean[] rowPlaces) {
		if (!rowPlaces[crntPlaceIndex]) {
			return false;
		}
		if (crntPlaceIndex + numPlaces > rowPlaces.length) {
			return false;
		}
		boolean isEligible = true;
		for (int i = 0; i < numPlaces; i++) {
			isEligible &= rowPlaces[crntPlaceIndex + i];
		}
		return isEligible;
	}

}
