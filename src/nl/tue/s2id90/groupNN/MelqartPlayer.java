package nl.tue.s2id90.groupNN;
import java.util.Collections;
import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
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
    
    int alphaBeta (GameNode node, int alpha, int beta){
        GameState state = node.getState();
        List <Move> moves = state.getMoves();
        for (Move move : moves){
            state.doMove(move);
            // recursive call
            state.undoMove(move);
        }
        Move bestMove = null;
        
        
        node.setBestMove(bestMove);
        return 0;
    }
}