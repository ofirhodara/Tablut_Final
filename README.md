# Tablut_Final - built 1 vs 1 and 1 vs computer Game
Algorithm: negas Count Move ordering with ab purning AI Agent for Tablut
i use bit board technique for building the board and bitwise operations in order to do moves and clculate the best moves
in the best and efficient way...

What is bit board?
A bitboard is a specialized bit array data structure commonly used in computer systems that play board games, where each bit corresponds to a game board space or piece. This allows parallel bitwise operations to set or query the game state, or determine moves or plays in the game.
 
 
 Example:
 board class:
    // Bits board 
    private Player currentPlayer;
    private final BitSet whitesPawns, kingPawn, blackPawns;
    private final BitSet board;
 
 check if king captured:
 
            // check if a king is captured
            if (captures.get(kingPos)) {
                 this.kingPawn.clear();
                 kingCaptruedPos = kingPos;
            }

            this.whitesPawns.andNot(captures); // 'eat' piece by doing andNot operation...
