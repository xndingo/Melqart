package nl.tue.s2id90.groupNN;
import org10x10.dam.game.Move;

/**
 *
 * @author Jeroen van Hoof
 * @author Theodore Margomenos
 */
public class GameNode {
    
    /**
     * Return the game state of this node.
     * 
     */
    public void getGameState(){
        
    }
    
    /**
     * Get the best move found for the game state of this node.
     * 
     */
    public void getBestMove(){
        
    }
    
    /**
     * Set the best move found for the game state of this node.
     * 
     * @param move The move to be set as best.
     * 
     */
    public void setBestMove(Move move){
        
    }
    
    public GameState getState(){
        return new GameState();
    }
}
