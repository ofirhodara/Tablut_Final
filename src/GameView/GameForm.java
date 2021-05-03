package GameView;

import LogicModel.TablutState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import ofirtablut.OfirTablut.Agent;

/**
 *
 * @author Ofir
 */
public class GameForm extends JFrame {

    private static final int BOARD_SIZE = 1600;
    private static final String turnsMessage = "                                      Turns Taken: ";
    
    // GUI components
    private JMenuBar menuBar = new JMenuBar();
    private JLabel statusLabel, turnsLabel;
    private TablutBoardPanel boardPanel;
    private Agent white, black;

    /**
     * Build graphical user interfaces of the Appliaction
     */
    public GameForm() {

        super("Board Game");
        this.setSize(623, 737);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);

        // Initalize Labels...
        // Mode:
        this.statusLabel = new JLabel("Human VS Human");
        statusLabel.setFont(statusLabel.getFont().deriveFont(30.0f));
        this.getContentPane().add(statusLabel, BorderLayout.SOUTH);

        // Number of turns: 
        this.turnsLabel = new JLabel(turnsMessage + "0");
        turnsLabel.setFont(turnsLabel.getFont().deriveFont(20.0f));
        this.getContentPane().add(turnsLabel, BorderLayout.NORTH);

        // start of Human vs Human Game in default 
        white = Agent.HUMAN;
        black = Agent.HUMAN;

        // build The board Panel 
        this.boardPanel = new TablutBoardPanel(this);
        boardPanel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        this.add(this.boardPanel);

        // build the Actions bar
        Action AIGameBlack = new AbstractAction("BLACK HUMAN VS AI") {
            @Override
            public void actionPerformed(ActionEvent e) {
                white = Agent.AI;
                black = Agent.HUMAN;
                newGame();

            }
        };

        Action AIGameWhite = new AbstractAction("WHITE HUMAN VS AI") {
            @Override
            public void actionPerformed(ActionEvent e) {
                black = Agent.AI;
                white = Agent.HUMAN;
                newGame();
            }
        };

        Action HumansAction = new AbstractAction("Human VS Human") {
            @Override
            public void actionPerformed(ActionEvent e) {
                white = Agent.HUMAN;
                black = Agent.HUMAN;
                newGame();
            }
        };

        // menu
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Choose Mode...");

        // add actions
        fileMenu.add(AIGameBlack);
        fileMenu.add(AIGameWhite);
        fileMenu.add(HumansAction);
        menuBar.add(fileMenu);

    }

    /**
     * Start New Game in the Mode According to white and clack agents
     */
    public void newGame() {

        // Set the Status of the Game ready
        UpdateNewGame();

        this.boardPanel.resetGame();

        if (white == Agent.AI) {
            // if the computer is white call to computerFirst 
            this.boardPanel.computerFirst();
            this.boardPanel.setAI(true);
        }

        if (black == Agent.AI) {
            this.boardPanel.setAI(true);
        }

    }

    public void UpdateNewGame() {
        String whitePlayer = "White:" + white.toString();
        String blackPlayer = "Black:" + black.toString();
        this.statusLabel.setText(whitePlayer + " , " + blackPlayer);
        showMessageDialog(null, "New Game: \n" + whitePlayer + " \n " + blackPlayer);
        this.turnsLabel.setForeground(Color.BLACK);
    }

    // Agent Setters
    public void setWhite(Agent white) {
        this.white = white;
    }

    public void setBlack(Agent black) {
        this.black = black;
    }

    public void updateTurns(int numberOfTurns) {
        this.turnsLabel.setText(turnsMessage + " " + numberOfTurns);
        if (numberOfTurns > TablutState.MAX_TURNS - 10) {
            this.turnsLabel.setForeground(Color.RED);
        }
    }

}
