package nl.tue.s2id90.group08;

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
public class PlayerLVL3 extends DraughtsPlayer {
    private final int maxDepth = 6;
    private int value;
    private int count;
    
    
    public PlayerLVL3() {
        super(PlayerLVL3.class.getResource("resources/squirtle.jpg"));
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
   
    int alphaBeta(NodeLVL3 node, int alpha, int beta, int player, int depth) 
            throws Exception{
        count++;
        DraughtsState state = node.getState();
        if (depth == 0){
            return player * node.getValue(node.getState());
        }
        for (Move move : state.getMoves()){
            state.doMove(move);
            alpha = max(alpha, -alphaBeta(new NodeLVL3(state.clone()), 
                    -beta, -alpha, -(player), depth - 1));
            state.undoMove(move);
            if (beta >= alpha){
                return alpha;
            }
        }
        return alpha;
    }
    
    Move rootAlphaBeta(NodeLVL3 node, int alpha, int beta, int player, int depth) 
            throws Exception {
        Move bestMove = null;
        this.value = -10000;
        DraughtsState state = node.getState();
        List<Move> moves = state.getMoves();
        
        // When only one move is possible, immediately choose that one.
        if (moves.size() == 1){
            return moves.get(0);
        }
        
        for (Move move : moves){
            state.doMove(move);
            alpha = max(alpha, -alphaBeta(new NodeLVL3(state.clone()), 
                    -beta, -alpha, -(player), depth - 1));
            state.undoMove(move);
            if (alpha > this.value){
                this.value = alpha;
                bestMove = move;
            }
        }
        System.out.println("PlayerLVL3 count:" + count);
        count = 0;
        if (bestMove == null){
            
            // When no move is found and depth equals two, choose a random one.
            if (depth <= 2){
                Collections.shuffle(moves);
                return moves.get(0);
            }
            
            // Lower the depth when no best move is found.
            return rootAlphaBeta(node, alpha, beta, player, depth - 1);
        }
        return bestMove;
    }

    @Override
    public Move getMove(DraughtsState ds) {
        try {
            List<Move> moves = ds.getMoves();
            NodeLVL3 node = new NodeLVL3(ds.clone());
            
            return rootAlphaBeta(node, -10000, 10000, 1, this.maxDepth);
        } catch (Exception ex) {
            Logger.getLogger(PlayerLVL3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
}
