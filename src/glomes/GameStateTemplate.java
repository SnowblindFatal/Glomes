/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

/**
 *
 * @author juho
 */
public abstract class GameStateTemplate {
    Glomes game;
    public GameStateTemplate(Glomes newgame){
        game = newgame;
    }
    public abstract void run();
}

