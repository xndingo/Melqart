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
import org10x10.dam.game.Move;

/**
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class PlayerLVL6 extends DraughtsPlayer {

    private int value;
    private boolean stopped = false;
    private boolean white;

    public PlayerLVL6() {
        super(PlayerLVL6.class.getResource("resources/squirtle.jpg"));
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

    @Override
    public void stop() {
        stopped = true;
    }

    private int miniMax(NodeLVL6 node, int depth, int alpha, int beta, 
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
                alpha = max(alpha, miniMax(new NodeLVL6(ds.clone()), 
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
                beta = min(beta, miniMax(new NodeLVL6(ds.clone()), depth - 1, alpha, beta, true));

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
            alpha = max(alpha, miniMax(new NodeLVL6(ds.clone()),
                    depth - 1, alpha, beta, false));
            ds.undoMove(move);
            if (alpha > tempScore) {
                tempMove = move;
                tempScore = alpha;
            }
        }
        
        return tempMove;
    }
    
    @Override
    public Move getMove(DraughtsState ds) {
        white = ds.isWhiteToMove();
        
        NodeLVL6 node = new NodeLVL6(ds.clone());
        Move bestMove = null;
        Move tempMove = null;
        
        // Starting depth.
        int depth = 0;
        int finishedDepth = 0;
        
        // Determine best move.
        while (true) {
            depth++; // Go to next depth.
            try {
                tempMove = findBestMove(depth, ds);
            } catch (AIStoppedException ex) {
                System.out.println("Depth at " + finishedDepth);
                break;
            }
            finishedDepth++;
            bestMove = tempMove;
        }
    
        // Return the best move.
        return bestMove;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
