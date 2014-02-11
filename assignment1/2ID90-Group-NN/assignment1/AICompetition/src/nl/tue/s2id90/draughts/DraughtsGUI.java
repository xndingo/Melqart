/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.draughts;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nl.tue.s2id90.contest.GameGUI;
import nl.tue.s2id90.contest.HumanMoveListener;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.BoardState;
import org10x10.dam.game.Move;
import org10x10.dam.game.MoveBoardListener;
import org10x10.dam.game.MoveSelector;
import org10x10.dam.game.MoveSelectorAdvanced;
import org10x10.dam.ui.Board;
import org10x10.dam.ui.DefaultFieldDecorator;
import org10x10.dam.ui.LastMoveListener;
import org10x10.dam.ui.MoveListener;
import org10x10.dam.ui.swing.SwingBoardPanel;
import org10x10.dam.ui.swing.movelist.JMoveList;
import org10x10.dam.ui.swing.movelist.MoveListModel;

/**
 *
 * @author huub
 */
public class DraughtsGUI implements GameGUI<DraughtsState,DraughtsPlayer,Move> {

    private SwingBoardPanel boardPanel;
    private JMoveList moveList;
    private JLabel numberOfPiecesLabel;
    private final MoveListModel moves = new MoveListModel(new ArrayList<Move>());
    private MoveBoardListener moveBoardListener;
    private DraughtsState ds;

    @Override
    public JPanel getBoardPanel() {
        if (boardPanel == null) {
            boardPanel = new SwingBoardPanel();
            boardPanel.setScalable(true);
            boardPanel.setPreferredSize(new Dimension(400, 400));
            Board board = boardPanel.getBoard();
            MoveSelector ms = new MoveSelectorAdvanced(board.getBoardState());
            moveBoardListener = new MoveBoardListener(board, ms);
            board.addBoardListener(moveBoardListener);
            moveBoardListener.setEnabled(false);
            board.addMoveListener(new LastMoveListener(new DefaultFieldDecorator()));
        }
        return boardPanel;
    }

    @Override
    public List<JComponent> getPanels() {
        if (moveList == null) {
            moveList = new JMoveList();
            moveList.setModel (moves);
            moveList.setName("moves"); // name as used in tabbedPane
            numberOfPiecesLabel=new JLabel("-");
            numberOfPiecesLabel.setHorizontalAlignment(JLabel.CENTER);
            numberOfPiecesLabel.setName("progress");
        }
        List<JComponent> panelList = new ArrayList<>();
        panelList.add(moveList);
        panelList.add(numberOfPiecesLabel);
        return panelList;
    }

    @Override
    public void show(DraughtsState gs) {
        Board board = boardPanel.getBoard();
        BoardState bs = convert(gs, board.getBoardState());
        board.startUpdate();
        board.setBoardState(bs);
        board.endUpdate();
        
        updatePieceCount(gs);
        
        moveList.setSelectedIndex(moves.size() - 1);
        moveList.repaint();
    }
    
    static private BoardState convert(DraughtsState ds, BoardState target) {
        BoardState bs = target==null?new BoardState(10,10) : target;
        bs.setPieces(ds.getPieces());
        bs.setWhiteToMove(ds.isWhiteToMove());
        return bs;
    }
    
    private void updatePieceCount(DraughtsState gs) {
        int[] pieces = gs.getPieces();
        
        int whites=0, blacks=0;
        for(int f=1; f<pieces.length; f=f+1) {
            int piece = pieces[f];
            if (Draughts.isWhite(piece)) whites++;
            else if (Draughts.isBlack(piece)) blacks++;
        }
        String status = ""+whites + " - " + blacks;
        numberOfPiecesLabel.setText(status);
    }

    @Override
    public DraughtsState getGameState() {
        if (ds==null) {
            ds = new DraughtsState() {
                @Override
                public void doMove(Move m) { // automatically update movelist when
                    // gamestate is updated
                    super.doMove(m);
                    moves.add(m);
                }

                @Override
                public void reset() { // reset movelist when gamestate is updated
                    super.reset();
                    moves.clear();
                }
            };
        }
        return ds;
    }

    @Override
    /** enables one human move and calls the listener when that move has been
     * played.
     */
    public void enableASingleHumanMove(final HumanMoveListener<Move> hml) {
        if (hml!=null) {
            moveBoardListener.setEnabled(true);
            Board board = boardPanel.getBoard();

            /**
             * add move listener that that provides a callback
             * after a human made a move on the board and then
             * also de-registers itself.
             */
            final MoveListener ml = new MoveListener() {
                @Override
                public void onMoveForward(Board b, Move m) {
                    b.removeMoveListener(this); // note: this==ml
                    moveBoardListener.setEnabled(false);
                    hml.onMove(m);    // callback
                }

                @Override
                public void onMoveBackward(Board b, Move m) {  }

                @Override
                public boolean isEnabled() { return true; }
            };
            
            board.addMoveListener(ml);

        }
    }

    @Override
    public void setGameStatusText(String status) {
        
    }
    
    
}   
