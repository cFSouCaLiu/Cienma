package net.examplifiers.java.base;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * <pre>
 * Les cinémas SimplonTheater vous demandent de mettre au point un logiciel simple pour permettre aux utilisateurs de
 * choisir des places de cinéma dans une salle au format fixe : 8 rangées avec dans chaque rangée 9 sièges.
 * Les utilisateurs doivent pouvoir spécifier la rangée dans laquelle ils veulent être placés ainsi que le nombre de places
 * à réserver.
 * Une fois la rangée choisie et le nombre de places spécifiées, vous devez vérifier s'il y a effectivement assez de place
 * sur la rangée :
 * - Si tel est le cas, vous affichez la représentation de la salle à l'utilisateur et proposez une nouvelle saisie.
 * - Sinon, vous spécifiez à l'utilisateur qu'il n'y a plus de place sur la rangée ou qu'il n'y en a pas assez.
 * </pre>
 * <pre>
 * SimplonTheater theaters require you to develop simple software to allow users to
 * choose cinema seats in a room with a fixed format: 8 rows with 9 seats in each row.
 * Users must be able to specify the row in which they want to be placed as well as the number of seats
 * to book.
 * Once the row has been chosen and the number of seats specified, you must check whether there is indeed enough space
 * on the row:
 * - If this is the case, you display the representation of the room to the user and propose a new entry.
 * - Otherwise, you tell the user that there is no more space on the row or that there is not enough.
 * </pre>
 * <pre>
 * Cinematografele SimplonTheater au nevoie de un soft simplu pentru a permite utilizatorilor să facă acest lucru :
 * a alege locuri de cinema într-o sală cu format fix , anume 8 rânduri cu 9 locuri pe fiecare rând.
 * Utilizatorii trebuie să poată specifica rândul în care doresc să fie plasati, precum și numărul de locuri de rezervat.
 * Odată ce rândul a fost ales și numărul de locuri specificat, trebuie să verificați dacă există într-adevăr suficient
 * spațiu pe acel rând :
 * - Dacă acesta este cazul, afișați reprezentarea sălii către utilizator și propuneți o nouă intrare tastatură (keyboard input).
 * - În caz contrar, îi spuneți utilizatorului că nu mai există spațiu în rând sau că nu este suficient.
 * Exemplu de redare (rendering) :
 * </pre>
 * <pre>
 * Combien de places voulez vous acheter ?
 * 4
 * A quelle rangée voulez vous aller ?
 * 3
 *
 *      --------------------------------------------
 *
 *
 * 0 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 1 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 2 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 3 | [ X ][ X ][ X ][ X ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 4 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 5 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 6 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 * 7 | [ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ][ _ ]
 *       0    1    2    3    4    5    6    7    8
 * </pre>
 *
 * @author <a href="mailto:emil_soucaliuc@hotmail.com">Emil Șoucaliuc</a>
 * @author <a href="mailto:Maxim.Cruceanu@outlook.com">p. Maxim</a>
 * @version 2021-04-13
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
	public static void main(final String[] args)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		proceedWithArguments(args);
		final AbstractCinemaReservation cinemaReservation = getConcreteSubclass().getDeclaredConstructor().newInstance();
		doCinemaReservation(cinemaReservation);
	}

	protected static Class<? extends AbstractCinemaReservation> getConcreteSubclass() {
		return AbstractCinemaReservation.class;
	}

	protected static final void proceedWithArguments(final String[] args) {
		if (args.length >= 3) {
			MAX_ROWS = Integer.parseInt(args[0]);
			PLACES_PER_ROW = Integer.parseInt(args[1]);
			POPULATES_CINEMA_PLACES = Boolean.parseBoolean(args[2]);
		}
	}

	protected static final boolean[][] getInitialCinemaPlacesStatus(final AbstractCinemaReservation concreteCinemaReservation) {
		final boolean[][] allPlaces = concreteCinemaReservation.createEmptyCinemaPlacesMatrix();
		if (POPULATES_CINEMA_PLACES) {
			concreteCinemaReservation.randomlyPopulateCinemaPlacesMatrix(allPlaces);
		}
		return allPlaces;
	}

	protected static final void doCinemaReservation(final AbstractCinemaReservation concreteCinemaReservation) {
		final boolean[][] allPlaces = getInitialCinemaPlacesStatus(concreteCinemaReservation);

		System.out.println("Initial configuration of cinema:");
		concreteCinemaReservation.displayCinemaPlacesLayout(allPlaces);
		System.out.println();

		concreteCinemaReservation.doReservationIfPossible(allPlaces);

		System.out.println("Final configuration of cinema:");
		concreteCinemaReservation.displayCinemaPlacesLayout(allPlaces);
	}

	protected abstract void doReservationIfPossible(final boolean[][] allPlaces);

	protected final int[] fetchReservationNumbers() {
		/* TODO: introduce i18n
		System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
		System.out.println(String.format(
				"Enter %d for the number of the desired places to indicate that no more reservations are desired, program will exit.",
				PLACES_PER_ROW_EXIT_VALUE));
		 */
		final Scanner sc = new Scanner(System.in);
		System.out.println("Combien de places voulez vous acheter ?");
		System.out.println(String.format(
				"(Entrez %d pour le nombre de places désirées pour indiquer qu'aucune réservation n'est désirée, le programme se terminera.)",
				PLACES_PER_ROW_EXIT_VALUE));
		final int numPlaces = sc.nextInt();
		System.out.println("A quelle rangée voulez vous aller ?");
		final int rowNum = sc.nextInt();
		return new int[] { rowNum, numPlaces };
	}

	protected final void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces,
			final int firstFreePlaceIndex) {
		System.out.println(String.format("Found and reserved %d consecutive places on row %d starting with place no. %d"
				+ " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, (firstFreePlaceIndex + numPlaces - 1)));
	}

	protected final void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
		System.out.println(String.format("Could not find %d consecutive places on row %d.", numPlaces, rowNum));
	}

	protected final void displayCinemaPlacesLayout(final boolean[][] allPlaces) {
		System.out.println();
		System.out.println("     --------------------------------------------");
		System.out.println();
		for (int i = 0; i < allPlaces.length; i++) {
			System.out.println();
			System.out.print(i + " | ");
			for (int j = 0; j < allPlaces[i].length; j++) {
				System.out.print((allPlaces[i][j] ? "[ _ ]" : "[ X ]"));
			}
		}
		System.out.println();
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			if (k == 0) { // the row prefix
				System.out.print("      " + k + "  ");
			} else {
				System.out.print("  " + k + "  ");
			}
		}
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
