/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Game class containing the game logic
 * @author Kornel
 */
public class Game extends JPanel implements ActionListener {

    private final boolean DEBUG = false;

    private final int width = 640;
    private final int height = 640;
    private final Dimension size = new Dimension(this.width, this.height);
    private final int tileWidth;
    private final int n = 16;
    private final int DELAY = 300;
    private final Keyboard keyboard = new Keyboard(this);
    private final GameFrame gameFrame;
    private boolean gameOver = false;

    private Image background;
    private Random rnd = new Random();

    private final int stoneCount = 3;
    private ArrayList<Point> rocks;
    private Point apple;
    private ArrayList<Point> snake;
    private char direction;
    private char[] directions = {'w', 'a', 's', 'd'};

    private Timer timer;
    private Date startDate;
    private final DateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private final Font baseFont = new Font("Verdana", Font.BOLD, 40);

    /**
     * This is the constructor of the Game class, which sets up the game window,
     * loads the game assets, generates the initial state of the game, starts
     * the game timer, and sets the start date. It also adds a key listener to
     * the game window.
     *
     * @param gameFrame
     */
    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.tileWidth = this.width / this.n;

        this.setPreferredSize(this.size);
        this.setMinimumSize(this.size);
        this.setMaximumSize(this.size);
        this.setLocation(0, 0);

        try {
            background = ImageIO.read(getClass().getResource("/images/sand.jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.generateSnake();
        this.generateRocks();
        this.newApple();
        this.direction = this.directions[this.getRandom(0, 4)];

        this.setFocusable(true);
        this.addKeyListener(keyboard);
        this.requestFocus();

        timer = new Timer(DELAY, this);
        timer.start();
        this.startDate = new Date();
    }

    /**
     * This code is an overridden method that paints the game screen. It first
     * calls the superclass method to ensure that the component is painted
     * correctly. Then, it draws the background image and calls the draw method
     * to draw the game objects on top of it.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.background, 0, 0, null);
        this.draw(g);
    }

    /**
     * This method is responsible for drawing the game graphics using the
     * provided Graphics object. It performs the following tasks:
     *
     * If DEBUG mode is enabled, it draws a grid of rectangles representing the
     * game board. If the game is over, it draws the game over screen with the
     * final score and "Game over!" text. It draws the apple as a red oval,
     * stones as gray rectangles, and the snake (snake) as orange and green
     * rectangles. It draws the current score and elapsed time using the
     * baseFont and dateFormat objects. The fillRect() and fillOval() methods of
     * the Graphics object are used to draw the shapes at the appropriate
     * positions on the game board based on their respective coordinates (x and
     * y) multiplied by the tileWidth value.
     *
     * @param g
     */
    private void draw(Graphics g) {
        //draw debug frame
        if (this.DEBUG) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    g.drawRect(i * this.tileWidth, j * this.tileWidth, this.tileWidth, this.tileWidth);
                }
            }
        }
        String scoreText = "Score: " + (this.snake.size() - 2);
        if (this.gameOver) {
            //draw score and time
            g.setColor(Color.BLACK);
            g.setFont(this.baseFont);
            g.drawString(scoreText, (this.width - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, g.getFont().getSize());
            String gameOverText = "Game over!";
            g.setFont(new Font(this.baseFont.getFontName(), this.baseFont.getStyle(), 75));
            g.drawString(gameOverText, (this.width - getFontMetrics(g.getFont()).stringWidth(gameOverText)) / 2, this.height / 2);
            return;
        }

        //draw apple
        g.setColor(Color.red);
        g.fillOval(this.apple.x * this.tileWidth, this.apple.y * this.tileWidth, this.tileWidth, this.tileWidth);
        //draw stones
        g.setColor(Color.gray);
        for (int i = 0; i < this.stoneCount; i++) {
            g.fillRect(this.rocks.get(i).x * this.tileWidth, this.rocks.get(i).y * this.tileWidth, this.tileWidth, this.tileWidth);
        }
        //draw snake
        g.setColor(Color.ORANGE);
        g.fillRect(this.snake.get(0).x * this.tileWidth, this.snake.get(0).y * this.tileWidth, this.tileWidth, this.tileWidth);
        g.setColor(Color.green);
        for (int i = 1; i < this.snake.size(); i++) {
            g.fillRect(this.snake.get(i).x * this.tileWidth, this.snake.get(i).y * this.tileWidth, this.tileWidth, this.tileWidth);
        }

        //draw score and time
        g.setColor(Color.BLACK);
        g.setFont(this.baseFont);
        FontMetrics metrics = getFontMetrics(g.getFont());
        Date diff = new Date(TimeUnit.MILLISECONDS.convert(new Date().getTime() - this.startDate.getTime(), TimeUnit.MILLISECONDS));
        g.drawString(scoreText, (this.width - metrics.stringWidth(scoreText)) / 2, g.getFont().getSize());
        g.drawString(this.dateFormat.format(diff), (this.width - metrics.stringWidth(this.dateFormat.format(diff))) / 2, g.getFont().getSize() * 2);
    }

    /**
     * This method displays a dialog box when the game is over. The dialog box
     * shows the player's final score and prompts them to enter their name to
     * save their result to a database using the saveToDatabase() method.
     */
    private void showGameOverDialog() {
        final int score = this.snake.size() - 2;
        final String result = JOptionPane.showInputDialog(null, "Congratulations!\nYour final score was: " + score, "Save your result!", JOptionPane.INFORMATION_MESSAGE);
        if (result != null) {
            this.saveToDatabase(result, score);
        }
    }

    /**
     * This function saves the player's name and score to a database table named
     * 'snake' using an SQL INSERT statement. It takes in two parameters, the
     * player's name and their score. The name is saved as a string and the
     * score as an integer.
     *
     * @param name
     * @param score
     */
    private void saveToDatabase(String name, int score) {
        this.gameFrame.insertRecord("INSERT INTO snake (name, score) VALUES ('" + name + "', " + score + ");");
    }

    /**
     * The generateSnake() method initializes the snake ArrayList with two Point
     * objects representing the starting position of the snake. The starting
     * position of the snake is calculated as follows:
     *
     * The variable x is assigned the value (n / 2) - 1, where n is the size of
     * the game board. This sets the starting position of the snake in the
     * middle of the game board horizontally.
     *
     * The variable y is also assigned the value (n / 2) - 1. This sets the
     * starting position of the snake in the middle of the game board
     * vertically.
     *
     * Two Point objects with the x and y values calculated above are then added
     * to the snake ArrayList.
     */
    private void generateSnake() {
        this.snake = new ArrayList<Point>();
        int x = (n / 2) - 1;
        int y = (n / 2) - 1;
        this.snake.add(new Point(x, y));
        this.snake.add(new Point(x, y));
    }

    /**
     * This function generates a new apple by randomly selecting coordinates and
     * checking if they collide with any rocks or parts of the snake. If a
     * collision is detected, it generates new coordinates and tries again. Once
     * it finds non-colliding coordinates, it creates a new Point object with
     * these coordinates and assigns it to the instance variable 'apple'.
     */
    private void newApple() {
        int x, y;
        do {
            x = getRandom(0, n);
            y = getRandom(0, n);
        } while (collides(x, y));
        if (this.DEBUG) {
            System.out.println("new apple: " + x + " " + y);
        }
        this.apple = new Point(x, y);
    }

    /**
     * This function named collides which takes two integer arguments x and y.
     * The method checks if there is a collision between the coordinates (x,y)
     * and any of the rocks or the snake parts. If there is a collision, it
     * returns true, otherwise it returns false.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean collides(int x, int y) {
        if (this.rocks != null) {
            for (Point rock : rocks) {
                if (rock.x == x && rock.y == y) {
                    return true;
                }
            }
        }
        if (this.snake != null) {
            for (Point part : snake) {
                if (part.x == x && part.y == y) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function generates a specified number of rocks and adds them to an
     * ArrayList. It does this by repeatedly generating random coordinates until
     * it finds a location that does not collide with any existing rocks or the
     * snake's body.
     */
    private void generateRocks() {
        this.rocks = new ArrayList();
        int x, y;
        for (int i = 0; i < this.stoneCount; i++) {
            do {
                x = getRandom(0, n);
                y = getRandom(0, n);
            } while (collides(x, y));
            if (this.DEBUG) {
                System.out.println("new stone: " + x + " " + y);
            }
            this.rocks.add(new Point(x, y));
        }
    }

    /**
     * This function returns a random integer within a given range.
     *
     * @param min
     * @param max
     * @return
     */
    private int getRandom(int min, int max) {
        return rnd.nextInt(max - min) + min;
    }

    /**
     * This function moves the snake by removing the tail and adding a new head
     * based on the direction of movement.
     */
    private void move() {
        this.snake.remove(this.snake.size() - 1);
        Point prev = this.snake.get(0);
        int x = (this.direction == 'a') ? -1 : (this.direction == 'd' ? 1 : 0);
        int y = (this.direction == 'w') ? -1 : (this.direction == 's' ? 1 : 0);
        this.snake.add(0, new Point(prev.x + x, prev.y + y));
    }

    /**
     * Checks if the game is over by determining if the snake collides with
     * walls, rocks, or itself.
     */
    private void isGameOver() {
        Point head = this.snake.get(0);
        if (head.x < 0 || head.x > n || head.y < 0 || head.y > n) {
            this.gameOver = true;
            return;
        }

        for (int i = this.snake.size() - 1; i > 0; i--) {
            if (head.x == this.snake.get(i).x && head.y == this.snake.get(i).y) {
                this.gameOver = true;
                return;
            }
        }

        for (int i = 0; i < this.rocks.size(); i++) {
            if (head.x == this.rocks.get(i).x && head.y == this.rocks.get(i).y) {
                this.gameOver = true;
                return;
            }
        }
    }

    /**
     * Checks if the snake has eaten the apple, and if so, calls the
     * incrementSnake() and newApple() methods to increase the length of the
     * snake and create a new apple.
     */
    private void isAppleEaten() {
        if (this.apple == null) {
            return;
        }
        Point head = this.snake.get(0);
        if (this.apple.x == head.x && this.apple.y == head.y) {
            this.incrementSnake();
            this.newApple();
        }
    }

    /**
     * This function creates a new point and adds it to the end of the snake.
     */
    private void incrementSnake() {
        Point prev = this.snake.get(this.snake.size() - 1);
        this.snake.add(new Point(prev.x, prev.y));
    }

    /**
     * This is an ActionListener's method that handles game logic. It calls the
     * move(), isGameOver(), and isAppleEaten() methods, updates the game board
     * with repaint(), and displays a game over dialog if the game is over.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!this.gameOver) {
            this.move();
            this.isGameOver();
            this.isAppleEaten();
        }
        this.repaint();
        if (this.gameOver) {
            this.timer.stop();
            this.showGameOverDialog();
        }
    }

    /**
     * This function sets the direction of an object to the given character.
     *
     * @param c
     */
    public void setDirection(char c) {
        this.direction = c;
    }
}
