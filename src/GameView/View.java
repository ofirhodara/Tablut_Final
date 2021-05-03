/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import ofirtablut.Controller.Controller;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import ofirtablut.Controller.IController;
import LogicModel.Action;
import LogicModel.IState;
import LogicModel.TablutState;
import ofirtablut.OfirTablut;
import ofirtablut.OfirTablut.Agent;
import ofirtablut.OfirTablut.Player;

/**
 *
 * @author Ofir
 */
public class View implements IView {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";

    private Scanner sc;

    private boolean running = true;

    private IController controller;

    private IState state;

    public View() {

        sc = new Scanner(System.in);

        // build the Controller of the program
        this.controller = new Controller(this);

    }
    //********************************* Game Runnig *************************************

    public void startGame(Agent white, Agent black) {

        if (white == Agent.AI && black == Agent.AI) {
            this.computersCompetetion();
            return;
        }

        int turnTo, turnFrom;
        boolean isCompuer = (white == Agent.AI || black == Agent.AI);

        if (isCompuer && white == Agent.AI) {
            controller.playAI();
        }

        this.printGameState();

        while (running) {
            // Get the col from the Client 
            System.out.println("Please, Press the cell from:");
            turnFrom = sc.nextInt();
            System.out.println("Please, Press the cell to:");
            turnTo = sc.nextInt();
            // Make the turn
            controller.makeTurn(turnFrom, turnTo, isCompuer);
        }

    }

    private void computersCompetetion() {

        while (running) {
            controller.playAI();
        }

    }

    //********************************* View Functions **********************************
    
    @Override
    public void updateGameDetails(BitSet captures, Action move) {
        // demy.printState();
    }

    @Override
    public void printDraw() {
        System.out.println("Sorry there is a Draw in the game!");
        this.running = false;
    }

    @Override
    public void printWIN(Player player) {
        System.out.println("The game is Over");
        System.out.println("The winner is: " + player);
        this.running = false;
    }


    //***********************************************************************************************************************
    @Override
    public void printGameState() {
        this.state.printState();
    }

    @Override
    public void setBoard(IState state) {
        this.state = state;
    }

 
     

}
