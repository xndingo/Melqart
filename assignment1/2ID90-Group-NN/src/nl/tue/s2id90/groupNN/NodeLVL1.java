package nl.tue.s2id90.groupNN;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class NodeLVL1 {
    public static DraughtsState ds;
    
    // Constructor
    public NodeLVL1(DraughtsState ds) {

         if (ds == null){
            throw new IllegalArgumentException("gs in gamenode");
        }
        NodeLVL1.ds = ds.clone();
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
        return NodeLVL1.ds;
    }
    
    /**
     * Sets the best move to can be made.
     * @param move
     */
    public void setBestMove(int move) {
        throw new UnsupportedOperationException("Not supported yet."); 
        
    }

    /**
     *
     * @return
     */
    public Integer getValue(DraughtsState ds) throws Exception {
        return evaluate(ds, ds.isWhiteToMove());
    }

    /**
     * Evaluates the current GameState.
     *
     * @param ds The GameState.
     * @return The value of the current state.
     */
    public static int evaluate(DraughtsState ds, boolean white){
        int[] pieces = ds.getPieces();
        int total = 0;
        for (int piece : pieces){
            switch(piece){
            case 0 : total += 0; break; // EMPTY
            case 1 : total += 100; break; // WHITEPIECE
            case 2 : total -= 100; break; // BLACKPIECE
            case 3 : total += 300; break; // WHITEKING
            case 4 : total -= 300; break; // BLACKKING
            case 5 : total += 0; break; // WITEFIELD
            }
        }
        if (!white){
            total = -total;
        }
        return total;
    }    
}