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
    
    void findIfReservationIsPossible(final int rowNum, final int numPlaces) {
        final boolean[][] allPlaces = new boolean[MAX_ROWS][PLACES_PER_ROW];
        final boolean[] rowPlaces = allPlaces[rowNum];
        for (int i = 0; i < PLACES_PER_ROW; i++) {
            if (isThisPlaceEligible(i, numPlaces, rowPlaces)) {
                displayAffirmativeReservationMessage(rowNum, numPlaces, i);
                final int[] newValues = displayReservationInvitation();
                final int newRowNum = newValues[0];
                final int newNumPlaces = newValues[1];
                // findIfReservationIsPossible(newRowNum, newNumPlaces);
            }
        }
        displayNegativeReservationMessage(rowNum, numPlaces);
    }
    
    boolean isThisPlaceEligible(final int crntPlaceIndex, final int numPlaces, final boolean[] rowPlaces) {
        if (!rowPlaces[crntPlaceIndex]) {
            return false;
        }
        if (crntPlaceIndex + numPlaces > rowPlaces.length) { // todo recheck
            return false;
        }
        boolean isEligible = true;
        for (int i = 0; i < numPlaces; i++) {
            isEligible &= rowPlaces[crntPlaceIndex + i]; // todo recheck
        }
        return isEligible;
    }
    
    int[] displayReservationInvitation() {
        System.out.println("Please enter the number of the row and, after a space, the number of the desired places");
        final Scanner sc = new Scanner(System.in);
        final int rowNum = sc.nextInt();
        final int numPlaces = sc.nextInt();
        return new int[] {rowNum, numPlaces};
    }
    
    void displayAffirmativeReservationMessage(final int rowNum, final int numPlaces, final int firstFreePlaceIndex) {
        System.out.println(String.format("Found %d consecutive places on row %d starting with place no. %"
                + " and ending with place no. %d.", numPlaces, rowNum, firstFreePlaceIndex, firstFreePlaceIndex + numPlaces - 1));
    }
    
    void displayNegativeReservationMessage(final int rowNum, final int numPlaces) {
        System.out.println(String.format("Could not find %d consecutive places on row %d.", numPlaces, rowNum));
    }

    public static void main(String[] args) {
        
    }
}
