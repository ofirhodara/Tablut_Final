package LogicModel;

import java.util.ArrayList;
import java.util.BitSet;
import ofirtablut.OfirTablut.Player;
import java.util.List;

/**
 *
 * @author Ofir
 */
public class BoardMoves {

    /**
     * the function build a list with all the possible Moves for the pawn in
     * pawnPosition in the Game state
     *
     * @param pawnPosition positions of the pawn on Tablut board
     * @param state state of the game of tablut
     * @return a list of the possible actions for this specific pawn
     */
    public static List<Action> getMovesForPawn(int pawnPosition, IState state) {

        // 16 is the maximum amount of moves we can possibly have:
        // it ensures no further allocations are needed      
        List<Action> moves = new ArrayList<>(16);

        BitSet forbiddenCells = (BitSet) state.getBoard().clone();

        // The castle cell is forbidden to step on To Every pieces
        forbiddenCells.set(GamePositions.E5.ordinal());

        // Camps may or may not be forbidden to step on
        if (state.getCurrentPlayer() == Player.WHITE) {
            // Whites can never go on the camps
            forbiddenCells.or(GamePositions.camps);
        } else {
            // Blacks can't go back on the camps
            // but if he right now in camp, he can move inside the camp
            if (!GamePositions.camps.get(pawnPosition)) {
                forbiddenCells.or(GamePositions.camps);
            }
        }

        // Check for moves in all directions       
        // Up
        for (int cell = pawnPosition - IState.EDGE_SIZE; cell >= 0; cell -= IState.EDGE_SIZE) {
            // When we find a forbidden cell, we can stop to check in this direction
            if (forbiddenCells.get(cell)) {
                break;
            }
            moves.add(new Action(pawnPosition, cell));
        }

        // Down
        for (int cell = pawnPosition + IState.EDGE_SIZE; cell < IState.BOARD_DIMENSION; cell += IState.EDGE_SIZE) {
            // When we find a forbidden cell, we can stop
            if (forbiddenCells.get(cell)) {
                break;
            }

            moves.add(new Action(pawnPosition, cell));

        }

        // Left: check only if the pawn isn't on column A
        if (pawnPosition % IState.EDGE_SIZE != 0) {

            // Make sure we don't end up out of the board
            // or one row above in column I
            for (int cell = pawnPosition - 1; cell >= 0 && cell % IState.EDGE_SIZE != IState.EDGE_SIZE - 1; cell--) {

                // When we find a forbidden cell, we can stop
                if (forbiddenCells.get(cell)) {
                    break;
                }

                moves.add(new Action(pawnPosition, cell));

            }

        }

        // Right: check only if the pawn isn't on column I
        if (pawnPosition % IState.EDGE_SIZE != IState.EDGE_SIZE - 1) {

            // Make sure we don't end up out of the board
            // or one row below in column A
            for (int cell = pawnPosition + 1; cell < IState.BOARD_DIMENSION && cell % IState.EDGE_SIZE != 0; cell++) {

                // When we find a forbidden cell, we can stop
                if (forbiddenCells.get(cell)) {
                    break;
                }
                moves.add(new Action(pawnPosition, cell));
            }
        }

        return moves;
    }

    /**
     * find all the captures of a move taken in the game
     *
     * @param move last action taken in the state
     * @param state state of the game of tablut
     * @return bit board mask of all the positions of captured pawns taken in
     * move
     */
    public static BitSet getCapturedPawns(Action move, IState state) {

        if (state.getCurrentPlayer() == Player.BLACK) {
            return getCapturesForBlack(move, state);
        }
        return getCapturesForWhite(move, state);
    }

    /**
     * get all capturers for move of white Player
     *
     * @param move the move taken
     * @param state the current board State
     * @return bit set mask of all the opponent killed pawns
     */
    private static BitSet getCapturesForWhite(Action move, IState state) {

        BitSet blacks = state.getBlackPawns();
        BitSet whites = (BitSet) state.getWhitePawns().clone();

        // add the king to the whites Bit Set
        whites.or(state.getKing());

        // clear the moving piece from the attacker board
        whites.clear(move.getFrom());

        return getNormalCaptures(move.getTo(), whites, blacks);

    }

    /**
     * get all capturers for move of black Player
     *
     * @param move the move taken
     * @param state the current board State
     * @return bit set mask of all the opponent killed pawns
     */
    private static BitSet getCapturesForBlack(Action move, IState state) {

        BitSet blacks = (BitSet) state.getBlackPawns().clone();

        // clear the Moving piece
        blacks.clear(move.getFrom());

        BitSet king = (BitSet) state.getKing().clone();
        BitSet whites = (BitSet) state.getWhitePawns().clone();

        // move the piece      
        blacks.set(move.getTo());

        // Normal captures 
        BitSet capturesPawns = getNormalCaptures(move.getTo(), blacks, whites);

        // Check if the king needs a special capture in four size
        int kingPosition = king.nextSetBit(0);

        if (king.intersects(GamePositions.specailKingCapture)) {
            if (kingPosition == GamePositions.E5.ordinal()) {
                // the king is in the castle, check the four cells Surrounded him
                if (BitSetHelper.cloneAndResult(blacks, GamePositions.kingSurrounded).cardinality() == 4) {
                    capturesPawns.set(kingPosition);
                }
            } else {

                BitSet result = new BitSet(IState.BOARD_DIMENSION);
                BitSet enemyMask = null;
                // need three captures....
                switch (kingPosition) {
                    // BitSetPosition.E4.ordinal() = 31
                    case 31:
                        enemyMask = GamePositions.kingE4Surrounded;
                        break;

                    // BitSetPosition.D5.ordinal() = 39
                    case 39:
                        enemyMask = GamePositions.kingD5Surrounded;

                        break;
                    // BitSetPosition.E6.ordinal() = 49
                    case 49:
                        enemyMask = GamePositions.kingE6Surrounded;
                        break;
                    // GamePositions.F5.ordinal() = 41
                    case 41:
                        enemyMask = GamePositions.kingF5Surrounded;
                        break;
                }
                if (BitSetHelper.cloneAndResult(blacks, enemyMask).cardinality() == 3) {
                    capturesPawns.set(kingPosition);
                }
            }
        } else {
            BitSet capturesKing = getNormalCaptures(move.getTo(), blacks, king);
            capturesPawns.or(capturesKing);
        }

        return capturesPawns;
    }

    /**
     * The function find the captures for an action in destination position and
     * build mask of that captured pawns
     *
     * @param position destination position of an action
     * @param attack attackers bitSet
     * @param opponent opponent bitSet
     * @return mask of that captured pawns
     */
    private static BitSet getNormalCaptures(int position, BitSet attack, BitSet opponent) {

        // Bit Board represent all the potensial captured pieces to the defence
        BitSet captured = new BitSet(IState.BOARD_DIMENSION);

        //Check UP
        // Check if the postion is not in the first 2 rows
        if (position / IState.EDGE_SIZE > 1) {
            int oneUpCell = position - IState.EDGE_SIZE;
            if (opponent.get(oneUpCell)) {
                int twoUpCell = oneUpCell - IState.EDGE_SIZE;
                if (attack.get(twoUpCell) || GamePositions.obstacles.get(twoUpCell)) {
                    captured.set(oneUpCell);
                }
            }
        }

        //Check DOWN
        // Check if the postion is not in the last 2 rows
        if (position / IState.EDGE_SIZE < IState.EDGE_SIZE - 2) {
            int oneDownCell = position + IState.EDGE_SIZE;
            if (opponent.get(oneDownCell)) {
                int twoDownCell = oneDownCell + IState.EDGE_SIZE;
                if (attack.get(twoDownCell) || GamePositions.obstacles.get(twoDownCell)) {
                    captured.set(oneDownCell);
                }
            }
        }

        //Check LEFT
        // Check if the postion is not in the left 2 columns
        if (position % IState.EDGE_SIZE > 1) {

            int oneLeftCell = position - 1;
            if (opponent.get(oneLeftCell)) {

                int twoLeftCell = oneLeftCell - 1;
                if (attack.get(twoLeftCell) || GamePositions.obstacles.get(twoLeftCell)) {
                    captured.set(oneLeftCell);
                }

            }
        }

        //Check RIGHT
        // Check if the postion is not in the right 2 columns
        if (position % IState.EDGE_SIZE < IState.EDGE_SIZE - 2) {

            int oneRightCell = position + 1;
            if (opponent.get(oneRightCell)) {

                int twoRightCell = oneRightCell + 1;
                if (attack.get(twoRightCell) || GamePositions.obstacles.get(twoRightCell)) {
                    captured.set(oneRightCell);
                }

            }
        }

        return captured;
    }

    /**
     * Find the protection level of the king
     *
     * @param kingPos king bit position on the bit Board
     * @param blacks black bit board
     * @return count of black / obstacles cells around the king
     */
    public static int dangerToKing(int kingPos, BitSet blacks) {

        int threat = 0;

        //left to the king
        if (kingPos % IState.EDGE_SIZE != 0 && (blacks.get(kingPos - 1) || GamePositions.obstacles.get(kingPos - 1))) {
            threat++;
        }

        //right to the king
        if (kingPos % IState.EDGE_SIZE != IState.EDGE_SIZE - 1 && (blacks.get(kingPos + 1) || GamePositions.obstacles.get(kingPos + 1))) {
            threat++;
        }

        //above to the king
        if (kingPos >= IState.EDGE_SIZE && (blacks.get(kingPos - IState.EDGE_SIZE) || GamePositions.obstacles.get(kingPos - IState.EDGE_SIZE))) {
            threat++;
        }

        //below  the king
        if (kingPos < IState.BOARD_DIMENSION - IState.EDGE_SIZE && (blacks.get(kingPos + IState.EDGE_SIZE) || GamePositions.obstacles.get(kingPos + IState.EDGE_SIZE))) {
            threat++;
        }

        return threat;

    }

    /**
     * Find how many white Pawns circles Around the king in order to keep him
     * safe
     *
     * @param whites whites bit Board
     * @param kingPos king bit position on the bit Board
     * @return count of whites Pawns around the king
     */
    public static int protectKing(int kingPos, BitSet whites) {

        int protects = 0;

        //left to the king
        if (kingPos % IState.EDGE_SIZE != 0 && whites.get(kingPos - 1)) {
            protects++;
        }

        //right to the king
        if (kingPos % IState.EDGE_SIZE != IState.EDGE_SIZE - 1 && whites.get(kingPos + 1)) {
            protects++;
        }

        //above to the king
        if (kingPos >= IState.EDGE_SIZE && whites.get(kingPos - IState.EDGE_SIZE)) {
            protects++;
        }

        //below  the king
        if (kingPos < IState.BOARD_DIMENSION - IState.EDGE_SIZE && whites.get(kingPos + IState.EDGE_SIZE)) {
            protects++;
        }

        return protects;

    }

    /**
     * @param kingBoard the kings bit set
     * @return how many king pawns needed to eat king
     */
    public static int pawnsToEatKing(BitSet kingBoard) {

        // castle
        if (kingBoard.get(GamePositions.CENTER_POSITION)) {
            return 4;
        }

        // near castle
        if (GamePositions.specailKingCapture.intersects(kingBoard)) {
            return 3;
        }
        // regular eating
        return 2;
    }

    /**
     * Find the minimum manhattan Distance for the king to one of the free
     * escape cells
     *
     * @param xPos x Position of the king on the board
     * @param yPos y Position of the king on the board
     * @param fullBoard bit Set Of all the pieces on the board
     * @return the minimum manhattan Distance found from the king to escapes
     */
    public static int minDistanceToCorner(int xKing, int yKing, BitSet fullBoard) {

        int minDistance = GamePositions.MAX_DISTANCE_CORNER;
        int manhattanDistance;
        for (int escapeCellPosition : GamePositions.escapeCells) {

            // check if the escape cell is empty
            if (fullBoard.get(escapeCellPosition) == false) {

                int xCellEscape = escapeCellPosition % IState.EDGE_SIZE;
                int yCellEscape = escapeCellPosition / IState.EDGE_SIZE;

                manhattanDistance = Math.abs(yKing - yCellEscape) + Math.abs(xKing - xCellEscape);

                minDistance = Math.min(minDistance, manhattanDistance);

                // the minimum distance can be One so if we got to So end the loop               
                if (minDistance == 1) {
                    return 1;
                }

            }
        }
        return minDistance;
    }

}
