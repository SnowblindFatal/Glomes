/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import de.matthiasmann.twl.Widget;

/**
 *
 * @author juho
 */
public abstract class GameStateTemplate extends Widget {
    Glomes game;
    public GameStateTemplate(Glomes newgame){
        game = newgame;
    }
    public abstract void use();
}

