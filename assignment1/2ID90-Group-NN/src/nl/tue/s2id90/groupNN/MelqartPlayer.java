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
    
    public MelqartPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }
    @Override
    /** @return a random move **/
    public Move getMove(DraughtsState s) {
        List<Move> moves = s.getMoves();
        Collections.shuffle(moves);
        return moves.get(0);
    }

    @Override
    public Integer getValue() {
        return 0;
    }
    
    // Material
    final int KING = 30;      // Number of kings
    final int DRAUGHT = 10;   // Number of draughts
    
    // Infastructure
    final double PLAY = 0.4;       // Playground
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
    double evaluate(DraughtsState ds){
        double total = 0;
        boolean white = ds.isWhiteToMove();
        total = evalMaterial(ds, white) + evalInfrastructure(ds, white);
        
        return total;
    }

    /**
     * Evaluates the material value.
     * 
     * @param ds The DraughtsState.
     * @param white If true, then white has to move.
     * @return The value of the material of the player that has to move.
     */
    int evalMaterial(DraughtsState ds, boolean white){
        int material = 0;
        int[] pieces = ds.getPieces();
        int draught = DraughtsState.WHITEPIECE;
        int king = DraughtsState.WHITEKING;
        if (!white){
            draught = DraughtsState.BLACKPIECE;
            king = DraughtsState.BLACKKING;
        }
        for (int piece : pieces){
            if (piece == draught){
                material += DRAUGHT;
            } else if (piece == king){
                material += KING;
            }
        }
        return material;
    }
    
    /**
     * Evaluate the infrastructure.
     * Calculates the possible moves, the playground size per piece,
     * the distribution over 3 sectors and the number of pieces that are in
     * a row of 3 or more.
     * 
     * @param ds The current DraughtsState.
     * @param white If true, white has to move.
     * @return The evaluation score.
     */
    double evalInfrastructure(DraughtsState ds, boolean white){
        // Possible moves.
        int moves = ds.getMoves().size();
        
        // Calculate size of playground per piece.
        int[] pieces = ds.getPieces();
        int playgrounds = 0;
        for (int i = 1; i <= 50; i++){
            if (pieces[i] == DraughtsState.WHITEPIECE 
                    || pieces[i] == DraughtsState.BLACKPIECE){
                // Determine column x and row y.
                int x = 0, y = 0, triangleR = 0, triangleL = 0;
                y = (int) Math.ceil(i/5);
                if (i % 10 == 6){ x = 1; }
                if (i % 10 == 1){ x = 2; }
                if (i % 10 == 7){ x = 3; }
                if (i % 10 == 2){ x = 4; }
                if (i % 10 == 8){ x = 5; }
                if (i % 10 == 3){ x = 6; }
                if (i % 10 == 9){ x = 7; }
                if (i % 10 == 4){ x = 8; }
                if (i % 10 == 0){ x = 9; }
                if (i % 10 == 5){ x = 10; }
                
                // Flip the board for black.
                if (!white){
                    x = 10 - x + 1;
                    y = 10 - y + 1;
                }
                
                // Calculate the big triangle
                int bigTriangle = y/2*(y+1);
                // Overflow on right side
                int baseR = y - 1 + x - 10;
                if (baseR > 0){
                    // Calculate triangle right
                    triangleR = (baseR-1)/2*baseR;  
                }
                
                // Overflow on left side
                int baseL = y - x;
                if (baseL > 0){
                    // Calculate triangle left
                    triangleL = (baseL+1)/2*baseL;  
                }
                
                int playground = bigTriangle - triangleR - triangleL;
                playgrounds += playground;
            } else if (pieces[i] == DraughtsState.WHITEKING 
                    || pieces[i] == DraughtsState.BLACKKING){
                // Everything is reachable for a king.
                playgrounds += 50; 
            }
        }
        
        // Distribution over 3 sectors (calculate own disadvantage per column)
        int whiteleft = 0, blackleft = 0, whiteright = 0, blackright = 0, whitecenter = 0, blackcenter = 0;
        for (int i = 1; i <= 50; i++){
            if (i%10 == 1 || i%10 == 6 || i%10 == 7){ // left column
                if(pieces[i] == DraughtsState.WHITEPIECE || pieces[i] == DraughtsState.WHITEKING){
                    whiteleft++;
                } else if(pieces[i] == DraughtsState.BLACKPIECE || pieces[i] == DraughtsState.BLACKKING){
                    blackleft++;
                }
            } else if (i%10 == 0 || i%10 == 4 || i%10 == 5){ // right column
                if(pieces[i] == DraughtsState.WHITEPIECE || pieces[i] == DraughtsState.WHITEKING){
                    whiteright++;
                } else if(pieces[i] == DraughtsState.BLACKPIECE || pieces[i] == DraughtsState.BLACKKING){
                    blackright++;
                }
            } else { // middle column
                if(pieces[i] == DraughtsState.WHITEPIECE || pieces[i] == DraughtsState.WHITEKING){
                    whitecenter++;
                } else if (pieces[i] == DraughtsState.BLACKPIECE || pieces[i] == DraughtsState.BLACKKING){
                    blackcenter++;                
                }
            }
        }        
        int distribution = whiteleft-blackleft + whiteright-blackright + whitecenter-blackcenter;
        if (!white){distribution -= distribution;}
        
        // Number of pieces that are in a row of 3 or more
        
        // Return
        return DISTR * distribution + MOVE * moves + PLAY * playgrounds;
    }
    
    /**
     * It applies the alpha-beta min-max algorithm given a 
     * game node {@code node}, an integer alpha, an integer beta
     * and if it's a maximizing player or not as a boolean.     * 
     * @param node game node
     * @param alpha integer
     * @param beta integer
     * @param maximizingPlayer boolean
     * @return alpha or beta integers
     */
    public int alphaBeta (GameNode node, int alpha, int beta, boolean maximizingPlayer){
        GameState state = node.getState();
        List <Move> moves = state.getMoves();
        if (maximizingPlayer) {
            for (Move move : moves){
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
        }
        else { //minimizingPlayer
            for (Move move : moves){
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
     * Returns the amount of pieces that are on the board
     * given a game state {@code gs}
     * @param gs game state of the board
     * @return the amount of pieces that are on the board. 0 if empty.
     */
    public int getPieceCount(DraughtsState gs) {
        int[] pieces = gs.getPieces();
        int count = 0;
        for (int f = 1; f < pieces.length; f = f+1) {
            int piece = pieces[f];
            if (Draughts.isWhite(piece) || Draughts.isBlack(piece)) {
                count++;            
            }
        }        
        return count; //0 if no pieces on the board.
    }   

    private boolean stopped = false ;
    @Override
    public void stop ( ) { 
        stopped = true ; 
    }
    int alphaBeta(GameNode node, int alpha, int beta) 
        throws AIStoppedException {
        if (stopped) {
            stopped = false;
            throw new AIStoppedException();
        }
    //.. .
        return 0;
    }
}