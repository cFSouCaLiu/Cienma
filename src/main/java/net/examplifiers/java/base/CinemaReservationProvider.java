package net.examplifiers.java.base;

import java.util.Scanner;

/**
 * This variant solves the problem of finding <code>N</code> consecutive vacancies in a row by turning the line into a string
 * like &quot;00XX00XXXX .... X0..XXX ... 0 .... X00&quot;, where the &quot;X&quot; marks the occupied places,
 * and &quot;0&quot; the free ones.<br />
 * Then we create the target string &quot;000 ... 00&quot; of length <code>N</code> and we use directly the facilities of the
 * <code>String</code> class (namely <code>indexOf</code>) to immediately find out at what position we find the target string in
 * the &quot;string&quot; (if ever).<br />
 * For this purpose, we introduced a method like <code>convertRowToString()</code>, which, with a little refinement, could be used
 * to print the row (in the current variant we did not do so, it was too much work), as well as for other similar finer features,
 * e.g. to suggest to the user several starting positions for the <code>N</code> places, if such a thing exists ... and so on.<br />
 * But this is just a &quot;toy&quot; solution, it works for the current situation, where the matrix of places is not big, but in
 * the general case we don&apos;t think it is advisable.
 * 
 * @author <a href="mailto:Maxim.Cruceanu@outlook.com">p. Maxim</a>
 * @version 2021-04-09
 */
public class CinemaReservationProvider {

	public static final int MAX_ROWS = 8;
	public static final int PLACES_PER_ROW = 9;

	public static final boolean FREE_SEAT = true;
	public static final boolean OCCUPIED_SEAT = false;
	private static final String FREE_SEAT_STRING = "0";
	private static final String OCCUPIED_SEAT_STRING = "X";

	private int rowRequested;
	private int numSeatsRequested;
	private int startingPosition;

	public static void main(String[] args) {
		final CinemaReservationProvider c = new CinemaReservationProvider();
		final boolean[][] allPlacesMatrix = c.initializeCinemaPlaces();
		boolean exitConditionFlag = false;
		boolean validInput;
		boolean foundAvailablePlaces;

		// OPTIONAL:
		System.out.println("Initial configuration of cinema:");
		c.displayCinemaPlacesLayout(allPlacesMatrix);
		System.out.println();

		while (!exitConditionFlag) {
			validInput = c.requestAndCheckUserInput(); // ask for a reservation from the user, and check it
			if (validInput) {
				foundAvailablePlaces = c.checkAvailabilityOfPlaces(allPlacesMatrix, c.rowRequested, c.numSeatsRequested);
				c.displayAffirmativeOrNegativeConfirmation(allPlacesMatrix, foundAvailablePlaces);
			} else {
				exitConditionFlag = true;
			} // exit the user-interrogation cycle, based on user termination and/or bad input
		} // end interrogation-loop

		// OPTIONAL:
		System.out.println("Final configuration of cinema:");
		c.displayCinemaPlacesLayout(allPlacesMatrix);
		System.out.println();

	} // END of main ---------------------------------------------

	private boolean[][] initializeCinemaPlaces() {
		// Here we could have the CinemaPopulation Matrix taken from another CinemaReservationProvider object...
		// ... or randomly initialized like e.g. boolean[][] allCinemaPlacesMatrix = c.createRandomCinemaPlacesMatrix();
		// But for now, we simply write an arbitrary matrix:
		final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];

		for (int k = 0; k < MAX_ROWS; k++) {
			for (int l = 0; l < PLACES_PER_ROW; l++) {
				allPlaces[k][l] = FREE_SEAT;
			}
		}

		allPlaces[0][0] = allPlaces[0][2] = allPlaces[0][3] = OCCUPIED_SEAT;
		allPlaces[1][1] = allPlaces[1][2] = allPlaces[1][3] = OCCUPIED_SEAT;
		allPlaces[2][1] = allPlaces[2][2] = allPlaces[2][3] = allPlaces[2][4] = OCCUPIED_SEAT;
		allPlaces[3][0] = allPlaces[3][1] = allPlaces[3][6] = allPlaces[3][7] = OCCUPIED_SEAT;
		allPlaces[4][2] = allPlaces[4][5] = allPlaces[4][6] = allPlaces[4][7] = OCCUPIED_SEAT;
		allPlaces[5][0] = allPlaces[5][1] = allPlaces[5][2] = allPlaces[5][3] = OCCUPIED_SEAT;
		allPlaces[6][0] = allPlaces[6][6] = allPlaces[6][7] = allPlaces[6][8] = OCCUPIED_SEAT;

		return allPlaces;
	}

	private boolean requestAndCheckUserInput() {
		final int[] userReservationInput = displayReservationInvitation();
		final int rowInput = userReservationInput[0];
		final int numPlacesInput = userReservationInput[1];

		if ((rowInput < 0) || (rowInput >= MAX_ROWS)) {
			System.out.println();
			System.err.print("BAD ROW INPUT, exiting ....");
			return false;
		}

		if ((numPlacesInput < 0) || (numPlacesInput > PLACES_PER_ROW)) {
			System.out.println();
			System.err.print("BAD INPUT FOR NUMBER OF CONTIGUOUS PLACES, exiting ....");
			return false;
		}

		if (numPlacesInput == 0) {
			System.out.println();
			System.out.print("NO MORE PLACES REQUESTED, exiting ....");
			return false;
		}

		rowRequested = rowInput;
		numSeatsRequested = numPlacesInput;
		return true;
	}

	private int[] displayReservationInvitation() {
		System.out.println();
		System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
		System.out.println(
				"(Enter 0 for the number of the desired places to indicate that no more reservations are desired, program will exit.)");
		System.out.print("YOUR INPUT HERE PLEASE:> ");

		final Scanner sc = new Scanner(System.in);
		final int rowNum = sc.nextInt();
		final int numPlaces = sc.nextInt();
		return new int[] { rowNum, numPlaces };
	}

	private boolean checkAvailabilityOfPlaces(final boolean[][] allCinemaPlacesMatrix, final int rowNum, final int numPlaces) {
		final boolean[] rowPlaces = allCinemaPlacesMatrix[rowNum];
		final String testedString = convertRowToString(rowPlaces);
		// the above is a string of the type "00X0XXX0...0X...X..0", with "X" marking occupied seats, "0" free ones

		final StringBuilder targetStringBuilder = new StringBuilder(FREE_SEAT_STRING);
		for (int i = 1; i < numPlaces; i++) {
			targetStringBuilder.append(FREE_SEAT_STRING);
		}
		final String targetString = targetStringBuilder.toString();
		// now we have generated the string "0000...0...00" of length = numPlaces (all free seats)

		// we search for the first position (if ever) of numPlaces consecutive free seats
		startingPosition = testedString.indexOf(targetString);

		return (startingPosition >= 0); // if target string is not found (-> negative index), we return false
	}

	private String convertRowToString(final boolean[] rowPlaces) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0, len = rowPlaces.length; i < len; i++) {
			result.append((rowPlaces[i]) ? FREE_SEAT_STRING : OCCUPIED_SEAT_STRING);
		}
		return result.toString();
	}

	private void displayAffirmativeOrNegativeConfirmation(final boolean[][] allPlaces, final boolean foundAvailablePlaces) {
		if (foundAvailablePlaces) {
			displayAffirmativeReservationMessage(rowRequested, numSeatsRequested, startingPosition);
			displayReservedPlacesLayout(allPlaces, rowRequested, startingPosition, numSeatsRequested);
			for (int k = startingPosition; k <= startingPosition + numSeatsRequested - 1; k++) {
				allPlaces[rowRequested][k] = OCCUPIED_SEAT;
			}
		} else {
			displayNegativeReservationMessage(rowRequested, numSeatsRequested);
		}
	}

	private void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces, final int firstFreePlaceIndex) {
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(String.format("BINGO!!! Found and reserved %d consecutive places on row %d starting with place no. %d"
				+ " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, (firstFreePlaceIndex + numPlaces - 1)));
		System.out.println();
	}

	private void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(String.format("SORRY!! Could not find %d consecutive places on row %d. Please try again...",
				numPlaces, rowNum));
		System.out.println();
	}

	private void displayReservedPlacesLayout(final boolean[][] allPlaces, final int rowReserved, final int startingPosition,
			final int numSeatsRequested) {
		System.out.println();
		System.out.print("Seats#:");
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		} // we print first the table header

		for (int i = 0; i < MAX_ROWS; i++) {
			System.out.println();
			// row separator, again
			System.out.println("==================================================================================");

			System.out.print("Row " + i + ":");
			for (int j = 0; j < PLACES_PER_ROW; j++) {
				boolean placeIsReserved = (i == rowReserved) && (j >= startingPosition) && (j <= startingPosition + numSeatsRequested - 1);
				System.out.print(" " + (placeIsReserved ? " [*] " : " [ ] "));
			}
		} // end printing cycle just for newly reserved seats

		System.out.println();
		System.out.println();
	} // end display reserved-seats

	private void displayCinemaPlacesLayout(final boolean[][] allPlaces) {
		System.out.println();
		System.out.print("Seats#:");
		// first we print the table header
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		}

		for (int i = 0; i < MAX_ROWS; i++) {
			System.out.println();
			// this is a row separator
			System.out.println("==================================================================================");

			System.out.print("Row " + i + ":");
			for (int j = 0; j < PLACES_PER_ROW; j++) {
				System.out.print(" " + (allPlaces[i][j] ? " [" + FREE_SEAT_STRING + "] " : " [" + OCCUPIED_SEAT_STRING + "] "));
			}
		} // end printing cycle for all occupied seats in cinema

		System.out.println();
		System.out.println();
	}

}
