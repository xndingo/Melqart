/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.groupNN;

import java.util.Collections;
import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/** 
 * @author Jeroen van Hoof
 * @author Theodore Margomenos
 */
public class RandomDraughtsPlayer extends DraughtsPlayer{
    @Override
    public Move getMove (DraughtsState s) {
        List <Move> moves = s.getMoves();
        Collections.shuffle(moves);
        return moves.get(0);
    }
    
    @Override
    public Integer getValue ( ) { return 0 ; }
    @Override
    public void stop ( ) { }
    
}
