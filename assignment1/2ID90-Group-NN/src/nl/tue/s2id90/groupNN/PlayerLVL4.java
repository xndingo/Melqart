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
public class PlayerLVL4 extends DraughtsPlayer {
    private final int maxHeight = 8;
    private int bestValue = - 1000000;

    private int value;
    private int countNodes;
    private int countMoves;
    
    
    public PlayerLVL4() {
        super(PlayerLVL4.class.getResource("resources/squirtle.jpg"));
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
   
    
    int alphaBeta(NodeLVL4 node, int alpha, int beta, 
        int player, int depth, int maxDepth) {
        DraughtsState ds = node.getState();
        countNodes++;
        
        if (depth == maxDepth || ds.isEndState() || stopped) {
            return node.getValue(player);
        }
        
        for (Move move : ds.getMoves()) {
            ds.doMove(move);
            alpha = min(alpha, -alphaBeta(new NodeLVL4(ds.clone()),
                    -beta, -alpha, -player, depth + 1, maxDepth));
            if (beta >= alpha){
                return alpha;
            }
            ds.undoMove(move);
        }
        return alpha;
    }
    
    Move rootAlphaBeta(NodeLVL4 node, int alpha, int beta, 
        int player, int depth, int maxDepth) {
        
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        
        DraughtsState ds = node.getState();
               
        for (Move move : ds.getMoves()) {
            countMoves++;
            ds.doMove(move);

            int score = -alphaBeta(new NodeLVL4(ds.clone()), 
                    alpha, beta, player, 0, maxDepth);
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
            ds.undoMove(move);
        }
        System.out.println("#LVL4 Calculated nodes: " + countNodes);
        System.out.println("#LVL4 Calculated Moves: " + countMoves);
        System.out.println("#LVL4 Best score: " + bestScore);
        return bestMove;
    }
    
    @Override
    public Move getMove(DraughtsState ds) {
        countNodes = 0;
        countMoves = 0;
        NodeLVL4 node = new NodeLVL4(ds.clone());
        return rootAlphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1,
                0, 6);
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
}
