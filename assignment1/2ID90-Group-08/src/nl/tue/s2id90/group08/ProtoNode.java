/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group08;

import static java.lang.Math.floor;
import nl.tue.s2id90.draughts.Draughts;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author jvhoof
 */
public class ProtoNode {

    private static DraughtsState ds;
    private static boolean white;

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

    // Constructor
    public ProtoNode(DraughtsState ds) {
        ProtoNode.ds = ds.clone();
        white = ds.isWhiteToMove();
    }

    public DraughtsState getState() {
        return ds;
    }

    /* Empty = 0
     * Whitepiece = 1
     * Blackpiece = 2
     * Whiteking = 3
     * Blackking = 4
     * Whitefield = 5
     */
    public int getValue() {
        int total = 0;
        int leftWhite = 0;
        int middleWhite = 0;
        int rightWhite = 0;
        int leftBlack = 0;
        int middleBlack = 0;
        int rightBlack = 0;
        int pieces = 0;
        int blackKings = 0;
        int whiteKings = 0;
        
        for (int c = 0; c <= 9; c++) {
            for (int r = 0; r <= 9; r++) {
                switch (ds.getPiece(r, c)) {
                case 0: // empty
                    break;
                case 1: // whitepiece
                    total += white ? 150 : 100;  // Own pieces get higher values
                    total += white ? 0 : 45 - r * 5; // Distance to promotion line
                    total += white ? PLAYGROUND[r][c] : 0;
                    break;
                case 2: // blackpiece
                    total -= white ? 100 : 150; // Own pieces get higher values
                    total -= white ? r * 10 : 0; // Distance to promotion line
                    total -= white ? 0 : PLAYGROUND[9-r][9-c];
                    break;
                case 3:  // whiteking
                    total += 300;
                    break;
                case 4: // blackking
                    total -= 300;
                    break;
                case 5: // whitefield
                    break;
                }
            }
        }
        
        return total;
    }
}
