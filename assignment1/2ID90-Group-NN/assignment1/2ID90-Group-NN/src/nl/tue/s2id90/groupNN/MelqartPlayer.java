package nl.tue.s2id90.groupNN;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 *
 * @author Jeroen van Hoof
 * @author Theodore Margomenos
 */
public class MelqartPlayer extends DraughtsPlayer {
    
    public MelqartPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }
    @Override
    /** @return a random move **/
    public Move getMove(DraughtsState s) {
        List<Move> moves = s.getMoves();
        Collections.shuffle(moves);
        return moves.get(0);
    }

    @Override
    public Integer getValue() {
        return 0;
    }
    
    int alphaBeta (GameNode node, int alpha, int beta, boolean maximizingPlayer){
        GameState state = node.getState();
        List <Move> moves = state.getMoves();
        if (maximizingPlayer) {
            for (Move move : moves){
                state.doMove(move);
                // recursive call
                alpha = max(alpha, alphaBeta(node, alpha, beta, maximizingPlayer));
                if (beta <= alpha) {
                    break; //b cut off
                }
                state.undoMove(move);
                return alpha;                                 
            }
        }
        else { //minimizingPlayer
            for (Move move : moves){
                state.doMove(move);
                // recursive call
                beta = min(beta, alphaBeta(node, alpha, beta, true));
                if (beta <= alpha) {
                    break; //a cut off
                }
                state.undoMove(move);
                return beta;                        
            }
        }
        return 0;
                    
                    }
        Move bestMove = null;
        
        
        node.setBestMove(bestMove);
        return 0;
    }
    
    private boolean stopped = false ;
    @Override
    public void stop ( ) { stopped = true ; }
    int alphaBeta (GameNode node , int alpha , int beta )
    throws AIStoppedException {
    if ( stopped ) {
    stopped = false ;
    throw new AIStoppedException ( ) ;
 }
    //.. .
}