/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofirtablut.Controller;

import java.util.List;

/**
 *
 * @author Ofir
 */

/*
* Interface of the Controller in MVC 
   Accepts input and converts it to commands for the model or view.
 */
public interface IController {

    public List<Integer> getMovesToView(int piecePos);

    public void makeTurn(int turnFrom, int turnTo, boolean isAI);

    public boolean isPieceInPos(int piecePos);

    public void resetGame();

    public int getCountTurns();

    public void playAI();
}
