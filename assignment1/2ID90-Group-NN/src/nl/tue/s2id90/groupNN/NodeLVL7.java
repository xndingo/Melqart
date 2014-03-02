package nl.tue.s2id90.groupNN;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class NodeLVL7 {

    // Setting global variables.
    private int param1, param2, param3, param4, param5, param6,
            param7, param8, param9, param10, param11, param12;

    /* Playground is defined as the number of reachable squares from a
     * particular square.
     */
    final static int[][] PLAYGROUND = new int[][]{
        {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
        {2, 0, 3, 0, 3, 0, 3, 0, 3, 0},
        {0, 5, 0, 6, 0, 6, 0, 6, 0, 4},
        {6, 0, 9, 0, 10, 0, 10, 0, 8, 0},
        {0, 11, 0, 14, 0, 15, 0, 13, 0, 9},
        {12, 0, 17, 0, 20, 0, 19, 0, 15, 0},
        {0, 19, 0, 24, 0, 25, 0, 22, 0, 16},
        {20, 0, 27, 0, 30, 0, 29, 0, 24, 0},
        {0, 29, 0, 34, 0, 35, 0, 32, 0, 25},
        {30, 0, 37, 0, 40, 0, 39, 0, 34, 0}
    };

    // Copy of the DraughtsState.
    private static DraughtsState ds;

    /**
     * Constructor.
     * In the constructor, we set up several parameters which will be used
     * to evaluate the game. The idea comes from this paper:
     * http://alpha.mini.pw.edu.pl/~mandziuk/PRACE/es_init.pdf
     * Some heuristics are left out, because they are unimportant,
     * inapplicable for International Draughts, or require heavy calculation.
     * 
     * @param ds a node in the DraughtsState
     */
    public NodeLVL7(DraughtsState ds) {
        if (ds == null) {
            throw new IllegalArgumentException("gs in gamenode");
        }
        NodeLVL7.ds = ds.clone();
        //1. Number of pawns;
        param1 = 100;
        //2. Number of kings;
        param2 = 300;
        //3. Number of safe pawns (i.e. adjacent to the edge of the board);
        param3 = 20;
        //4. Number of safe kings;
        param4 = 10;
        //5. Number of moveable pawns (i.e. able to perform a move other than capturing).
        param5 = 5;
        //6. Number of moveable kings. Parameters 5 and 6 are calculated taking no notice of
        //capturing priority;
        param6 = 10;
        //7. Aggregated distance of the pawns to promotion line;
        param7 = 10;
        //8. Number of unoccupied fields on promotion line.
        param8 = 10;
        //9. Number of defender pieces (i.e. the ones situated in two lowermost rows);
        param9 = 20;
        //10. Number of attacking pawns (i.e. positioned in three topmost rows);
        param10 = 10;
        //11. Number of loner pieces. Loner piece is defined as the one not adjacent to any other
        param11 = -20;
        //12. Number of holes, i.e. empty squares adjacent to at least three pieces of the same color.
        param12 = -20;
    }

    /**
     * Retrieve the evaluation of the current state.
     * 
     * @return the evaluation of this state.
     */
    public Integer getValue() {
        return totalEval();
    }

    /**
     * Gets the best move that can be made.
     *
     * @return
     */
    public int getBestMove() {
        return 0; //testing        
    }

    /**
     * Retrieves a copy of the current GameState.
     *
     * @return the current GameState
     */
    public DraughtsState getState() {
        return NodeLVL7.ds;
    }

    /**
     * Sets the best move to can be made.
     *
     * @param move
     */
    public void setBestMove(int move) {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    /**
     * Evaluation method for white pieces. 
     * Yes, we are racist.
     * 
     * @param r row
     * @param c column
     * @param topLeft piece to the top left of current piece
     * @param topRight piece to the top right of current piece
     * @param bottomLeft piece to the bottom left of current piece
     * @param bottomRight piece to the bottom right of current piece
     * @return 
     */
    public int evalWhitePiece(int r, int c, int topLeft,
            int topRight, int bottomLeft, int bottomRight) {
        int total = 0;

        //1. Number of pawns;
        total += param1;

        //3. Number of safe pawns (i.e. adjacent to the edge of the board);
        if (c == 9 || c == 0) {
            total += param3;
        }

        //5. Number of moveable pawns (i.e. able to perform a move other than capturing).
        if (topLeft == 0) {
            total += param5;
        } else if (topRight == 0) {
            total += param5;
        }

        //7. Aggregated distance of the pawns to promotion line;
        total += 9 * param7 - r * 1 * param7;

        //9. Number of defender pieces (i.e. the ones situated in two lowermost rows);
        if (r > 7) {
            total += param9;
        }

        //10. Number of attacking pawns (i.e. positioned in three topmost rows);
        if (r < 3) {
            total += param10;
        }

        //11. Number of loner pawns. Loner piece is defined as the one not adjacent to any other
        int adjacent = 0;
        adjacent += (Draughts.isWhite(topLeft)) ? 1 : 0;
        adjacent += (Draughts.isWhite(topRight)) ? 1 : 0;
        adjacent += (Draughts.isWhite(bottomLeft)) ? 1 : 0;
        adjacent += (Draughts.isWhite(bottomRight)) ? 1 : 0;
        total += (adjacent == 0) ? param11 : 0;

        //13. Playground for pawns
        total += PLAYGROUND[r][c];

        return total;
    }

    /**
     * Evaluation method for kings.
     * 
     * @param r row
     * @param c column
     * @param topLeft piece to the top left of current piece
     * @param topRight piece to the top right of current piece
     * @param bottomLeft piece to the bottom left of current piece
     * @param bottomRight piece to the bottom right of current piece
     * @return 
     */
    public int evalKing(int r, int c, int topLeft,
            int topRight, int bottomLeft, int bottomRight) {
        int total = 0;

        //2. Number of kings;
        total += param2;
        
        //4. Number of safe kings;
        if (c == 9 || c == 0) {
            total += param4;
        }

        //6. Number of moveable kings. Parameters 5 and 6 are calculated taking no notice of
        //capturing priority;
        boolean moveable = false;

        if (c > 0 && r < 9) {
            if (ds.getPiece(r + 1, c - 1) == DraughtsState.EMPTY) {
                moveable = true;
            }
        } else if (c < 9 && r < 9) {
            if (ds.getPiece(r + 1, c + 1) == DraughtsState.EMPTY) {
                moveable = true;
            }
        } else if (c > 0 && r > 0) {
            if (ds.getPiece(r - 1, c - 1) == DraughtsState.EMPTY) {
                moveable = true;
            }
        } else if (c < 9 && r > 0) {
            if (ds.getPiece(r - 1, c + 1) == DraughtsState.EMPTY) {
                moveable = true;
            }
        }
        if (moveable) {
            total += param6;
        }
        return total;
    }

    /**
     * Evaluation method for black pieces. 
     * Yes, we are racist.
     * 
     * @param r row
     * @param c column
     * @param topLeft piece to the top left of current piece
     * @param topRight piece to the top right of current piece
     * @param bottomLeft piece to the bottom left of current piece
     * @param bottomRight piece to the bottom right of current piece
     * @return 
     */    
public int evalBlackPiece(int r, int c, int topLeft,
            int topRight, int bottomLeft, int bottomRight) {
        int total = 0;

        //1. Number of pawns;
        total += param1;

        //3. Number of safe pawns (i.e. adjacent to the edge of the board);
        if (c == 9 || c == 0) {
            total += param3;
        }

        //5. Number of moveable pawns (i.e. able to perform a move other than capturing).
        if (bottomLeft == 0) {
            total += param5;
        } else if (bottomRight == 0) {
            total += param5;
        }

        //7. Aggregated distance of the pawns to promotion line;
        total += r * param7;

        //9. Number of defender pieces (i.e. the ones situated in two lowermost rows);
        if (r < 3) {
            total += param9;
        }

        //10. Number of attacking pawns (i.e. positioned in three topmost rows);
        if (r > 6) {
            total += param10;
        }

        //11. Number of loner pawns. Loner piece is defined as the one not adjacent to any other
        int adjacent = 0;
        adjacent += (Draughts.isBlack(topLeft)) ? 1 : 0;
        adjacent += (Draughts.isBlack(topRight)) ? 1 : 0;
        adjacent += (Draughts.isBlack(bottomLeft)) ? 1 : 0;
        adjacent += (Draughts.isBlack(bottomRight)) ? 1 : 0;
        total += (adjacent == 0) ? param11 : 0;

        //13. Playground for pawns
        total += PLAYGROUND[9-r][9-c];

        return total;
    }
        
    public int evalEmpty(int r, int c, int topLeft, 
            int topRight, int bottomLeft, int bottomRight){
        int total = 0;
        //12. Number of holes, i.e. empty squares adjacent to at least three 
        //pieces of the same color.
        int adjacent = 0;
        adjacent += (Draughts.isWhite(topLeft)) ? 1 : 0;
        adjacent += (Draughts.isWhite(topRight)) ? 1 : 0;
        adjacent += (Draughts.isWhite(bottomLeft)) ? 1 : 0;
        adjacent += (Draughts.isWhite(bottomRight)) ? 1 : 0;
        total += (adjacent >= 3) ? param11 : 0;
        adjacent = 0;
        adjacent += (Draughts.isBlack(topLeft)) ? 1 : 0;
        adjacent += (Draughts.isBlack(topRight)) ? 1 : 0;
        adjacent += (Draughts.isBlack(bottomLeft)) ? 1 : 0;
        adjacent += (Draughts.isBlack(bottomRight)) ? 1 : 0;
        total -= (adjacent >= 3) ? param11 : 0;
            return total;
    }

    /**
     * Splits evaluation per piece and adds up to total.
     * 
     * @return the total evaluation.
     */
    public int totalEval() {
        int topLeft, topRight, bottomLeft, bottomRight, piece, total = 0;
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 10; r++) {
                
                // Determine current piece
                piece = ds.getPiece(r, c);
                
                // Skip whitefields.
                if (piece == DraughtsState.WHITEFIELD) {
                    continue;
                }
                
                // Determine adjacent pieces
                if (r > 0 && c > 0) {
                    topLeft = ds.getPiece(r - 1, c - 1);
                } else {
                    topLeft = 6;
                }
                if (r > 0 && c < 9) {
                    topRight = ds.getPiece(r - 1, c + 1);
                } else {
                    topRight = 6;
                }
                if (r < 9 && c > 0) {
                    bottomLeft = ds.getPiece(r + 1, c - 1);
                } else {
                    bottomLeft = 6;
                }
                if (r < 9 && c < 9) {
                    bottomRight = ds.getPiece(r + 1, c + 1);
                } else {
                    bottomRight = 6;
                }
                
                // Determine evaluation function for current piece.
                if (piece == DraughtsState.WHITEPIECE){
                    total += evalWhitePiece(r, c, topLeft, topRight, bottomLeft, bottomRight);
                } else if (piece == DraughtsState.BLACKPIECE){
                    total -= evalBlackPiece(r, c, topLeft, topRight, bottomLeft, bottomRight);
                } else if (piece == DraughtsState.WHITEKING){
                    total += evalKing(r, c, topLeft, topRight, bottomLeft, bottomRight);
                } else if (piece == DraughtsState.BLACKKING){
                    total -= evalKing(r, c, topLeft, topRight, bottomLeft, bottomRight);
                } else {
                    total += evalEmpty(r, c, topLeft, topRight, bottomLeft, bottomRight);
                }
            }
        }
        return total;
    }    
}
