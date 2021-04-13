package net.examplifiers.java.base;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

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
public abstract class AbstractCinemaReservation {

	/**
	 * The number of the desired places to indicate that no more reservations are desired, in which case the program will exit.
	 */
	public static final int PLACES_PER_ROW_EXIT_VALUE = 0;

	/**
	 * The number of rows in the cinema (default: 8); can be modified using the 1st argument of the {@link #main(String[])} method
	 */
	protected static int MAX_ROWS = 8;
	/**
	 * The number of places per row in the cinema (default: 9); can be modified using the 2nd argument of the {@link #main(String[])} method
	 */
	protected static int PLACES_PER_ROW = 9;
	/**
	 * Flag to indicate whether we want an initial population of the occupied places in the cinema (default: <code>true</code>); can be modified using the 3rd argument of the {@link #main(String[])} method
	 */
	protected static boolean POPULATES_CINEMA_PLACES = true;

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
		return AbstractCinemaReservation.class;
	}
	protected abstract void findIfReservationIsPossible(final boolean[][] allPlaces);

	protected final int[] fetchReservationNumbers() {
		System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
		System.out.println(String.format(
				"Enter %d for the number of the desired places to indicate that no more reservations are desired, program will exit.",
				PLACES_PER_ROW_EXIT_VALUE));
		final Scanner sc = new Scanner(System.in);
		final int rowNum = sc.nextInt();
		final int numPlaces = sc.nextInt();
		return new int[] { rowNum, numPlaces };
	}

	protected final void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces, final int firstFreePlaceIndex) {
		System.out.println(String.format("Found and reserved %d consecutive places on row %d starting with place no. %d"
				+ " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, (firstFreePlaceIndex + numPlaces - 1)));
	}

	protected final void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
		System.out.println(String.format("Could not find %d consecutive places on row %d.", numPlaces, rowNum));
	}

	protected final void displayCinemaPlacesLayout(final boolean[][] allPlaces) {
		System.out.println();
		System.out.print("Seats#:");
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		}

		for (int i = 0; i < allPlaces.length; i++) {
			System.out.println();
			System.out.println("==================================================================================");
			System.out.print("Row " + i + ":");
			for (int j = 0; j < allPlaces[i].length; j++) {
				System.out.print(" " + (allPlaces[i][j] ? " [0] " : " [X] "));
			}
		}
		System.out.println();
	}

	protected final boolean[][] createEmptyCinemaPlacesMatrix() {
		return new boolean[MAX_ROWS][PLACES_PER_ROW];
	}

	/**
	 * Here I have put a dummy initialization of the cinema hall
	 * (not really necessary for the time being &mdash; just for testing...)
	 */
	protected final void randomlyPopulateCinemaPlacesMatrix(final boolean[][] allPlaces) {
		for (int k = 1; k < MAX_ROWS; k++) {
			for (int l = 6; l < PLACES_PER_ROW; l++) {
				allPlaces[k][l] = true;
			}
		}

		allPlaces[1][1] = allPlaces[1][2] = allPlaces[1][3] = true;
		allPlaces[3][0] = allPlaces[3][1] = allPlaces[3][6] = allPlaces[3][5] = true;
	}

}
