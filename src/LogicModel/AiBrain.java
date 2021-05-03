package LogicModel;

import java.util.Comparator;
import java.util.List;
import ofirtablut.OfirTablut.Player;

/**
 * class responsible of the artificial intelligence of the game this class use
 * Nega Max algorithm in order to choose the optimal move for player in the game
 * Of Tablut
 *
 * @author Ofir
 */

public class AiBrain {

    private IState boardGame;
    private static final int DEPTH_SEARCH = 5;

    public AiBrain(IState boardGame) {
        this.boardGame = boardGame;
    }

    /**
     * The function call to NegaMax function and find the best Move for the
     * current player
     *
     * @return Action with the positions of the best move founded in the search
     */
    public Action bestMove() {
        Action optimalAction;
        long start = System.currentTimeMillis();

        optimalAction = negaMax(DEPTH_SEARCH, Integer.MIN_VALUE, Integer.MAX_VALUE);

        long finish = System.currentTimeMillis();
        System.out.println("Time to Choose Move: " + (finish - start) / (double) 1000 + " Seconds! ");
        System.out.println("\t" + optimalAction);
        return optimalAction;
    }

    /**
     * the function use negaMax algorithm with alpha beta pruning to choose the
     * optimal move for the current player in this.boardGame
     *
     * @param depth depth of the search in the Game tree
     * @param alpha The best highest-value choice we have found so far
     * @param beta The lowest-value choice we have found so far
     * @return the best Action for the current player founded
     */
    
    private Action negaMax(int depth, double alpha, double beta) {

        if (boardGame.isGameOver() || depth == 0) {
            // if we get to leaf or max search depth 
            return new Action(boardGame.getHuristic());
        }

        // get all possible moves 
        List<Action> possibleMoves = boardGame.getAvailablePawnMoves();

        moveOrdering(possibleMoves);

        Action bestMove = new Action();

        for (Action move : possibleMoves) {

            // get to captured pawns from the move
            // in order to "undo" the move after searching            
            CapturedPawns captures = boardGame.performAction(move);

            // call the function recursively with less depth 
            Action childBestMove = negaMax(depth - 1, -beta, -alpha);

            // use the NegaMax principle that max(a, b) = -min(-a, -b)
            childBestMove.negateGrade();

            double childGrade = childBestMove.getGrade();
            
            // found better Move to Do
            if (childGrade > bestMove.getGrade()) {
                bestMove = move;
                bestMove.setGrade(childGrade);
            }

            // Undo move           
            this.boardGame.cancelMove(captures, move);

            // check if we can "cut" parts of the game tree
            alpha = Math.max(alpha, bestMove.getGrade());

            if (alpha >= beta) {
                break;
            }

        }
        return bestMove;
    }

    /**
     * The function responsible for sort the list of possible moves in way that
     * will cause more "cutting" of the game tree will be explained in the
     * article
     *
     * @param moves the list of possible moves in node of the game tree
     */
    private void moveOrdering(List<Action> moves) {
        // if the player is black 
        // sort by priority:
        // 1. if the destination of the action is near the King 
        // 2. if the origin Is not in camp Cells
        // 3. all other actions

        // for white Player:
        // the king moves will be calculated first
        
        if (this.boardGame.getCurrentPlayer() == Player.BLACK) {
            
            int kingPos = this.boardGame.getKing().nextSetBit(0);
            
            moves.sort(new Comparator<Action>() {
                // sort in descending order
                @Override
                public int compare(Action m1, Action m2) {
                    return m2.actionBlackValue(kingPos) - m1.actionBlackValue(kingPos);
                }
                
            });
            
        }
    }
}
