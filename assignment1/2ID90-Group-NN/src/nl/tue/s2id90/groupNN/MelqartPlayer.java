package nl.tue.s2id90.groupNN;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.List;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/** 
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
    
    /**
     * It applies the alpha-beta min-max algorithm given a 
     * game node {@code node}, an integer alpha, an integer beta
     * and if it's a maximizing player or not as a boolean.     * 
     * @param node game node
     * @param alpha integer
     * @param beta integer
     * @param maximizingPlayer boolean
     * @return alpha or beta integers
     */
    public int alphaBeta (GameNode node, int alpha, int beta, boolean maximizingPlayer){
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
            node.setBestMove(alpha);
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
            node.setBestMove(beta);
        }
        return 0;                    
    }
    
    /**
     * Returns the amount of pieces that are on the board
     * given a game state {@code gs}
     * @param gs game state of the board
     * @return the amount of pieces that are on the board. 0 if empty.
     */
    public int getPieceCount(DraughtsState gs) {
        int[] pieces = gs.getPieces();
        int count = 0;
        for (int f = 1; f < pieces.length; f = f+1) {
            int piece = pieces[f];
            if (Draughts.isWhite(piece) || Draughts.isBlack(piece)) {
                count++;            
            }
        }        
        return count; //0 if no pieces on the board.
    }   
    
    /**
     * Evaluates the state of the draughts board.
     * @param ds draught state
     * @return an integer evaluation of the draughts state
     */
    public int evaluate (DraughtsState ds) {
        // obtain piecesarray
        int[] pieces = ds.getPieces();
        int eval = 0;
        // compute a value for this state, e.g.
        // by compareing p[i] to WHITEPIECE, WHITEKING, etc
        //. . .
        return 0;
    }

    private boolean stopped = false ;
    @Override
    public void stop ( ) { 
        stopped = true ; 
    }
    int alphaBeta(GameNode node, int alpha, int beta) 
        throws AIStoppedException {
        if (stopped) {
            stopped = false;
            throw new AIStoppedException();
        }
    //.. .
        return 0;
    }
}