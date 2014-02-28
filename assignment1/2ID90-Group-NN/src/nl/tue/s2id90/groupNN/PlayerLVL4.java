package nl.tue.s2id90.groupNN;

import static java.lang.Math.max;
import static java.lang.Math.min;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

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

    private int miniMax(NodeLVL4 node, int depth, int alpha, int beta) {
        DraughtsState ds = node.getState();
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
        int depth = 6;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestScore = -Integer.MAX_VALUE;
        NodeLVL4 node = new NodeLVL4(ds.clone());
        Move bestMove = null;
        for (Move move : ds.getMoves()) {
            ds.doMove(move);
            if (bestMove == null) {
                bestMove = move;
            }
            alpha = max(alpha, miniMax(new NodeLVL4(ds.clone()), depth - 1, alpha, beta));
            if (alpha > bestScore) {
                bestMove = move;
                bestScore = alpha;
            }
            ds.undoMove(move);
        }

        System.out.println("#LVL4 Best score: " + bestScore);
        return bestMove;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
