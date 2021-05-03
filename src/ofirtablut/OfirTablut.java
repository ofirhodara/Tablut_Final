package ofirtablut;

import GameView.GameForm;
import GameView.View;
import LogicModel.GamePositions;

/**
 *
 * @author Ofir
 */
public class OfirTablut {

    public enum Player {
        WHITE, BLACK
    };

    public enum Piece {
        BLACK, WHITE, KING, EMPTY
    };

    public enum Agent {
        HUMAN, AI
    };

    public static void main(String[] args) {
        
        /*
        long start = System.currentTimeMillis();
        
         View gameView = new View();
         gameView.startGame(Agent.AI, Agent.AI);
         
        long finish = System.currentTimeMillis();
        
        System.out.println("Time to Choose Move: " + (finish - start) / (double) 1000 + " Seconds! ");
         */
      
        GameForm g = new GameForm();
        g.setVisible(true);

    }

}
