package GameView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;
import ofirtablut.Controller.Controller;
import LogicModel.Action;
import LogicModel.GamePositions;
import LogicModel.IState;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ofirtablut.OfirTablut.Piece;
import ofirtablut.OfirTablut.Player;

/**
 *
 * @author Ofir
 */
public class TablutBoardPanel extends BoardPanel implements MouseListener, IView {

    // Static constants about the GUI design.
    static final Color BACKGROUND_COLOR = Color.GRAY;
    static final Color MOVE_COLOR = Color.RED;
    static final Color CORNER_COLOR = new Color(211, 211, 211);
    static final int CENTER_POSITION = 40;
    static final Color CENTER_COLOR = new Color(255, 215, 0);
    static final Color CAMPS_COLOR = Color.GRAY;
    static final Color PROTECTED_COLOR = new Color(218, 165, 32);
    static final Color BOARD_COLOR2 = new Color(245, 222, 179);
    static final Color BOARD_COLOR1 = new Color(244, 164, 96);
    static final Color MUSCOVITE_COLOR = Color.BLACK;
    static final Color SWEDE_COLOR = Color.WHITE;
    static final int HIGHLIGHT_THICKNESS = 3;
    static final int PIECE_SIZE = 55;
    static final int FONT_SIZE = (int) (PIECE_SIZE * 0.5);
    static final int SQUARE_SIZE = (int) (PIECE_SIZE * 1.25); // Squares 25% bigger than pieces.
    static final String DRAW_MESSAGE = "The game is Over! \n It's a Draw!";
    static final String WINNER_MESSAGE = "The game is Over!\n The winner is: ";

    // Stores all board pieces   
    // private GUIPiece pieceToMove;
    private int pieceIndexSelected;
    private ArrayList<Integer> legalCoordsToMoveTo;
    private GameForm gameForm;
    private GUIPiece[] boardPieces;

    /**
     * constructor to the board Panel
     *
     * @param mainView get the Main Game form
     */
    public TablutBoardPanel(GameForm mainView) {
        addMouseListener(this);
        this.boardPieces = new GUIPiece[IState.BOARD_DIMENSION];
        legalCoordsToMoveTo = new ArrayList<>();
        initBoard();
        setUp();
        this.controller = new Controller(this);
        this.gameForm = mainView;
        pieceIndexSelected = -1;
    }

    /**
     * build the boardPieces ArrayList of pieces in the start of the Game
     */
    private void initBoard() {
        for (int piecePos = 0; piecePos < IState.BOARD_DIMENSION; piecePos++) {
            int xPos = (piecePos % 9) * SQUARE_SIZE + SQUARE_SIZE / 2;
            int yPos = (piecePos / 9) * SQUARE_SIZE + SQUARE_SIZE / 2;
            this.boardPieces[piecePos] = new GUIPiece(Piece.EMPTY, xPos, yPos);
        }
    }

    /**
     * set up the starting pieces on the GUI board
     */
    private void setUp() {
        for (int bitPos = 0; bitPos < IState.BOARD_DIMENSION; bitPos++) {
            this.boardPieces[bitPos].pieceType = GamePositions.getPawn(bitPos);
        }
    }

    /**
     * reset the Game board when starts new Game
     */
    public void resetGame() {
        setUp();
        isAI = false;
        legalCoordsToMoveTo.clear();
        this.controller.resetGame();
        humanRepaint();
        this.gameForm.updateTurns(0);
        pieceIndexSelected = -1;
    }

    @Override
    public void printDraw() {
        showMessageDialog(null, DRAW_MESSAGE);
        gameForm.newGame();
    }

    @Override
    public void printWIN(Player player) {
        showMessageDialog(null, WINNER_MESSAGE + player);
        gameForm.newGame();
    }

    /**
     * the function update the board according after getting action details from
     * the controller
     *
     * @param captures BitSet of the captured pawns in the current Move
     * @param move the last action the Human/AI did
     */
    @Override
    public void updateGameDetails(BitSet captures, Action move) {

        // clear all the captures  pieces      
        for (int i = captures.nextSetBit(0); i != -1; i = captures.nextSetBit(i + 1)) {
            this.boardPieces[i].pieceType = Piece.EMPTY;
        }

        // update according to the moving piece 
        GUIPiece movingPiece = this.boardPieces[move.getFrom()];
        Piece piece = movingPiece.pieceType; 
        movingPiece.pieceType = Piece.EMPTY;

        humanRepaint();
        
       // in order that the user will see The AI moves
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TablutBoardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        GUIPiece destPiece = this.boardPieces[move.getTo()];
        destPiece.pieceType = piece;
        humanRepaint();
    }

    /**
     * Drawing the current board
     *
     * @param g Graphics of the game form
     */
    @Override
    public void drawBoard(Graphics g) {

        super.drawBoard(g); // Paints the background

        // Make the circle look pretty.
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint board.
        Color crtColor = BOARD_COLOR2;
        for (int piecePos = 0; piecePos < IState.BOARD_DIMENSION; piecePos++) {

            if (piecePos == CENTER_POSITION) {
                g.setColor(CENTER_COLOR);
            } else {
                if (GamePositions.camps.get(piecePos)) {
                    g.setColor(CAMPS_COLOR);
                } // if he has escape cells
                else {
                    g.setColor(crtColor);
                }
            }

            // Fill and paint the rectangle with the current color.
            g.fillRect((piecePos % IState.EDGE_SIZE) * SQUARE_SIZE, (piecePos / IState.EDGE_SIZE) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

            // swap the color of the box  
            crtColor = (crtColor == BOARD_COLOR1) ? BOARD_COLOR2 : BOARD_COLOR1;
        }

        // If there are any requested moves, highlight those squares. and then
        // draw border around square if human could move there, given current click.
        for (int piecePos : legalCoordsToMoveTo) {
            g.setColor(MOVE_COLOR);
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(HIGHLIGHT_THICKNESS));
            g.drawRect((piecePos % 9) * SQUARE_SIZE, (piecePos / 9) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            g2.setStroke(oldStroke);
        }

        // Put the pieces on the board.
        for (GUIPiece gp : boardPieces) {
            gp.draw(g);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    private void humanRepaint() {
        bufferDirty = true;
        repaint();
    }

    /**
     * Get the place that the human clicked and do the turn if it is legal
     *
     * @param e An event which indicates that a mouse action occurred in a
     * component
     */
    @Override
    public void mousePressed(MouseEvent e) {

        int clickX = e.getX();
        int clickY = e.getY();
        int xIndex = clickX / SQUARE_SIZE;
        int yIndex = clickY / SQUARE_SIZE;
        int bitIndex = yIndex * IState.EDGE_SIZE + xIndex;
        //pieceToMove == null
        if (pieceIndexSelected == -1) {

            // check if the user clicked on a piece
            GUIPiece currentPiece = this.boardPieces[bitIndex];

            if (currentPiece.pieceType != Piece.EMPTY && clickInCircle(clickX, clickY, currentPiece.xPos, currentPiece.yPos)) {
                //pieceToMove = currentPiece;
                pieceIndexSelected = bitIndex;
            }

            if (pieceIndexSelected != -1) {
                // check if we click on our piece 
                if (controller.isPieceInPos(pieceIndexSelected) == false) {
                    //pieceToMove = null;
                    pieceIndexSelected = -1;
                    legalCoordsToMoveTo = new ArrayList<>(16);
                } else {
                    //pieceToMove.piecePos
                    List<Integer> possibleDest = this.controller.getMovesToView(pieceIndexSelected);
                    legalCoordsToMoveTo.addAll(possibleDest);
                }
            }
        } else {

            // Then the piece was already clicked on, 
            // and now we want to actually move it.
            int dest = bitIndex;

            // check if it is legal move 
            if (legalCoordsToMoveTo.contains(dest)) {

                // pieceToMove.piecePos
                this.controller.makeTurn(pieceIndexSelected, dest, this.isAI);
                this.gameForm.updateTurns(this.controller.getCountTurns());
            }
            //pieceToMove = null;
            pieceIndexSelected = -1;
            legalCoordsToMoveTo.clear();
        }

        humanRepaint();
    }

    @Override
    public void computerFirst() {
        controller.playAI();
        this.gameForm.updateTurns(1);
    }

    // GUI Helpers functions
    private static boolean clickInCircle(int x, int y, int cx, int cy) {
        // check if the distance is small then the radius  
        return Math.pow(cx - x, 2) + Math.pow(cy - y, 2) < Math.pow(PIECE_SIZE / 2, 2);

    }

    /*
    private static boolean clickInSquare(int x, int y, int cx, int cy) {
        return Math.abs(x - cx) < SQUARE_SIZE / 2 && Math.abs(y - cy) < SQUARE_SIZE / 2;
    }*/
    
    private final class GUIPiece {

        private Piece pieceType;
        // positions on the screen
        private int xPos;
        private int yPos;

        public GUIPiece(Piece pieceType, int xPos, int yPos) {
            this.pieceType = pieceType;
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public void draw(Graphics g) {
            draw(g, xPos, yPos);
        }

        public void draw(Graphics g, int cx, int cy) {
            if (pieceType != Piece.EMPTY) {

                int x = cx - PIECE_SIZE / 2;
                int y = cy - PIECE_SIZE / 2;

                g.setColor(pieceType == Piece.BLACK ? MUSCOVITE_COLOR : SWEDE_COLOR);

                // Paint piece           
                g.fillOval(x, y, PIECE_SIZE, PIECE_SIZE);

                if (pieceType != Piece.BLACK) {
                    // draw a border around whites
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, PIECE_SIZE, PIECE_SIZE);
                }

                if (pieceType == Piece.KING) {
                    g.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, FONT_SIZE));
                    g.setColor(Color.RED); // Black font color
                    int textMod = (int) (PIECE_SIZE * (9.0 / 50.0)); // helps with centering
                    g.drawString("K", cx - textMod, cy + textMod);
                }
            }

        }
    }

//*****************************************************************************************************************************
    /* Don't use these interface methods */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void printGameState() {

    }

    @Override
    public void setBoard(IState state) {
    }

}
