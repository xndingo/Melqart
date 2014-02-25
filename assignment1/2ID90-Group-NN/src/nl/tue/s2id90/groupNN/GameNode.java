package nl.tue.s2id90.groupNN;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class GameNode {
    private DraughtsState gs;
    
    // Constructor
    public GameNode(DraughtsState gs) {
        this.gs = new DraughtsState();
    }
       
    /**
     * Gets the best move that can be made.
     * @return 
     */
    public int getBestMove() {
        return 0; //testing        
    }

  
    /**
     * Gets the game state of this node.
     * @return 
     */
    public DraughtsState getState() {
        return this.gs;
    }
    
    /**
     * Sets the best move to can be made.
     * @param move
     */
    public void setBestMove(int move) {
        throw new UnsupportedOperationException("Not supported yet."); 
        
    }
    
    
    public Integer getValue() {
        return evaluate(gs);
    }

    // Material
    final static int KING = 30;      // Number of kings
    final static int DRAUGHT = 10;   // Number of draughts

    // Infastructure
    final double PLAY = 0.4;  // Playground
    final int MOVE = 4;       // Number of possible moves
    final int MIRROR = 5;     // Number of opposing draughts
    final int DISTR = 5;      // Distribution of draughts in three columns

    // Strategy
    final int SIDE = 6;       // Number of draughts on the sides
    final int LAST = 7;       // Number of draughts on last line
    final int STEPS = 4;      // Manhattan distance to end (except Kings)
    final int DEF = 8;        // Number of defended draughts

    /**
     * Evaluates the current DraughtsState.
     *
     * @param ds The DraughtsState.
     * @return The value of the current state.
     */
    public static int evaluate(DraughtsState ds) {
        int total = 0;

        // White or black has to move.
        boolean isWhite = ds.isWhiteToMove();
        // Possible moves.
        int moves = ds.getMoves().size();

        for (int c = 0; c <= 10; c++) {
            for (int r = 0; r <= 10; r++) {
                total += addValue(r, c, isWhite, ds);
            }
        }

        return total;
    }

    public static int addValue(int r, int c, boolean isWhite, DraughtsState ds) {
        int piece = ds.getPiece(r, c);
        if (piece != DraughtsState.WHITEFIELD
                && piece != DraughtsState.EMPTY) {
            if (isWhite) {
                if (piece == DraughtsState.WHITEKING) {
                    return 50 + KING;
                } else {
                    return calcPlayground(r, c, true) + DRAUGHT;
                }
            } else {
                if (piece == DraughtsState.BLACKKING) {
                    return 50 + KING;
                } else {
                    return calcPlayground(r, c, false) + DRAUGHT;
                }
            }

        } else {
            return 0;
        }
    }

    public static int calcPlayground(int y, int x, boolean white) {
        int triangleR = 0, triangleL = 0;

        // Flip board for black
        if (!white) {
            x = 10 - x + 1;
            y = 10 - y + 1;
        }

        // Calculate the big triangle
        int bigTriangle = y / 2 * (y + 1);

        // Overflow on right side
        int baseR = y - 1 + x - 10;
        if (baseR > 0) {
            // Calculate triangle right
            triangleR = (baseR - 1) / 2 * baseR;
        }

        // Overflow on left side
        int baseL = y - x;
        if (baseL > 0) {
            // Calculate triangle left
            triangleL = (baseL + 1) / 2 * baseL;
        }

        return bigTriangle - triangleR - triangleL;

    }
    
}