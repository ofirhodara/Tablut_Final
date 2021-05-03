package ofirtablut.Controller;

import java.util.List;
import LogicModel.Action;
import LogicModel.TablutState;
import LogicModel.IState;
import GameView.IView;
import java.util.stream.Collectors;
import LogicModel.AiBrain;
import LogicModel.BoardMoves;
import LogicModel.CapturedPawns;

/**
 *
 * @author Ofir
 */

public class Controller implements IController {

    private IState state;
    private IView view;

    /**
     * Controller constructor
     *
     * @param gameView get object of IView and build the controller and the
     * model Layers
     */
    public Controller(IView gameView) {
        this.view = gameView;
        this.state = new TablutState();
        this.view.setBoard(state);
    }

    /**
     * The function reset The game of Tablut by reset the TablutState to the
     * starting board
     */
    @Override
    public void resetGame() {
        this.state = new TablutState();
    }

    /**
     * the function perform human action if the game is against AI perform also
     * the Computer's Turn
     *
     * @param turnFrom piece index to move
     * @param turnTo destination to move the piece in turnFrom place
     * @param isAI boolean flag that say if the game is against computer
     */
    @Override
    public void makeTurn(int turnFrom, int turnTo, boolean isAI) {

        // build the action that the user asked     
        Action desiredAction = new Action(turnFrom, turnTo);

        // perform human move 
        doMove(desiredAction);

        boolean isGameOver = isEnd();
        // check if the Game Over
        if (!isGameOver) {
            // if the Game is not over and we play
            // against Computer Call to AI function to do turn 
            if (isAI) {
                playAI();
            }
        }

    }

    /**
     * the function perform Action and updates the view about the move results
     *
     * @param move move to perform on the board
     */
    private void doMove(Action move) {
        CapturedPawns captures = state.performAction(move);
        
        // update the view about the changes on the board 
        this.view.updateGameDetails(captures.getCaptured(), move);
    }

    /**
     * The function check if the game is Over and update the view accordingly
     *
     * @return True if the Game is Over else return False
     */
    public boolean isEnd() {

        if (state.isGameOver()) {
            if (state.isDraw()) {
                view.printDraw();
            } else {
                view.printWIN(state.getEnemy());
            }
            return true;
        }
        return false;
    }

    /**
     * the function find the possible moves for Piece in order to mark them in
     * the Board
     *
     * @param piecePos piece position on the board
     * @return list of possible moves to the piece At piecePos
     */
    @Override
    public List<Integer> getMovesToView(int piecePos) {
        List<Action> moves = BoardMoves.getMovesForPawn(piecePos, state);
        return moves.stream().map(urEntity -> urEntity.getTo()).collect(Collectors.toList());
    }

    /**
     * the function call to the artificial intelligence Algorithm and perform
     * the optimal move for the computer
     */
    @Override
    public void playAI() {
        AiBrain brain = new AiBrain(this.state);
       
        Action turn = brain.bestMove();
        // perform Computer Move
        doMove(turn);
        // check if the Computer Won
        isEnd();
    }

    @Override
    public boolean isPieceInPos(int piecePos) {
        return this.state.isPlayerPieceInPos(piecePos);
    }
    
    @Override
    public int getCountTurns() {
        return state.getCountTurns();
    }
}
