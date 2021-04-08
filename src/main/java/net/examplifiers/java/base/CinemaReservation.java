package net.examplifiers.java.base;

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
public class CinemaReservation {

	public static final int MAX_ROWS = 8;
	public static final int PLACES_PER_ROW = 9;

	private void findIfReservationIsPossible(final boolean[][] allPlaces, int rowNum, int numPlaces) {
		boolean flagFoundReservation;

		if (rowNum < 0 && numPlaces < 0) { // valid just at the beginning of the program, afterwards these values will be input by the user
			final int[] rowNumAndNumPlaces = displayReservationInvitation();
			rowNum = rowNumAndNumPlaces[0];
			numPlaces = rowNumAndNumPlaces[1];
		}

		if (numPlaces == 0) {
			System.out.println("User indicated that no more reservations are desired, exiting.");
			return;
		}
		final boolean[] rowPlaces = allPlaces[rowNum];
		flagFoundReservation = false;
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

		// now we display new reservation invitation
		final int[] newValues = displayReservationInvitation();
		final int newRowNum = newValues[0];
		final int newNumPlaces = newValues[1];
		findIfReservationIsPossible(allPlaces, newRowNum, newNumPlaces);
	}

	private boolean isThisPlaceEligible(final int crntPlaceIndex, final int numPlaces, final boolean[] rowPlaces) {
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

	private int[] displayReservationInvitation() {
		System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
		System.out.println(
				"Enter 0 for the number of the desired places to indicate that no more reservations are desired, program will exit.");
		final Scanner sc = new Scanner(System.in);
		final int rowNum = sc.nextInt();
		final int numPlaces = sc.nextInt();
		return new int[] { rowNum, numPlaces };
	}

	private void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces, final int firstFreePlaceIndex) {
		System.out.println(String.format("Found and reserved %d consecutive places on row %d starting with place no. %d"
				+ " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, (firstFreePlaceIndex + numPlaces - 1)));
	}

	private void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
		System.out.println(String.format("Could not find %d consecutive places on row %d.", numPlaces, rowNum));
	}

	private void displayCinemaPlacesLayout(final boolean[][] allPlaces) {

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

	private boolean[][] createCinemaPlacesMatrix() {
		return new boolean[MAX_ROWS][PLACES_PER_ROW];
	}

	/** HERE I HAVE PUT A DUMMY INITIALIZATION OF THE CINEMA HALL
	 * (not really necessary for the time being...just for testing...)
	 */
	private void randomlyPopulateCinemaPlacesMatrix(final boolean[][] allPlaces) {
		for (int k = 1; k < MAX_ROWS; k++) {
			for (int l = 6; l < PLACES_PER_ROW; l++) {
				allPlaces[k][l] = true;
			}
		}

		allPlaces[1][1] = allPlaces[1][2] = allPlaces[1][3] = true;
		allPlaces[3][0] = allPlaces[3][1] = allPlaces[3][6] = allPlaces[3][5] = true;
	}

	public static void main(String[] args) {
		final CinemaReservation cinemaReservation = new CinemaReservation();
		// final int[] rowNumAndNumPlaces = cinemaReservation.displayReservationInvitation();
		// System.out.println(String.format("Entered rowNum = %d and numPlaces = %d.", rowNumAndNumPlaces[0], rowNumAndNumPlaces[1]));

		// final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];
		final boolean[][] allPlaces = cinemaReservation.createCinemaPlacesMatrix();
		cinemaReservation.randomlyPopulateCinemaPlacesMatrix(allPlaces);

		System.out.println("Initial configuration of cinema:");
		cinemaReservation.displayCinemaPlacesLayout(allPlaces);
		System.out.println();

		cinemaReservation.findIfReservationIsPossible(allPlaces, -1,
				-1); // we impose absurd negative entries, to force the initial appeal to user input

		System.out.println("Final configuration of cinema:");
		cinemaReservation.displayCinemaPlacesLayout(allPlaces);
	}

}
