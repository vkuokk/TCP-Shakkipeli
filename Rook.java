package Shakkipeli;

import java.util.HashMap;
//luokka tornille, koordinaatit lukuparina

public class Rook {
    private HashMap<Integer, Integer> pieceCoordinates = new HashMap<Integer, Integer>();

    public Rook(int xcoord, int ycoord){
        pieceCoordinates.put(xcoord, ycoord);
    }
    Rook() {

    }
}
