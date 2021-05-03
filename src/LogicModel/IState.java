
package LogicModel;

import java.util.BitSet;
import java.util.List;
import ofirtablut.OfirTablut.Player;

/**
 * Interface to State of Tablut Board
 *
 * @author Ofir
 */
public interface IState {

    final int MAX_TURNS = 100;

    final int EDGE_SIZE = 9;

    final int BOARD_DIMENSION = EDGE_SIZE * EDGE_SIZE;

    public int getCountTurns();

    public boolean isPlayerPieceInPos(int bitPos);

    public void printState();

    public Player getEnemy();

    Player getCurrentPlayer();

    void changeCurrentPlayer();

    BitSet getBlackPawns();

    BitSet getWhitePawns();

    public void undoCaptures(CapturedPawns captures);

    public void undoAction(Action action);

    public CapturedPawns performAction(Action action);

    BitSet getKing();

    BitSet getBoard();

    boolean isWinningState();

    boolean blackHasWon();

    boolean whiteHasWon();

    List<Action> getAvailablePawnMoves();

    List<Action> getAvailableKingMoves();

    public IState clone();

    public boolean isGameOver();

    public double getHuristic();

    public void cancelMove(CapturedPawns Captures, Action move);

    public boolean isDraw();

}
