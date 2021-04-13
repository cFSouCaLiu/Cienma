package net.examplifiers.java.base;

import java.lang.reflect.InvocationTargetException;

/**
 * Performs the cinema reservation by treating the cinema places as a bi-dimensional <code>boolean</code> array
 * and searching within the unidimensional array that is an element of the bi-dimensional one.
 *
 * @author <a href="mailto:FSoucaliuc@outlook.com">Florin Șoucaliuc</a>
 * @author <a href="mailto:emil_soucaliuc@hotmail.com">Emil Șoucaliuc</a>
 * @author <a href="mailto:Maxim.Cruceanu@outlook.com">p. Maxim</a>
 * @version 2021-04-13
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
	public static void main(final String[] args)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		/*
		 * todo: reduce the code duplication in main
		 * Simply calling AbstractCinemaReservation.main(args) will throw an InstantiationException in AbstractCinemaReservation.main().
		 */
		proceedWithArguments(args);
		final AbstractCinemaReservation cinemaReservation = getConcreteSubclass().getDeclaredConstructor().newInstance();
		doCinemaReservation(cinemaReservation);
	}

	protected static Class<? extends AbstractCinemaReservation> getConcreteSubclass() {
		return CinemaReservation.class;
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
		doReservationIfPossible(allPlaces);
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
