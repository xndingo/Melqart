package nl.tue.s2id90.group08;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.contest.util.TimedSearchTask;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;
import nl.tue.s2id90.contest.util.TimedSearchTask;

/**
 * @author Jeroen van Hoof
 * @author Theodoros Margomenos
 */
public class PlayerLVL4 extends DraughtsPlayer {

    private int value;
    private boolean stopped = false;

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

    @Override
    public void stop() {
        stopped = true;
    }

    private int miniMax(NodeLVL4 node, int depth, int alpha, int beta) throws AIStoppedException {
        DraughtsState ds = node.getState();
        if (stopped){
            stopped = false;
            throw new AIStoppedException();
        }
        
        if (depth == 0 || ds.isEndState()) {
            stopped = false;
            return node.getValue();
        }
        if (!ds.isWhiteToMove()) {
            for (Move move : ds.getMoves()) {
                ds.doMove(move);
                // Maximize
                alpha = max(alpha, miniMax(new NodeLVL4(ds.clone()), depth - 1, alpha, beta));

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
                beta = min(beta, miniMax(new NodeLVL4(ds.clone()), depth - 1, alpha, beta));

                if (alpha >= beta) {
                    return alpha;
                }
                ds.undoMove(move);
            }
            return beta;
        }
    }

    @Override
    public Move getMove(DraughtsState ds) {
        NodeLVL4 node = new NodeLVL4(ds.clone());
        Move bestMove = null;
        
        // Get moves and shuffle them.
        List<Move> moves = ds.getMoves();
        Collections.shuffle(moves);
        int nrOfMoves = moves.size();
        
        // Determine depth.
        int depth = 6;
        
        if (nrOfMoves < 12){
            depth = 8;
        }
        if (nrOfMoves < 7){
            depth = 10;
        }
        if (nrOfMoves < 5){
            depth = 12;
        }
        if (nrOfMoves == 1){
            return moves.get(0);
        }
        System.out.println("#LVL4 Calculating at depth: " + depth);
        
        // Set alpha, beta and bestScore to (-)infinity.
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestScore = Integer.MIN_VALUE;
        
        // Determine best move.
        for (Move move : moves) {
            ds.doMove(move);
            if (bestMove == null) {
                bestMove = move;
            }
            // If timer stopped, exit move calculation and use best found move.
            try {
                alpha = max(alpha, miniMax(new NodeLVL4(ds.clone()), 
                        depth - 1, alpha, beta));
            } catch (AIStoppedException ex) {
                System.out.println("Timer stopped.");
                ds.undoMove(move);
                break;
            }
            if (alpha > bestScore) {
                bestMove = move;
                bestScore = alpha;
            }
            ds.undoMove(move);
        }

        // Return the best move.
        System.out.println("#LVL4 Best score: " + bestScore);
        return bestMove;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
