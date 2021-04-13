package net.examplifiers.java.base;

import java.lang.reflect.InvocationTargetException;

/**
 * Performs the cinema reservation by treating the cinema places as a bi-dimensional <code>boolean</code> array
 * and searching within the unidimensional array that is an element of the bi-dimensional one.
 *
 * @author <a href="mailto:Maxim.Cruceanu@outlook.com">p. Maxim</a>
 * @version 2021-04-08
 */
public class CinemaReservationBooleanArrayOptimised extends AbstractCinemaReservation {

	public static final int MAX_ROWS = 8;
	public static final int PLACES_PER_ROW = 9;
	public static final boolean FREE = false;
	public static final boolean OCCUPIED = true;
	private int rowReserved; // this stores the current row where we have validated a reservation
	private int startingPosition; //...of the reservation
	private int endPosition; //...of the reservation
	private int nextPositionAdvance; // this stores how much should I go forward when scanning the row for (contiguous) blocks of free or occupied seats

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
		/*
		 * todo: reduce the code duplication in main
		 * Simply calling AbstractCinemaReservation.main(args) will throw an InstantiationException in AbstractCinemaReservation.main().
		 */
		/*
		proceedWithArguments(args);
		final AbstractCinemaReservation cinemaReservation = getConcreteSubclass().getDeclaredConstructor().newInstance();
		doCinemaReservation(cinemaReservation);
		 */
		proceedWithArguments(args);
		final AbstractCinemaReservation cinemaReservation = getConcreteSubclass().getDeclaredConstructor().newInstance();
		final boolean[][] allPlaces = getInitialCinemaPlacesStatus(cinemaReservation);
		//	}
		//	public static void mainOLD(String[] args) {
		//		final CinemaReservationBooleanArrayOptimised cinemaReservation = new CinemaReservationBooleanArrayOptimised();
		//		//        final int[] rowNumAndNumPlaces = cinemaReservation.displayReservationInvitation();
		//		//        System.out.println(String.format("Entered rowNum = %d and numPlaces = %d.", rowNumAndNumPlaces[0], rowNumAndNumPlaces[1]));
		//
		//		// final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];
		//		final boolean[][] allPlaces = cinemaReservation.createEmptyCinemaPlacesMatrix();
		//
		//		// cinemaReservation.randomlyPopulateCinemaPlacesMatrix(allPlaces);

		System.out.println("Initial configuration of cinema:");
		((CinemaReservationBooleanArrayOptimised) cinemaReservation).displayCinemaPlacesEnrichedLayout(allPlaces, false,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).rowReserved,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).startingPosition,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).endPosition);
		System.out.println();

		cinemaReservation.doReservationIfPossible(allPlaces);

		System.out.println("Final configuration of cinema:");
		((CinemaReservationBooleanArrayOptimised) cinemaReservation).displayCinemaPlacesEnrichedLayout(allPlaces, false,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).rowReserved,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).startingPosition,
				((CinemaReservationBooleanArrayOptimised) cinemaReservation).endPosition);
	}

	protected static Class<? extends AbstractCinemaReservation> getConcreteSubclass() {
		return CinemaReservationBooleanArrayOptimised.class;
	}

	protected void doReservationIfPossible(final boolean[][] allPlaces) {
		final int[] rowNumAndNumPlaces = fetchReservationNumbers();
		final int rowNum = rowNumAndNumPlaces[0];
		final int numPlaces = rowNumAndNumPlaces[1];
		if (numPlaces == PLACES_PER_ROW_EXIT_VALUE) {
			System.out.println("User indicated that no more reservations are desired, exiting.");
			return;
		}

		final boolean[] rowPlaces = allPlaces[rowNum];
		boolean flagFoundReservation = false;
		int indexTested = 0; // we start with the leftmost seat

		while (indexTested < PLACES_PER_ROW) {
			if (isThisPlaceEligible(indexTested, numPlaces, rowPlaces)) {
				displayAffirmativeReservationMessage(rowNum, numPlaces, indexTested);

				rowReserved = rowNum;
				startingPosition = indexTested;
				endPosition = indexTested + numPlaces - 1;
				displayCinemaPlacesEnrichedLayout(allPlaces, true, rowReserved, startingPosition,
						endPosition); // print the reserved-seats layout to the user

				// mark these places as occupied
				for (int j = 0; j < numPlaces; j++) {
					rowPlaces[indexTested + j] = false;
				}

				flagFoundReservation = true; //we have found (&committed) a reservation...
				break; // ... so we do not test any more other positions as eligible!
			} // end if
			indexTested += nextPositionAdvance;
		} // end while

		if (!flagFoundReservation) {
			displayNegativeReservationMessage(rowNum, numPlaces);
		}

		// now we run this method again, displaying a new reservation invitation and so on
		doReservationIfPossible(allPlaces);
	}

	private boolean isThisPlaceEligible(final int crntPlaceIndex, final int numPlaces, final boolean[] rowPlaces) {
		boolean statusOfIndexTested = rowPlaces[crntPlaceIndex];

		if (crntPlaceIndex + numPlaces > PLACES_PER_ROW) {
			return false;
		}

		if (statusOfIndexTested == FREE) {
			for (int i = 0; i < numPlaces; i++) {
				if (rowPlaces[crntPlaceIndex + i]) {
					nextPositionAdvance = i + 1;
				} else {
					return false;
				}
			}
			return true;

		} // end if FREE

		if (statusOfIndexTested == OCCUPIED) {
			for (int j = 0; j < PLACES_PER_ROW - crntPlaceIndex; j++) {
				if (!rowPlaces[crntPlaceIndex + j]) {
					nextPositionAdvance = j + 1;
				} else {
					break;
				}
			}
		} // end if OCCUPIED
		return false;
	}

	private void displayCinemaPlacesEnrichedLayout(final boolean[][] allPlaces, final boolean flagReservedOnly,
			final int rowReserved, final int startingPosition, final int endPosition) {
		// flagReservedOnly=true signals that I should only print the currently reserved seats; otherwise, I print the whole picture of the cinema occupation
		System.out.println();
		System.out.print("Seats#:");
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		} // we print first the table header

		if (!flagReservedOnly) {

			for (int i = 0; i < MAX_ROWS; i++) {

				System.out.println();
				System.out.println(
						"=================================================================================="); //this is a row separator

				System.out.print("Row " + i + ":");
				for (int j = 0; j < PLACES_PER_ROW; j++) {
					System.out.print(" " + (allPlaces[i][j] ? " [0] " : " [X] "));
				}
			} // end printing cycle for all occupied seats in cinema
		} else {
			for (int i = 0; i < MAX_ROWS; i++) {

				System.out.println();
				System.out.println(
						"=================================================================================="); //row separator, again

				System.out.print("Row " + i + ":");
				for (int j = 0; j < PLACES_PER_ROW; j++) {
					boolean placeIsReserved = (i == rowReserved) && (j >= startingPosition) && (j <= endPosition);
					System.out.print(" " + (placeIsReserved ? " [*] " : " [ ] "));
				}
			} //end printing cycle just for newly reserved seats
		}

		System.out.println();
		System.out.println();
	}

}
