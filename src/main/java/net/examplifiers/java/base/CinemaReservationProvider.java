package net.examplifiers.java.base;

import java.util.Scanner;

/**
 * Cinematografele SimplonTheater au nevoie de un soft simplu pentru a permite utilizatorilor să facă acest lucru :
 * a alege locuri de cinema într-o sală cu format fix , anume 8 rânduri cu 9 locuri pe fiecare rând.
 * Utilizatorii trebuie să poată specifica rândul în care doresc să fie plasati, precum și numărul de locuri de rezervat.
 * Odată ce rândul a fost ales și numărul de locuri specificat, trebuie să verificați dacă există într-adevăr suficient
 * spațiu pe acel rând :
 * - Dacă acesta este cazul, afișați reprezentarea sălii către utilizator și propuneți o nouă intrare tastatură (keyboard input).
 * - În caz contrar, îi spuneți utilizatorului că nu mai există spațiu în rând sau că nu este suficient.
 * Exemplu de redare (rendering) :
 */
public class CinemaReservationProvider {

	public static final int TOTAL_ROWS = 8;
	public static final int PLACES_PER_ROW = 9;

	public static final boolean FREE_SEAT = true;
	public static final boolean OCCUPIED_SEAT = false;

	private int rowRequested;
	private int numSeatsRequested;
	private int startingPosition;

	public static void main(String[] args) {

		boolean[][] allPlaces_Matrix = new boolean[TOTAL_ROWS][PLACES_PER_ROW];
		boolean ExitCondition_flag;
		boolean validInput;
		boolean foundAvailablePlaces;

		final CinemaReservationProvider c = new CinemaReservationProvider();

		allPlaces_Matrix = c.initializeCinemaPlaces();

		//OPTIONAL:
		System.out.println("Initial configuration of cinema:");
		c.displayCinemaPlacesLayout(allPlaces_Matrix);
		System.out.println();

		ExitCondition_flag = false;
		while (!ExitCondition_flag) {

			validInput = c.requestAndCheckUserInput();  //ask for a reservation from the user, and check it

			if (validInput) {

				foundAvailablePlaces = c.checkAvailabilityOfPlaces(allPlaces_Matrix, c.rowRequested, c.numSeatsRequested);

				c.displayAffirmativeOrNegativeConfirmation(allPlaces_Matrix, foundAvailablePlaces);

			} else {
				ExitCondition_flag = true;
			} //exit the user-interrogation cycle, based on user termination and/or bad input

		} //end interrogation-loop

		//OPTIONAL:
		System.out.println("Final configuration of cinema:");
		c.displayCinemaPlacesLayout(allPlaces_Matrix);
		System.out.println();

	} //END of Main---------------------------------------------

	private boolean[][] initializeCinemaPlaces() {

		//Here we could have the CinemaPopulation Matrix taken from another CinemaReservationProvider object...
		// ... or randomly initialized like e.g. boolean[][] allCinemaPlacesMatrix = c.createRandomCinemaPlacesMatrix();
		// But for now, we simply write an arbitrary matrix:

		final boolean[][] allPlaces = new boolean[TOTAL_ROWS][PLACES_PER_ROW];

		for (int k = 0; k < TOTAL_ROWS; k++) {
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

		int[] userReservationInput = displayReservationInvitation();
		int rowInput = userReservationInput[0];
		int numPlacesInput = userReservationInput[1];

		if ((rowInput < 0) || (rowInput >= TOTAL_ROWS)) {
			System.out.println();
			System.out.print("BAD ROW INPUT, exiting ....");
			return false;
		}

		if ((numPlacesInput < 0) || (numPlacesInput > PLACES_PER_ROW)) {
			System.out.println();
			System.out.print("BAD INPUT FOR NUMBER OF CONTIGUOUS PLACES , exiting ....");
			return false;
		}

		if (numPlacesInput == 0) {
			System.out.println();
			System.out.print("NO MORE PLACES REQUESTED , exiting ....");
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

	private boolean checkAvailabilityOfPlaces(final boolean[][] allCinemaPlacesMatrix, int rowNum, int numPlaces) {

		boolean[] rowPlaces = allCinemaPlacesMatrix[rowNum];
		String testedSTRING = convertRowToString(rowPlaces);
		// the above is a string of the type "00X0XXX0...0X...X..0", with "X" marking occupied seats, "0" free ones

		String targetSTRING = "0";
		for (int i = 1; i < numPlaces; i++) {
			targetSTRING += "0";
		}
		//we have now generated the string "0000...0...00" of length= numPlaces (all free seats)

		startingPosition = testedSTRING
				.indexOf(targetSTRING); //we search for the first position (if ever) of numPlaces consecutive free seats

		return startingPosition != -1;  //if target string is not found (->negative index), we return false

	}

	private String convertRowToString(boolean[] rowPlaces) {
		int len = rowPlaces.length;
		String result = "";
		for (int i = 0; i < len; i++) {
			result += (rowPlaces[i]) ? "0" : "X";

		}
		return result;
	}

	private void displayAffirmativeOrNegativeConfirmation(boolean[][] allPlaces, boolean foundAvailablePlaces) {
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
		System.out.println(
				String.format("SORRY!! Could not find %d consecutive places on row %d. Please try again...", numPlaces, rowNum));
		System.out.println();
	}

	private void displayReservedPlacesLayout(final boolean[][] allPlaces, int rowReserved, int startingPosition,
			int numSeatsRequested) {

		System.out.println();
		System.out.print("Seats#:");
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		}    // we print first the table header

		for (int i = 0; i < TOTAL_ROWS; i++) {

			System.out.println();
			System.out.println(
					"=================================================================================="); //row separator, again

			System.out.print("Row " + i + ":");
			for (int j = 0; j < PLACES_PER_ROW; j++) {
				boolean placeIsReserved = (i == rowReserved) && (j >= startingPosition) && (j <= startingPosition + numSeatsRequested - 1);
				System.out.print(" " + (placeIsReserved ? " [*] " : " [ ] "));
			}
		} //end printing cycle just for newly reserved seats

		System.out.println();
		System.out.println();
	} //end display reserved-seats

	private void displayCinemaPlacesLayout(final boolean[][] allPlaces) {

		System.out.println();
		System.out.print("Seats#:");
		for (int k = 0; k < PLACES_PER_ROW; k++) {
			System.out.print("  " + k + "   ");
		}    // we print first the table header

		for (int i = 0; i < TOTAL_ROWS; i++) {

			System.out.println();
			System.out.println(
					"=================================================================================="); //this is a row separator

			System.out.print("Row " + i + ":");
			for (int j = 0; j < PLACES_PER_ROW; j++) {
				System.out.print(" " + (allPlaces[i][j] ? " [0] " : " [X] "));
			}
		} //end printing cycle for all occupied seats in cinema

		System.out.println();
		System.out.println();
	}

} // END CLASS
