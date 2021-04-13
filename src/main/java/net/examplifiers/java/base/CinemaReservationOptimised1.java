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
public class CinemaReservationOptimised1 {

    public static final int MAX_ROWS = 8;
    public static final int PLACES_PER_ROW = 9;
    public static final boolean FREE = false;
    public static final boolean OCCUPIED = true;
    private int rowReserved; // this stores the current row where we have validated a reservation
    private int startingPosition; //...of the reservation
    private int endPosition; //...of the reservation
    private int nextPositionAdvance ; // this stores how much should I go forward when scanning the row for (contiguous) blocks of free or occupied seats

    private void findIfReservationIsPossible(final boolean[][] allPlaces, int rowNum, int numPlaces) {
        boolean flagFoundReservation;

        if (rowNum < 0 && numPlaces < 0) { //valid just at the begining of the program, afterwards these values will be input by the user
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
        int indexTested = 0; //we start with the leftmost seat

        while (indexTested < PLACES_PER_ROW) {
            if (isThisPlaceEligible(indexTested, numPlaces, rowPlaces)) {
                displayAffirmativeReservationMessage(rowNum, numPlaces, indexTested);

                rowReserved = rowNum;
                startingPosition = indexTested;
                endPosition = indexTested + numPlaces - 1;
                displayCinemaPlacesLayout(allPlaces, true, rowReserved, startingPosition, endPosition); //print the reserved-seats layout to the user

                // mark these places as occupied
                for (int j = 0; j < numPlaces; j++) {
                    rowPlaces[indexTested + j] = false;
                }


                flagFoundReservation = true; //we have found (&committed) a reservation...
                break; //...so we do not test any more other positions as elligible!
            } //end if
            indexTested += nextPositionAdvance;
        } //end while

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
        /**if (!rowPlaces[crntPlaceIndex]) {
            return false;
        }
        if (crntPlaceIndex + numPlaces > rowPlaces.length) { // todo recheck
            return false;
        }
        boolean isEligible = true;
        for (int i = 0; i < numPlaces; i++) {
            isEligible &= rowPlaces[crntPlaceIndex + i]; // todo recheck
        }
        return isEligible; */

        boolean statusOfIndexTested = rowPlaces[crntPlaceIndex];

        if (crntPlaceIndex + numPlaces > PLACES_PER_ROW) {
            return false;}

        if (statusOfIndexTested==FREE){
            for (int i =0; i<numPlaces; i++) {
                if (rowPlaces[crntPlaceIndex+i]) { nextPositionAdvance= i+1;}
                else { return  false;}
            }
            return true;


        }//end if FREE

        if(statusOfIndexTested==OCCUPIED) {
            for (int j =0; j<PLACES_PER_ROW-crntPlaceIndex; j++) {
                if (!rowPlaces[crntPlaceIndex+j]) { nextPositionAdvance= j+1;}
                else {break;}
            }


        }//end if OCCUPIED
        return false;




    }

    private int[] displayReservationInvitation() {
        System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
        System.out.println("(Enter 0 for the number of the desired places to indicate that no more reservations are desired, program will exit.)");
        System.out.print("YOUR INPUT HERE PLIZ:> ");
        final Scanner sc = new Scanner(System.in);
        final int rowNum = sc.nextInt();
        final int numPlaces = sc.nextInt();
        return new int[]{rowNum, numPlaces};
    }

    private void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces, final int firstFreePlaceIndex) {
        System.out.println(String.format("Found and reserved %d consecutive places on row %d starting with place no. %d"
                + " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, (firstFreePlaceIndex + numPlaces - 1)));
    }

    private void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
        System.out.println(String.format("Could not find %d consecutive places on row %d.", numPlaces, rowNum));
    }

    private void displayCinemaPlacesLayout(final boolean[][] allPlaces, boolean flagReservedOnly, int rowReserved, int startingPosition, int endPosition) {

        //flagReservedOnly=true signals that I should only print the currently reserved seats; otherwise, I print the whole picture of the cinema occupation
        System.out.println();
        System.out.print("Seats#:");
        for (int k = 0; k < PLACES_PER_ROW; k++) {
            System.out.print("  " + k + "   ");
        }    // we print first the table header

        if (!flagReservedOnly) {

            for (int i = 0; i < MAX_ROWS; i++) {

                System.out.println();
                System.out.println("=================================================================================="); //this is a row separator

                System.out.print("Row " + i + ":");
                for (int j = 0; j < PLACES_PER_ROW; j++) {
                    System.out.print(" " + (allPlaces[i][j] ? " [0] " : " [X] "));
                }
            } //end printing cycle for all occupied seats in cinema
        }
        else {
            for (int i = 0; i < MAX_ROWS; i++) {

                System.out.println();
                System.out.println("=================================================================================="); //row separator, again

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

    private boolean[][] createCinemaPlacesMatrix() {
        final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];

        //HERE I HAVE PUT A DUMMY INITIALIZATION OF THE CINEMA HALL...just for testing...

        for (int k = 1; k < MAX_ROWS; k++) {
            for (int l = 6; l < PLACES_PER_ROW; l++) {
                allPlaces[k][l] = true;
            }
        }

        allPlaces[1][1] = allPlaces[1][2] = allPlaces[1][3] = true;
        allPlaces[3][0] = allPlaces[3][1] = allPlaces[3][6] = allPlaces[3][5] = true;

        return allPlaces;
    }


    private void randomlyPopulateCinemaPlacesMatrix(final boolean[][] allPlaces) {
        // TODO (not really necessary for the time being)
    }

    public static void main(String[] args) {
        final CinemaReservationOptimised1 cinemaReservation = new CinemaReservationOptimised1();
//        final int[] rowNumAndNumPlaces = cinemaReservation.displayReservationInvitation();
//        System.out.println(String.format("Entered rowNum = %d and numPlaces = %d.", rowNumAndNumPlaces[0], rowNumAndNumPlaces[1]));

        // final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];
        final boolean[][] allPlaces = cinemaReservation.createCinemaPlacesMatrix();

        // cinemaReservation.randomlyPopulateCinemaPlacesMatrix(allPlaces);

        System.out.println("Initial configuration of cinema:");
        cinemaReservation.displayCinemaPlacesLayout(allPlaces, false, cinemaReservation.rowReserved, cinemaReservation.startingPosition, cinemaReservation.endPosition);
        System.out.println();

        cinemaReservation.findIfReservationIsPossible(allPlaces, -1, -1); //we impose absurd negative entries, to force the initial appeal to user input

        System.out.println("Final configuration of cinema:");
        cinemaReservation.displayCinemaPlacesLayout(allPlaces, false, cinemaReservation.rowReserved, cinemaReservation.startingPosition, cinemaReservation.endPosition);

    }
}
