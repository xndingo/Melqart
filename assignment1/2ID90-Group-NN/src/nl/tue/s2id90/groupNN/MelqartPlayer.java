package nl.tue.s2id90.groupNN;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class MelqartPlayer extends DraughtsPlayer {
    private final int maxDepth = 6;
    private int value;
    private int count;
    
    
    public MelqartPlayer() {
        super(MelqartPlayer.class.getResource("resources/squirtle.jpg"));
    }
    
    /**
     * Returns the amount of pieces that are on the board given a game state
     * {@code gs}
     *
     * @param gs game state of the board
     * @return the amount of pieces that are on the board. 0 if empty.
     */
    public int getPieceCount(DraughtsState gs) {
        int[] pieces = gs.getPieces();
        int count = 0;
        for (int f = 1; f < pieces.length; f = f + 1) {
            int piece = pieces[f];
            if (Draughts.isWhite(piece) || Draughts.isBlack(piece)) {
                count++;
            }
        }
        return count; //0 if no pieces on the board.
    }

    private boolean stopped = false;

    @Override
    public void stop() {
        stopped = true;
    }
   
    int alphaBeta(GameNode node, int alpha, int beta, int player, int depth) 
            throws Exception{
        count++;
        DraughtsState state = node.getState();
        if (depth == 0){
            return player * node.getValue(node.getState());
        }
        for (Move move : state.getMoves()){
            state.doMove(move);
            alpha = max(alpha, -alphaBeta(new GameNode(state.clone()), 
                    -beta, -alpha, -(player), depth - 1));
            state.undoMove(move);
            if (beta >= alpha){
                return alpha;
            }
        }
        return alpha;
    }
    
    Move rootAlphaBeta(GameNode node, int alpha, int beta, int player, int depth) 
            throws Exception {
        Move bestMove = null;
        DraughtsState state = node.getState();
        int maxEval = -10000;
        for (Move move : state.getMoves()){
            state.doMove(move);
            alpha = max(alpha, -alphaBeta(new GameNode(state.clone()), 
                    -beta, -alpha, -(player), depth - 1));
            state.undoMove(move);
            if (alpha > maxEval){
                maxEval = alpha;
                bestMove = move;
            }
        }
<<<<<<< HEAD
        System.out.println("MelqartPlayer count:" + count);
=======
        System.out.println("Count:" + count);
>>>>>>> e3d0ef84be024ac61e72e2de77e53afb68d02f14
        count = 0;
        return bestMove;
    }

    @Override
    public Move getMove(DraughtsState ds) {
        try {
            List<Move> moves = ds.getMoves();
            GameNode node = new GameNode(ds.clone());
            
            return rootAlphaBeta(node, -10000, 10000, 1, this.maxDepth);
        } catch (Exception ex) {
            Logger.getLogger(MelqartPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
}
