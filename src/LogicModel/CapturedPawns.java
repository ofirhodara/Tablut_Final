
package LogicModel;

import java.util.BitSet;

/**
 *
 * @author Ofir
 */
public class CapturedPawns {

    private BitSet captured;
    private int kingPos;

    public BitSet getCaptured() {
        return captured;
    }

    public CapturedPawns(BitSet captured) {
        this.captured = captured;
        this.kingPos = -1;
    }

    public CapturedPawns(BitSet captured, int kingPos) {
        this.captured = captured;
        this.kingPos = kingPos;
    }

    public int getKingPos() {
        return kingPos;
    }

    public void setKingPos(int kingPos) {
        this.kingPos = kingPos;
    }

}
