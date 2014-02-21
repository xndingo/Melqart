package nl.tue.s2id90.groupNN;

import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class GameNode  {
    
    // Constructor
    public GameNode() {
        
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
    public GameState getState() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Sets the best move to can be made.
     * @param move
     */
    public void setBestMove(int move) {
        throw new UnsupportedOperationException("Not supported yet."); 
        
    }
    
} 
