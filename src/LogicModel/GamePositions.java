/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicModel;

import java.util.Arrays;
import java.util.BitSet;
import ofirtablut.OfirTablut.Piece;

/**
 *
 * @author Ofir
 */

public enum GamePositions {

    A1("A1"), B1("B1"), C1("C1"), D1("D1"), E1("E1"), F1("F1"), G1("G1"), H1("H1"), I1("I1"),
    A2("A2"), B2("B2"), C2("C2"), D2("D2"), E2("E2"), F2("F2"), G2("G2"), H2("H2"), I2("I2"),
    A3("A3"), B3("B3"), C3("C3"), D3("D3"), E3("E3"), F3("F3"), G3("G3"), H3("H3"), I3("I3"),
    A4("A4"), B4("B4"), C4("C4"), D4("D4"), E4("E4"), F4("F4"), G4("G4"), H4("H4"), I4("I4"),
    A5("A5"), B5("B5"), C5("C5"), D5("D5"), E5("E5"), F5("F5"), G5("G5"), H5("H5"), I5("I5"),
    A6("A6"), B6("B6"), C6("C6"), D6("D6"), E6("E6"), F6("F6"), G6("G6"), H6("H6"), I6("I6"),
    A7("A7"), B7("B7"), C7("C7"), D7("D7"), E7("E7"), F7("F7"), G7("G7"), H7("H7"), I7("I7"),
    A8("A8"), B8("B8"), C8("C8"), D8("D8"), E8("E8"), F8("F8"), G8("G8"), H8("H8"), I8("I8"),
    A9("A9"), B9("B9"), C9("C9"), D9("D9"), E9("E9"), F9("F9"), G9("G9"), H9("H9"), I9("I9");

   
    /**
     * Represents the cell name
     */
    private final String name;

    /**
     * Constructor method with given name argument
     *
     * @param name represents the cell name
     */
    
    GamePositions(String name) {
        this.name = name;
    }
    
     /**
     * The function find the piece in specific index in the starter board game  
     * @param bitPos the index of wanted piece
     * @return the Piece in bitPos of the starting board of Tablut
     */
    public static Piece getPawn(int bitPos) {          
        
        if (GamePositions.initBlack.get(bitPos)) {
            return Piece.BLACK;
        }
        
        if (GamePositions.initKing.get(bitPos)) {
            return Piece.KING;
        }
        
        if (GamePositions.initWhite.get(bitPos)) {
            return Piece.WHITE;
        }
        return Piece.EMPTY;
    
    }
        

      /**
      * Array of indexes of camp cells
      */
     private static final int[] campCells = {
                                                            D1.ordinal(),   E1.ordinal(),   F1.ordinal(),
                                                                            E2.ordinal(),

            A4.ordinal(),                                                                                                                   I4.ordinal(),
            A5.ordinal(),   B5.ordinal(),                                                                                              H5.ordinal(),I5.ordinal(),
            A6.ordinal(),                                                                                                                   I6.ordinal(),

                                                                            E8.ordinal(),
                                                            D9.ordinal(),   E9.ordinal(),   F9.ordinal()
    };

    /**
     * Array of ints representing all the escape cells on the board
     */
    public static final int[] escapeCells = {
                            B1.ordinal(),   C1.ordinal(),                                                   G1.ordinal(),   H1.ordinal(),
            A2.ordinal(),                                                                                                                   I2.ordinal(),
            A3.ordinal(),                                                                                                                   I3.ordinal(),



            A7.ordinal(),                                                                                                                   I7.ordinal(),
            A8.ordinal(),                                                                                                                   I8.ordinal(),
                            B9.ordinal(),   C9.ordinal(),                                                   G9.ordinal(),   H9.ordinal()
    };

    
    
    /**
     * Array of ints representing the throne/castle cell on the board
     */
    private static final int[] castleCells = {




                                                                            E5.ordinal()




    };

    
    /**
     * Array of ints representing all the obstacles cells on the board
     */
    private static final int[] obstacleCells = {
                                                            D1.ordinal(),                   F1.ordinal(),
                                                                            E2.ordinal(),

            A4.ordinal(),                                                                                                                   I4.ordinal(),
                            B5.ordinal(),                              E5.ordinal(),                                   H5.ordinal(),
            A6.ordinal(),                                                                                                                   I6.ordinal(),

                                                                            E8.ordinal(),
                                                            D9.ordinal(),                 F9.ordinal()
    };

    
    
    
    /**
     * Array of ints representing all the king's special cells on the board
     */
    private static final int[] kingSurroundedCells= {



                                                                            E4.ordinal(),
                                                            D5.ordinal()       ,         F5.ordinal(),
                                                                            E6.ordinal(),



    };
      
    private static final int[] kingInE4SurroundedCells = {
                                                                            E3.ordinal(),
                                                            D4.ordinal(),                   F4.ordinal(),
    };
    

    /**
     * Array of ints representing the D5 special capture
     */
    private static final int[] kingInD5SurroundedCells = {
                                                            D4.ordinal(),
                                            C5.ordinal(),
                                                            D6.ordinal(),
    };

    /**
     * Array of ints representing the E6 special capture
     */
    private static final int[] kingInE6SurroundedCells = {
                                                            D6.ordinal(),                   F6.ordinal(),
                                                                            E7.ordinal(),


    };

    /**
     * Array of ints representing the F5 special capture
     */
    private static final int[] kingInF5SurroundedCells = {
                                                                                            F4.ordinal(),
                                                                                                               G5.ordinal(),
                                                                                            F6.ordinal(),
    };
    
    
     
    private static final int[] specailKingCaptures = {
                                                                           E4.ordinal(),
                                                          D5.ordinal(),E5.ordinal(), F5.ordinal(),
                                                                           E6.ordinal()
    };
    
    

    
   
    
    /**************************************** Starting Positions**********************************************/
    
    private static final int[] blackStartingBoard = {

                                                            D1.ordinal(),E1.ordinal(), F1.ordinal(),
                                                                            E2.ordinal(),

            A4.ordinal(),                                                                                                                            I4.ordinal(),
            A5.ordinal(),   B5.ordinal(),                                                                                   H5.ordinal(),  I5.ordinal(),
            A6.ordinal(),                                                                                                                            I6.ordinal(),

                                                                            E8.ordinal(),
                                                            D9.ordinal(),E9.ordinal(), F9.ordinal()

    };

    /**
     * Array of ints representing the starting position of white pawns
     */
    private static final int[] whiteStartingBoard = {



                                                                            E3.ordinal(),
                                                                            E4.ordinal(),
                                       C5.ordinal(), D5.ordinal(),                  F5.ordinal(),   G5.ordinal(),
                                                                            E6.ordinal(),
                                                                            E7.ordinal(),



    };

    /**
     * Array of ints representing the starting position of the king
     */
    private static final int[] kingStartingBoard = {





                                                                            E5.ordinal()





    };
    

    public static final int[] whiteWeights = {
            2,  2,  2,  0,  0,  0,  2,  2,  2,
            2,  1,  1,  4,  0,  4,  1,  1,  2,
            2,  1,  2,  2,  3,  2,  2,  1,  2,
            0,  4,  2,  1,  3,  1,  2,  4,  0,
            0,  0,  3,  3,  0,  3,  3,  0,  0,
            0,  4,  2,  1,  3,  1,  2,  4,  0,
            2,  1,  2,  2,  3,  2,  2,  1,  2,
            2,  1,  1,  4,  0,  4,  1,  1,  2,
            2,  2,  2,   0, 0,  0,  2,  2,  2
    };
    
    
    public static final int[] blackWeights = {
            1,  3,  2,  3,  3,  3,  2,  3,  1,
            3,  2,  5,  1,  3,  1,  5,  2,  3,
            2,  5,  1,  5,  2,  5,  1,  5,  2,
            3,  1,  5,  1,  2,  1,  5,  1,  3,
            3,  3,  2,  2,  0,  2,  2,  3,  3,
            3,  1,  5,  1,  2,  1,  5,  1,  3,
            2,  5,  1,  5,  2,  5,  1,  5,  2,
            3,  2,  5,  1,  3,  1,  5,  2,  3,
            1,  3,  2,  3,  3,  3,  2,  3,  1
    };
    
    
      
     
     private static final int[] strategicWhiteStart = {
                                                                                D2.ordinal(),             F2.ordinal(),
                                                                B4.ordinal(),                                             H4.ordinal(),
                                                                                    
                                                                B6.ordinal(),                                             H6.ordinal(),
                                                                              D8.ordinal(),             F8.ordinal()
             
    };    
    
     
    
      private static final int[] rhombus = {
                          C2.ordinal(),       G2.ordinal(),
        B3.ordinal(),                                           H3.ordinal(),

        B7.ordinal(),                                           H7.ordinal(),
                          C8.ordinal(),       G8.ordinal()
    };
      
      
    public static final BitSet strategicBlack = BitSetHelper.newFromPositions(rhombus);   
    public static final BitSet strategicWhite = BitSetHelper.newFromPositions(strategicWhiteStart); 
    
    // Masks of special Cells on the board
    public static final BitSet escape = BitSetHelper.newFromPositions(escapeCells);
    public static final BitSet camps = BitSetHelper.newFromPositions(campCells);
    public static final BitSet obstacles = BitSetHelper.newFromPositions(obstacleCells);           
    public static final BitSet kingSurrounded= BitSetHelper.newFromPositions(kingSurroundedCells);       
   
    // Special king captures Masks
    public static final BitSet kingF5Surrounded= BitSetHelper.newFromPositions(kingInF5SurroundedCells);       
    public static final BitSet kingE6Surrounded= BitSetHelper.newFromPositions(kingInE6SurroundedCells);       
    public static final BitSet kingD5Surrounded= BitSetHelper.newFromPositions(kingInD5SurroundedCells);       
    public static final BitSet kingE4Surrounded= BitSetHelper.newFromPositions(kingInE4SurroundedCells);       
    public static final BitSet specailKingCapture = BitSetHelper.newFromPositions(specailKingCaptures);  
      
    // masks of starting Positions
    public static final BitSet initWhite = BitSetHelper.newFromPositions(whiteStartingBoard);
    public static final BitSet initKing = BitSetHelper.newFromPositions(kingStartingBoard);
    public static final BitSet initBlack = BitSetHelper.newFromPositions(blackStartingBoard); 
    
    // Contants about the board
    public static final int CENTER_POSITION = 40;
    public static final int MAX_DISTANCE_CORNER = 7;
      
   public static final double AVG_WEIGHTS_BLACK = 2.65;
   public static final double AVG_WEIGHTS_WHITE = 2.41;
   
}
