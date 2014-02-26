/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.groupNN;

import nl.tue.s2id90.draughts.DraughtsPlugin;



/**
 *
 * @author huub
 */
public class MyDraughtsPlugin extends DraughtsPlugin {
    public MyDraughtsPlugin() {
        // make two players available to the AICompetition tool
        // During the final competition you should make only your 
        // best player available. For testing it might be handy
        // to make more than one player available.
        super(
                new PlayerLVL0(), 
                new PlayerLVL1(), 
                new PlayerLVL2(), 
                new PlayerLVL3()
        );
    }
}