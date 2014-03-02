package nl.tue.s2id90.groupNN;

import nl.tue.s2id90.groupNN.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.List;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class PlayerLVL7 extends DraughtsPlayer {

    private int value;
    private boolean stopped = false;
    private boolean white;
    private int lastScore;

    public PlayerLVL7() {
        super(PlayerLVL7.class.getResource("resources/squirtle.jpg"));
    }

    @Override
    public void stop() {
        stopped = true;
    }
 
    private int miniMax(NodeLVL7 node, int depth, int alpha, int beta, 
            boolean player) throws AIStoppedException {
        DraughtsState ds = node.getState();
        if (stopped){
            stopped = false;
            throw new AIStoppedException();
        }
        
        if (depth == 0 || ds.isEndState()) {
            if (white) {
                return node.getValue();
            } else {
                return -node.getValue();
            }
        }
        if (player) {
            for (Move move : ds.getMoves()) {
                ds.doMove(move);
                // Maximize
                alpha = max(alpha, miniMax(new NodeLVL7(ds.clone()), 
                        depth - 1, alpha, beta, false));

                if (alpha >= beta) {
                    return beta;
                }
                ds.undoMove(move);
            }
            return alpha;
        } else {
            for (Move move : ds.getMoves()) {
                ds.doMove(move);
                //Minimize
                beta = min(beta, miniMax(new NodeLVL7(ds.clone()), depth - 1, alpha, beta, true));

                if (alpha >= beta) {
                    return alpha;
                }
                ds.undoMove(move);
            }
            return beta;
        }
    }
    
    public Move findBestMove(int depth, DraughtsState ds) throws AIStoppedException {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int tempScore = Integer.MIN_VALUE;
        List<Move> moves = ds.getMoves();
        Move tempMove = null;
        for (Move move : moves) {
            ds.doMove(move);
                    // If timer stopped, exit move calculation and use best found move.
            // Maximize current player.
            alpha = max(alpha, miniMax(new NodeLVL7(ds.clone()),
                    depth - 1, alpha, beta, false));
            ds.undoMove(move);
            if (alpha > tempScore) {
                tempMove = move;
                tempScore = alpha;
            }
        }
        
        this.lastScore = tempScore;
        return tempMove;
    }
    
    @Override
    public Move getMove(DraughtsState ds) {
        white = ds.isWhiteToMove();
        
        NodeLVL7 node = new NodeLVL7(ds.clone());
        Move bestMove = null;
        Move tempMove = null;
        
        // Starting depth.
        int depth = 6;
        int finishedDepth = 6;
        
        // Determine best move.
        while (true) {
            depth++; // Go to next depth.
            try {
                tempMove = findBestMove(depth, ds);
            } catch (AIStoppedException ex) {
                System.out.println("#LVL7: Depth at " + finishedDepth);
                break;
            }
            finishedDepth++;
            bestMove = tempMove;
            this.value = this.lastScore;
        }
    
        // Return the best move.
        return bestMove;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
