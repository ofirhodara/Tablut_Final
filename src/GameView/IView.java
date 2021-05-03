
package GameView;

import java.util.BitSet;
import java.util.List;
import LogicModel.Action;
import LogicModel.IState;
import ofirtablut.OfirTablut.Player;


/**
 * Interface for the View part of the application the view include Any
 * representation of information such as a chart, diagram or table of the
 * application
 *
 * @author Ofir
 */
public interface IView {

    // update that we have a draw
    public void printDraw();

    // update who is the player that Won
    public void printWIN(Player player);

    public void updateGameDetails(BitSet captures, Action move);   
    
    // will be deleted
    public void printGameState();

    public void setBoard(IState state);

}
