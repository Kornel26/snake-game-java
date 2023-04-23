/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;

/**
 * This class defines a GUI game window that allows the user to play a game,
 * reset it, view records, and exit. It also allows the user to insert new
 * records into a database and display the top 10 records in a table.
 *
 * @author Kornel
 */
public class GameFrame extends JFrame implements ActionListener {

    private Game game;
    private final int windowHeight = 640;
    private final int windowWidth = 640;
    private final JMenuItem newGameMenuItem = new JMenuItem("New game");
    private final JMenuItem recordsMenuItem = new JMenuItem("Records");
    private final JMenuItem exitMenuItem = new JMenuItem("Exit");

    /**
     * The GameFrame() constructor creates a new game window with a JMenuBar,
     * containing a JMenu called "Game". It sets the initial size and position
     * of the window, and adds a new Game object to it. It also sets the window
     * to be non-resizable and displays it to the user. The menu items on the
     * menu bar respond to user clicks using the actionPerformed() function.
     */
    public GameFrame() {
        this.game = new Game(this);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        newGameMenuItem.addActionListener(this);
        gameMenu.add(newGameMenuItem);
        recordsMenuItem.addActionListener(this);
        gameMenu.add(recordsMenuItem);
        exitMenuItem.addActionListener(this);
        gameMenu.add(exitMenuItem);

        menuBar.add(gameMenu);
        this.setJMenuBar(menuBar);

        this.setSize(this.windowHeight + menuBar.getHeight(), this.windowWidth);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(game);
        this.setResizable(false);
        this.pack();

        this.setVisible(true);
    }

    /**
     * The actionPerformed() function listens for menu item clicks and performs
     * different actions based on the clicked item. If the clicked item is
     * newGameMenuItem, the game is reset, if it is recordsMenuItem, the game
     * records are displayed, and if it is exitMenuItem, the game window is
     * closed.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameMenuItem) {
            this.resetGame();
        } else if (e.getSource() == recordsMenuItem) {
            this.showRecords();
        } else if (e.getSource() == exitMenuItem) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * The insertRecord() function inserts a new record into the snake database
     * table using the provided sql statement, which contains the details of the
     * new record to be inserted.
     *
     * @param sql
     */
    public void insertRecord(String sql) {
        Database.insert(sql);
    }

    /**
     * The showRecords() function displays the top 10 scores from the snake
     * database table in a JTable object within a new JFrame, or a message if
     * there are no records.
     */
    private void showRecords() {
        JFrame frame = new JFrame();

        String[] columnNames = {"Name", "Score"};
        ResultSet rs = Database.executeQuery("SELECT name, score FROM snake ORDER BY score DESC, id ASC LIMIT 10");
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            while (rs.next()) {
                dataList.add(new Object[]{rs.getString("name"), rs.getInt("score")});
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        Object[][] data = new Object[dataList.isEmpty() ? 1 : dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        if (dataList.isEmpty()) {
            data[0] = new Object[]{"No records has been saved yet!", 0};
        }
        JTable table = new JTable(data, columnNames);

        frame.add(table);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * The resetGame() function refreshes the game by replacing the current game
     * object with a new instance of Game, and updating the UI components.
     */
    public void resetGame() {
        this.remove(game);
        game = new Game(this);
        this.add(game);
        SwingUtilities.updateComponentTreeUI(this);
        game.requestFocus();
    }

}
