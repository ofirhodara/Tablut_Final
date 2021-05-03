/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicModel;

import java.util.BitSet;

/**
 *
 * @author Ofir
 */
public  class BitSetHelper {

    /**
     * build mask from array of positions indexes
     * @param positions array include the positions of the bits will be set to one in the mask
     * @return Mast built from the positions Array
     */
    public static BitSet newFromPositions(int[] positions) {
        BitSet posSet = new BitSet(IState.BOARD_DIMENSION);
        for (int position : positions) {
            posSet.set(position);
        }
        return posSet;
    }

    /**
     *  return a clone of and operation on BitSet board1 and board2
     * @param board1  board bitSet
     * @param board2  board bitSet
     * @return bitSet of the result of AND operation without change board1 and board2
     */
    public static BitSet cloneAndResult(BitSet board1, BitSet board2) {
     
        BitSet clone1 = (BitSet) board1.clone();
        clone1.and(board2);        
        return clone1;
    }
}
