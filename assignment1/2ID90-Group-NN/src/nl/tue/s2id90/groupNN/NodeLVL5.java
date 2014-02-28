package nl.tue.s2id90.groupNN;

import static java.lang.Math.abs;
import nl.tue.s2id90.draughts.DraughtsState;


/**
 * @author Theodoros Margomenos
 * @author Jeroen van Hoof
 */
public class NodeLVL5 {

    private static DraughtsState ds;

    // Constructor
    public NodeLVL5(DraughtsState ds) {
        if (ds == null) {
            throw new IllegalArgumentException("gs in gamenode");
        }
        NodeLVL5.ds = ds.clone();
    }
       
    /**
     * Gets the best move that can be made.
     * @return 
     */
    public int getBestMove() {
        return 0; //testing        
    }

  
    /**
     * Gets the game state of this node.
     * @return 
     */
    public DraughtsState getState() {
        return NodeLVL5.ds;
    }
    
    /**
     * Sets the best move to can be made.
     * @param move
     */
    public void setBestMove(int move) {
        throw new UnsupportedOperationException("Not supported yet."); 
        
    }

    /**
     *
     * @return
     */
    public Integer getValue() {
        // Calculate the balance for white
        int total = 0;
        for (int c = 0; c < 10; c++) {
            for (int r = 0; r < 10; r++) {
                total += addValue(r, c);
            }
        }
        
        return total;
    }

    // Material
    final static int KING = 350;      // Number of kings
    final static int DRAUGHT = 100;   // Number of draughts
    
    final static int[][] PLAYGROUND = new int[][]{
        {0,  1,  0,  1,  0,  1,  0,  1,  0,  1},
        {2,  0,  3,  0,  3,  0,  3,  0,  3,  0},
        {0,  5,  0,  6,  0,  6,  0,  6,  0,  4},
        {6,  0,  9,  0,  10, 0,  10, 0,  8,  0},
        {0,  11, 0,  14, 0,  15, 0,  13, 0,  9},
        {12, 0,  17, 0,  20, 0,  19, 0,  15, 0},
        {0,  19, 0,  24, 0,  25, 0,  22, 0,  16},
        {20, 0,  27, 0,  30, 0,  29, 0,  24, 0},
        {0,  29, 0,  34, 0,  35, 0,  32, 0,  25},
        {30, 0,  37, 0,  40, 0,  39, 0,  34, 0}
    };

    
    public static int addValue(int r, int c) {
        int piece = ds.getPiece(r, c);
        int whiteScore = 0;
        int blackScore = 0;
        
        if (piece == DraughtsState.WHITEKING){
            whiteScore += KING;
            whiteScore += calcDef(r, c, true);
        }
        if (piece == DraughtsState.WHITEPIECE){
            whiteScore += DRAUGHT;
            whiteScore += PLAYGROUND[r][c];   
            whiteScore += calcDef(r, c, true);

        }
        if (piece == DraughtsState.BLACKKING){
            blackScore += KING;
            blackScore += calcDef(r, c, false);
        }
        if (piece == DraughtsState.BLACKPIECE){
            blackScore += DRAUGHT;
            blackScore += PLAYGROUND[9-r][9-c];
            blackScore += calcDef(r, c, false);
        }

        return whiteScore - blackScore;
        
    }       
    
    public static int calcDef(int row, int col, boolean isPieceWhite){
        
        int total = 0;
        int topleft, topright, bottomleft, bottomright;
        
        
        if (row > 0 && col > 0){
            topleft = ds.getPiece(row-1, col-1);
            total += addDef(topleft, isPieceWhite);
        } else {
            total += 10;
        }
        if (row > 0 && col < 9){
            topright = ds.getPiece(row-1, col+1);
            total += addDef(topright, isPieceWhite);
        } else {
            total += 10;
        }
        if (row < 9 && col > 0){
            bottomleft = ds.getPiece(row+1, col-1);
            total += addDef(bottomleft, isPieceWhite);
        } else {
            total += 10;
        }
        if (row < 9 && col < 9){
            bottomright = ds.getPiece(row+1, col+1);
            total += addDef(bottomright, isPieceWhite);
        } else {
            total += 10;
        }
        
        return total;
  
    }
    
    public static int addDef(int piece, boolean isPieceWhite){
        // Check if piece is the same.
        if (isPieceWhite && piece == DraughtsState.WHITEPIECE 
                || piece == DraughtsState.WHITEKING){
            return 5;
        }
        if (!isPieceWhite && piece == DraughtsState.BLACKPIECE ||
                piece == DraughtsState.BLACKKING){
            return 5;
        }
        
        return 0;
    }
    
}