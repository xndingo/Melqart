package nl.tue.s2id90.groupNN;

import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * State of the game.
 * 
 * @author Jeroen van Hoof
 * @author Theodore Margomenos
 */
public class GameState {
    
    /**
     * Get all valid moves in this state.
     * 
     * @return Returns the possible moves.
     */
    public List <org10x10.dam.game.Move> getMoves(){
        DraughtsState s = new DraughtsState();
        return s.getMoves();
    }
    
    /**
     * Make a move.
     * 
     * @param move the move to make
     */
    public void doMove(Move move){
        
    }
    
    /**
     * Undo a move
     * 
     * @param move the move to undo
     */
    public void undoMove(Move move){
        
    }
}
