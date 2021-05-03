
package LogicModel;

/**
 * class represent action in the game Tablut
 *
 * @author Ofir
 */
public class Action {

    // the grade of the move
    private double grade;

    // piece index to move
    private final int from;

    // destination of the move index
    private final int to;

    // Construcrors:
    public Action(double grade) {
        this.grade = grade;
        this.from = -1;
        this.to = -1;
    }

    public Action() {
        this.grade = Integer.MIN_VALUE;
        this.from = -1;
        this.to = -1;
    }

    public Action(int from, int to) {
        this.from = from;
        this.to = to;
        this.grade = 0;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void negateGrade() {
        this.grade *= -1;

    }

    public double getGrade() {
        return grade;
    }

    public int getFrom() {
        return this.from;
    }

    public int getTo() {
        return this.to;
    }

    /**
     * get the secondary evaluation depends for black move in O(1)
     * @param kingPosition bit index of the king 
     * @return secondary evaluation value
     */
    public int actionBlackValue(int kingPosition) {
        
        // grades:
        // if near King 2
        // if not in camp King 1
        // else 0
        
        // left or right
        if (this.to == kingPosition + 1 || this.to == kingPosition - 1) {
            return 2;
        }
        // up or down
        if (this.to == kingPosition - IState.EDGE_SIZE || this.to == kingPosition + IState.EDGE_SIZE ) {
            return 2;
        }
                
        if(!GamePositions.camps.get(from))
            return 1;      
       
        return 0;

    }

    @Override
    public boolean equals(Object other) {
        Action otherAction = (Action) other;
        return otherAction.from == this.from && otherAction.to == this.to;
    }

    private String getNameCell(int cellPosition)
    {
        int yPos = ((cellPosition / 9) + 1);
        int xPos = (cellPosition % 9) ;
        
        String name = (char) (xPos + 'A') + "" +  yPos;
        
        return name;
    }
    
    @Override
    public String toString() {
        return "Action{" + "Origin Cell = " +getNameCell(from) + ", Destination Cell = " + getNameCell(this.to)+  "}";
    }

}
