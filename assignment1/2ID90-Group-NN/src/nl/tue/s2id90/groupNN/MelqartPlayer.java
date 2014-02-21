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
 * @author Theodoros Margomenos
 */
public class MelqartPlayer extends DraughtsPlayer {

    public Integer getValue() {
        return 0;
    }

    // Material
    final static int KING = 30;      // Number of kings
    final static int DRAUGHT = 10;   // Number of draughts

    // Infastructure
    final double PLAY = 0.4;  // Playground
    final int MOVE = 4;       // Number of possible moves
    final int MIRROR = 5;     // Number of opposing draughts
    final int DISTR = 5;      // Distribution of draughts in three columns

    // Strategy
    final int SIDE = 6;       // Number of draughts on the sides
    final int LAST = 7;       // Number of draughts on last line
    final int STEPS = 4;      // Manhattan distance to end (except Kings)
    final int DEF = 8;        // Number of defended draughts

    /**
     * Evaluates the current DraughtsState.
     *
     * @param ds The DraughtsState.
     * @return The value of the current state.
     */
    public static int evaluate(DraughtsState ds) {
        int total = 0;

        // White or black has to move.
        boolean isWhite = ds.isWhiteToMove();
        // Possible moves.
        int moves = ds.getMoves().size();

        for (int c = 0; c <= 10; c++) {
            for (int r = 0; r <= 10; r++) {
                total += addValue(r, c, isWhite, ds);
            }
        }

        return total;
    }

    public static int addValue(int r, int c, boolean isWhite, DraughtsState ds) {
        int piece = ds.getPiece(r, c);
        if (piece != DraughtsState.WHITEFIELD
                && piece != DraughtsState.EMPTY) {
            if (isWhite) {
                if (piece == DraughtsState.WHITEKING) {
                    return 50 + KING;
                } else {
                    return calcPlayground(r, c, true) + DRAUGHT;
                }
            } else {
                if (piece == DraughtsState.BLACKKING) {
                    return 50 + KING;
                } else {
                    return calcPlayground(r, c, false) + DRAUGHT;
                }
            }

        } else {
            return 0;
        }
    }

    public static int calcPlayground(int y, int x, boolean white) {
        int triangleR = 0, triangleL = 0;

        // Flip board for black
        if (!white) {
            x = 10 - x + 1;
            y = 10 - y + 1;
        }

        // Calculate the big triangle
        int bigTriangle = y / 2 * (y + 1);

        // Overflow on right side
        int baseR = y - 1 + x - 10;
        if (baseR > 0) {
            // Calculate triangle right
            triangleR = (baseR - 1) / 2 * baseR;
        }

        // Overflow on left side
        int baseL = y - x;
        if (baseL > 0) {
            // Calculate triangle left
            triangleL = (baseL + 1) / 2 * baseL;
        }

        return bigTriangle - triangleR - triangleL;

    }

    /**
     * It applies the alpha-beta min-max algorithm given a game node
     * {@code node}, an integer alpha, an integer beta and if it's a maximizing
     * player or not as a boolean.
     *
     *
     * @param node game node
     * @param alpha integer
     * @param beta integer
     * @param maximizingPlayer boolean
     * @return alpha or beta integers
     */
    public int alphaBeta(GameNode node, int alpha, int beta, boolean maximizingPlayer) {
        System.out.println("Executing alphabeta...");
        GameState state = node.getState();
        List<Move> moves = state.getMoves();
        if (maximizingPlayer) {
            for (Move move : moves) {
                state.doMove(move);
                // recursive call
                alpha = max(alpha, alphaBeta(node, alpha, beta, maximizingPlayer));
                if (beta <= alpha) {
                    break; //b cut off
                }
                state.undoMove(move);
            }
            node.setBestMove(alpha);
            return alpha;
        } else { //minimizingPlayer
            for (Move move : moves) {
                state.doMove(move);
                // recursive call
                beta = min(beta, alphaBeta(node, alpha, beta, true));
                if (beta <= alpha) {
                    break; //a cut off
                }
                state.undoMove(move);
            }
            node.setBestMove(beta);
            return beta;
        }
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

int alphaBeta (GameNode node, int depth, int alpha, int beta, boolean maximizingPlayer){
        GameState state = node.getState();
        List<Move> moves = state.getMoves();
        if (depth == 0 || state.isEndState()) {
            return node.getValue();
        } 
        if (maximizingPlayer) {
            for (Move move : moves){
                state.doMove(move);
                // recursive call
                alpha = max(alpha, alphaBeta((GameNode) state, depth-1, alpha, beta, false));
                if (beta <= alpha) {
                    break; //b cut off
                }
                state.undoMove(move);                                
            }
            node.setBestMove(alpha);
            return alpha;           
        }
        else { //minimizingPlayer
            for (Move move : moves){
                state.doMove(move);
                // recursive call
                beta = min(beta, alphaBeta((GameNode) state, depth-1, alpha, beta, true));
                if (beta <= alpha) {
                    break; //a cut off
                }
                state.undoMove(move);                     
            }
            node.setBestMove(beta);
            return beta; 
        }
}

    @Override
    public Move getMove(DraughtsState s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
