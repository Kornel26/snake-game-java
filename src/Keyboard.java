/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Keyboard class that handles keyboard inputs
 * @author Kornel
 */
public class Keyboard extends KeyAdapter {

    private final Game game;

    /**
     * This function creates a new instance of the Keyboard class and sets the
     * Game instance variable to the given Game object. The Keyboard class is
     * used to handle user input from the keyboard for the Game class.
     *
     * @param game
     */
    public Keyboard(Game game) {
        this.game = game;
    }

    /**
     * When a key is pressed, it checks if it is one of the allowed keys for
     * movement ('w', 'a', 's', or 'd'). If it is, it calls the setDirection
     * method of the Game class with the pressed key as an argument to update
     * the direction of the snake.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        char c = Character.toLowerCase(e.getKeyChar());
        if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
            this.game.setDirection(c);
        }
    }

}
