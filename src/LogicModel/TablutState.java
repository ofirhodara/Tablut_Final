
package LogicModel;

import static GameView.View.ANSI_BLUE;
import static GameView.View.ANSI_RED;
import static GameView.View.ANSI_RESET;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import ofirtablut.OfirTablut.Player;

/**
 *
 * @author Ofir
 */
public class TablutState implements IState {

    private int countTurns = 0;

    // Bits board 
    private Player currentPlayer;
    private final BitSet whitesPawns, kingPawn, blackPawns;
    private final BitSet board;

    // Consts
    private static final int WHITE_INIT_COUNT = 8;
    private static final int BLACK_INIT_COUNT = 16;
    private static final double WON_GRADE = 1000;

     private final int COUNT_NEIGHBORS = 4;
    private final int MIN_WIN_MOVES = 5;
    
    // evaluation Functions Influencers
    private final String KING_PROTECTION = "White Pawns protects The king";
    private final String WHITE_COUNT = "White Pawns Alive";
    private final String BLACK_COUNT = "Black Pawns Alive";   
    private final String KING_ADVANCE_CORNERS = "Min manhattan distance To escapes Cells";
    private final String DANGER_KING = "Black pawns threats the king";
    private final String KING_PATHS = "Free paths to king to reach the corner";
    private final String CELL_WEIGHTS = "Strategic cells Weights";
    
    /**
     * Deep copy constructor
     */
    public TablutState(Player currentPlayer, BitSet whitesPawns, BitSet kingPawn, BitSet blackPawns, int turns) {
        this.currentPlayer = (Player) currentPlayer;
        this.whitesPawns = (BitSet) whitesPawns.clone();
        this.kingPawn = (BitSet) kingPawn.clone();
        this.blackPawns = (BitSet) blackPawns.clone();
        this.countTurns = turns;

        // Create Board from all the pieces
        this.board = new BitSet(IState.BOARD_DIMENSION);
        this.board.or(whitesPawns);
        this.board.or(blackPawns);
        this.board.or(kingPawn);
    }

    /**
     * build initial game constructor
     */
    public TablutState() {
        // The white player is always start the game 
        this.currentPlayer = Player.WHITE;

        // Create the initial board of the game 
        this.whitesPawns = (BitSet) GamePositions.initWhite.clone();
        this.blackPawns = (BitSet) GamePositions.initBlack.clone();
        this.kingPawn = (BitSet) GamePositions.initKing.clone();

        // Create Board from all the pieces
        this.board = new BitSet(IState.BOARD_DIMENSION);
        updateBoard();
    }

    //*************************** IState Functions ******************************************/
    @Override
    public int getCountTurns() {
        return countTurns;
    }

    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean isPlayerPieceInPos(int bitPos) {
        if (this.currentPlayer == Player.BLACK) {
            return this.blackPawns.get(bitPos);
        }
        return this.whitesPawns.get(bitPos) || this.kingPawn.get(bitPos);
    }

    @Override
    public Player getEnemy() {
        return currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
    }

    @Override
    public void changeCurrentPlayer() {
        this.currentPlayer = getEnemy();
    }

    @Override
    public BitSet getBlackPawns() {
        return this.blackPawns;
    }

    @Override
    public BitSet getWhitePawns() {
        return this.whitesPawns;
    }

    @Override
    public BitSet getKing() {
        return this.kingPawn;

    }
   /**
    * update this.board with all the sub-boards bits
    */
    private void updateBoard() {
        this.board.or(whitesPawns);
        this.board.or(blackPawns);
        this.board.or(kingPawn);
    }

    @Override
    public IState clone() {
        return new TablutState(this.currentPlayer, this.whitesPawns, this.kingPawn, this.blackPawns, this.countTurns);
    }

    @Override
    public BitSet getBoard() {
        return this.board;
    }

    /**
     * @return boolean value according if the game is Draw
     */
    @Override
    public boolean isDraw() {
        return this.countTurns >= TablutState.MAX_TURNS;
    }

    /**
     * @return boolean value according if one of the Player Has Won the game
     */
    @Override
    public boolean isWinningState() {
        return blackHasWon() || whiteHasWon();
    }

    /**
     * The function check if the black player is the winner
     *
     * @return if black won return true else return false
     */
    @Override
    public boolean blackHasWon() {
        if (kingPawn.isEmpty()) {
            return true;
        }
        return currentPlayer == Player.WHITE && !hasMove();
    }

    /**
     * The function check if the white player is the winner
     *
     * @return if white won return true else return false
     */
    @Override
    public boolean whiteHasWon() {
        if (kingPawn.intersects(GamePositions.escape)) {
            return true;
        }
        return currentPlayer == Player.BLACK && !hasMove();
    }

    /**
     * @return True if the current Player has At least one Move, else return
     * False
     */
    private boolean hasMove() {
        BitSet boardOfPlayer = this.currentPlayer == Player.WHITE ? this.whitesPawns : this.blackPawns;

        // get all pawns Moves
        for (int i = boardOfPlayer.nextSetBit(0); i >= 0; i = boardOfPlayer.nextSetBit(i + 1)) {
            if (BoardMoves.getMovesForPawn(i, this).isEmpty() == false) {
                return true;
            }
        }

        // if the current player is White so check King Moves
        if (this.currentPlayer == Player.WHITE) {
            if (this.getAvailableKingMoves().isEmpty() == false) {
                return true;
            }
        }

        return false;
    }

    
    /**
     * find all possible Moves of the current Player according to the Tablut
     * Rules
     *
     * @return list of all the available moves for this currentPlayer
     */
    @Override
    public List<Action> getAvailablePawnMoves() {

        BitSet boardOfPlayer = currentPlayer == Player.WHITE ? this.whitesPawns : this.blackPawns;

        List<Action> moves = new ArrayList<>(boardOfPlayer.cardinality() * 10);

        // if the current player is White so add King Moves 
        // for Move Ordering i put them in the start of the List
        if (currentPlayer == Player.WHITE) {
            moves.addAll(getAvailableKingMoves());
        }
        
        // get all pawns Moves
        for (int i = boardOfPlayer.nextSetBit(0); i >= 0; i = boardOfPlayer.nextSetBit(i + 1)) {
            moves.addAll(BoardMoves.getMovesForPawn(i, this));
        }
      
        return moves;
    }

    /**
     * @return list of all the possible actions for the king to move in this state
     */
    @Override
    public List<Action> getAvailableKingMoves() {

        int kingPosition = this.kingPawn.nextSetBit(0);
        // Only to be sure 
        if (kingPosition == -1) {
            return new ArrayList<>();
        }
        return BoardMoves.getMovesForPawn(kingPosition, this);

    }

    /**
     * @return the evaluation of the board for the current player
     */
    @Override
    public double getHuristic() {
        if (isDraw()) {
            return 0;
        }

        if (currentPlayer == Player.BLACK) {
            return getHuristicForBlack();
        }
        return getHuristicForWhite();
    }

    @Override
    public boolean isGameOver() {
        return isWinningState() || isDraw();
    }

    /**
     * perform the action of the current Player on this board
     *
     * @param action move to perform
     * @return CapturedPawns object with details about the captured pawn eaten
     * in this action
     */
    @Override
    public CapturedPawns performAction(Action action) {
        BitSet captures = BoardMoves.getCapturedPawns(action, this);
        
        int fromIndex = action.getFrom();
        int toIndex = action.getTo();
        
        int kingCaptruedPos = -1;
        if (currentPlayer == Player.BLACK) {

            // this is move of black player
            blackPawns.clear(fromIndex);
            blackPawns.set(toIndex);

            int kingPos = this.kingPawn.nextSetBit(0);

            // check if a king is captured
            if (captures.get(kingPos)) {
                this.kingPawn.clear();
                kingCaptruedPos = kingPos;
            }

            this.whitesPawns.andNot(captures);

        } else {

            // this is move of white player
            // check if the player move the king or regular pawn
            BitSet played = this.whitesPawns;

            if (this.kingPawn.get(fromIndex)) {
                played = this.kingPawn;
            }
            played.clear(fromIndex);
            played.set(toIndex);
            blackPawns.andNot(captures);

        }

        // change the player
        this.changeCurrentPlayer();

        // update the main board
        this.board.clear(fromIndex);
        this.board.set(toIndex);
        this.board.andNot(captures);

        // inc the number of moves played
        this.countTurns++;
        return new CapturedPawns(captures, kingCaptruedPos);
    }

    /**
     * The function cancel "action" by undo it
     * @param action action that made in nega Max algorithm and needs to be deleted
     */
    @Override
    public void undoAction(Action action) {
        BitSet pawns = this.blackPawns;
        if (this.currentPlayer == Player.BLACK) {
            // check if is it the king or regular white Pawn
            pawns = this.whitesPawns;
            if (this.kingPawn.get(action.getTo())) {
                pawns = this.kingPawn;
            }
        }
        pawns.clear(action.getTo());
        pawns.set(action.getFrom());

        this.board.clear(action.getTo());
        this.board.set(action.getFrom());
    }

    /**
     * cancel all the captures made represented in captures mask
     * @param captures mask of captures made while searching in the tree games
     */
    @Override
    public void undoCaptures(CapturedPawns captures) {
        BitSet capturedPawns = captures.getCaptured();
        if (this.currentPlayer == Player.BLACK) {
            this.blackPawns.or(capturedPawns);
        } else {
            int kingPos = captures.getKingPos();
            // check if the king captured
            if (kingPos != -1) {
                this.kingPawn.set(kingPos);
                capturedPawns.clear(kingPos);
            }
            this.whitesPawns.or(capturedPawns);
        }
        updateBoard();
    }

    /**
     * Cancel the move taken and return the board to the state before
     *
     * @param Captures CapturedPawns object with the details about the captures
     * @param move the move to cancel
     */
    @Override
    public void cancelMove(CapturedPawns Captures, Action move) {

        undoAction(move);

        // set the captured to '1' again
        undoCaptures(Captures);

        // change the player back
        changeCurrentPlayer();

        // dec the move counter
        countTurns--;
    }

    //****************************** Hetistics Function Utills ***********************************//
    
    //  the weights of the different parameters to evaluate the game state
    
    private final HashMap<String, Integer> whiteHuristics = new HashMap<String, Integer>() {
        {
            put(WHITE_COUNT, 20);
            put(BLACK_COUNT, 12);
            put(KING_ADVANCE_CORNERS, 10);
            put(KING_PROTECTION, 10);
            put(DANGER_KING, -16);
            put(KING_PATHS, 32);           
            put(CELL_WEIGHTS, 5);           
        }
    };

    private final HashMap<String, Integer> blackHuristics = new HashMap<String, Integer>() {
        {
            put(WHITE_COUNT, 16);
            put(BLACK_COUNT, 16);
            put(KING_ADVANCE_CORNERS, -10);
            put(KING_PROTECTION, -10);
            put(DANGER_KING, 16);
            put(KING_PATHS, -32);           
            put(CELL_WEIGHTS, 5);          
        }
    };


    /** 
     * @return the evaluation grade of this Current state to the white Player
     */
    public double getHuristicForWhite() {     
        // Check if is it possible to win according to the count of turns made
        if (this.countTurns >= MIN_WIN_MOVES) {
            
            if (this.whiteHasWon()) {
                return WON_GRADE - this.countTurns;
            }

            if (this.blackHasWon()) {
                return (-1 * WON_GRADE) + this.countTurns;
            }
        }        
        
        // get the bit number of the king 
        int kingPos = this.kingPawn.nextSetBit(0);

        int blackCount = this.blackPawns.cardinality();
        int whiteCount = this.whitesPawns.cardinality();
        
        // Pawns Counter
        double whiteAlive = whiteCount / (double) WHITE_INIT_COUNT;
        double blackEaten = (BLACK_INIT_COUNT - blackCount) / (double) BLACK_INIT_COUNT;

        // king danger
        int pawnsToEatKing = BoardMoves.pawnsToEatKing(kingPawn);

        double enemyNearKing = BoardMoves.dangerToKing(kingPos, blackPawns) / (double) pawnsToEatKing;
        
        // kings protection
        double protectingKing = BoardMoves.protectKing(kingPos, this.whitesPawns) / (double) COUNT_NEIGHBORS;
      
        // if this is the start of the game Consider the sum Weights of the cells
        double strategicCells = this.countTurns < 15 ? differenceSumWeights(whiteCount,blackCount) : 0;      
        
        // Attempts to win the Game, divide by 1.3 because One way to win is not always 
        // promise a Win 
        double winWays = freePathsToKing() / 1.3;

        int xKingPos = kingPos % EDGE_SIZE, yKingPos = kingPos / EDGE_SIZE;

        int minDistance = BoardMoves.minDistanceToCorner(xKingPos, yKingPos, this.board);

        // MAX_DISTANCE_CORNER - minDistance
        // ---------------------------------------------  = 1 - minDistance/MAX_DISTANCE_CORNER
        //     MAX_DISTANCE_CORNER    
        
        double kingsAdvancement = 1 - (minDistance / GamePositions.MAX_DISTANCE_CORNER);

        // evaluate according to the weights of each function 
        double eval = blackEaten * whiteHuristics.get(BLACK_COUNT);
        eval += whiteAlive * whiteHuristics.get(WHITE_COUNT);
        eval += protectingKing * whiteHuristics.get(KING_PROTECTION);
        eval += enemyNearKing * whiteHuristics.get(DANGER_KING);   
        eval += strategicCells * whiteHuristics.get(CELL_WEIGHTS);
        eval += kingsAdvancement * whiteHuristics.get(KING_ADVANCE_CORNERS);
                
        // From turn 35 and up, the white needs to be more Aggressive SO Multipy the Weights of KING_PATHS by 1.2
        if (this.countTurns > 35) {
            eval += winWays  * whiteHuristics.get(KING_PATHS) * 1.2;
        } else {
            eval += winWays  * whiteHuristics.get(KING_PATHS);
        }
        
        return eval;
    }
    
     /** 
     * @return the evaluation grade of this Current state to the black Player
     */
    public double getHuristicForBlack() {
         // Check if is it possible to win according to the count of turns made
        if (this.countTurns >= MIN_WIN_MOVES) {
            if (this.blackHasWon()) {
                return WON_GRADE - this.countTurns;
            }

            if (this.whiteHasWon()) {

                return (-1 * WON_GRADE) + this.countTurns;
            }
        }
        
        // get the bit number of the king 
        int kingPos = this.kingPawn.nextSetBit(0);

        int blackCount = this.blackPawns.cardinality();
        int whiteCount = this.whitesPawns.cardinality();
        
         // Pawns Counter
        double blackAlive = blackCount / BLACK_INIT_COUNT;
        double whiteEaten = (WHITE_INIT_COUNT - whiteCount) / (double) WHITE_INIT_COUNT;
        
        // Danger to king 
        double enemyNearKing = BoardMoves.dangerToKing(kingPos, blackPawns) / (double) BoardMoves.pawnsToEatKing(this.kingPawn);

        // Attempts to win the Game, divide by 1.3 because One way to win is not always 
        // promise a Win 
        double winWays = (double) freePathsToKing() / 1.3;

        int xKingPos = kingPos % EDGE_SIZE, yKingPos = kingPos / EDGE_SIZE;

        int minDistance = BoardMoves.minDistanceToCorner(xKingPos, yKingPos, this.board);
        
        // MAX_DISTANCE_CORNER - minDistance
        // ---------------------------------------------  = 1 - minDistance/MAX_DISTANCE_CORNER
        //     MAX_DISTANCE_CORNER    
        
        double kingsAdvancement = 1 - (minDistance / GamePositions.MAX_DISTANCE_CORNER);

          // if this is the start of the game Consider the sum Weights of the cells
        double strategicCells = this.countTurns < 15 ? differenceSumWeights(whiteCount,blackCount) : 0;
     
        
        double protectingKing = BoardMoves.protectKing(kingPos, this.whitesPawns) / (double) COUNT_NEIGHBORS;

        double eval = blackAlive * blackHuristics.get(BLACK_COUNT);
        
        eval += whiteEaten * blackHuristics.get(WHITE_COUNT);

        eval += winWays * blackHuristics.get(KING_PATHS);

        eval -= strategicCells * blackHuristics.get(CELL_WEIGHTS);
        
        eval += protectingKing * blackHuristics.get(KING_PROTECTION);
        
        eval += kingsAdvancement * blackHuristics.get(KING_ADVANCE_CORNERS);

       // From turn 35 and up, the black needs to be more Aggressive SO
       // get more higher grade one they cause danger to the King 
        if (this.countTurns > 35) {
            eval += enemyNearKing * blackHuristics.get(DANGER_KING) * 1.2;
        } else {
            eval += enemyNearKing * blackHuristics.get(DANGER_KING);
        }
        return eval;
    }
    


    /**
     * The function calculate the Difference between The Sum Strategic Weights
     * of the white Pawns and the Black Pawns
     * @param whiteCount number of white Pawns Alive
     * @param blackCount number of black Pawns Alive
     * @return  the difference of sums weighted between 0 to 1
     */
    
    private double differenceSumWeights(int whiteCount ,int blackCount) {
      
        double blackWeights = this.sumWeights(Player.BLACK) / (blackCount * GamePositions.AVG_WEIGHTS_BLACK);
        double whiteWeights = this.sumWeights(Player.WHITE) / (whiteCount * GamePositions.AVG_WEIGHTS_WHITE);
        return whiteWeights - blackWeights;
    }

    /**
     * Calculate the sum Of the weights of the places that the Pawns of the
     * player is on
     *
     * @param player The player to Calculate his Pawns board Positions
     * @return The sum of all the weights
     */  
    private int sumWeights(Player player) {
        int sum = 0;
        int[] cellsGrade = (player == Player.WHITE) ? GamePositions.whiteWeights : GamePositions.blackWeights;

        BitSet bitBoard = (player == Player.WHITE) ? this.whitesPawns : this.blackPawns;

        for (int i = bitBoard.nextSetBit(0); i != -1; i = bitBoard.nextSetBit(i + 1)) {
            sum += cellsGrade[i];
        }
        return sum;
    }

    /**
     * @return count of free path to king to get to one of the corners Cells and
     * win the Game
     */
    private int freePathsToKing() {
        List<Action> movesKing = this.getAvailableKingMoves();
       
        BitSet kingDestinations = new BitSet(BOARD_DIMENSION);
        
        for (Action move : movesKing) {
            // set bit in Mask
            kingDestinations.set(move.getTo());
        }
        
        kingDestinations.and(GamePositions.escape);
        // count the number of 1 bits
        return kingDestinations.cardinality();
    }

    
    private int countOnStratagicForPlayer() {

        if (this.currentPlayer == Player.BLACK) {
            return BitSetHelper.cloneAndResult(GamePositions.strategicBlack, this.blackPawns).cardinality();
        }
        return BitSetHelper.cloneAndResult(GamePositions.strategicWhite, this.whitesPawns).cardinality();

    } 
    
    /**
     * The function print the State to the User for debugging
     */
    
    @Override
    public void printState() {
        System.out.println("The board");
        int place = 0;
        char player;
        // loop over all the board cells
        for (int i = 0; i < IState.BOARD_DIMENSION; i++) {
            player = ' ';
            if (this.blackPawns.get(i)) {
                System.out.print(ANSI_RED);
                player = 'B';
            } else {
                System.out.print(ANSI_BLUE);
                if (this.kingPawn.get(i)) {
                    player = 'K';
                } else {
                    if (this.whitesPawns.get(i)) {
                        player = 'W';
                    }
                }
            }
            if (place < 10) {
                System.out.print(" ");
            }
            System.out.print(place + " [" + player + "] ");
            System.out.print(ANSI_RESET);
            if (i % 9 == 8) {
                System.out.println("");
            }
            place++;
        }
        System.out.println("\n\n");
    }

}
