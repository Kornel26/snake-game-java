/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database class that handles database functionality
 * @author Kornel
 */
public class Database {

    private static Connection conn;
    private static final String CONNECTIONSTRING = "jdbc:mysql://localhost/assignment_snake?user=root&password=root";

    /**
     * The Database constructor connects to the database using the connect()
     * method, which sets up the connection to the database.
     */
    public Database() {
        connect();
    }

    /**
     * This is a static method connect() that establishes a database connection
     * using the DriverManager class. The connection string is defined by a
     * constant CONNECTIONSTRING. If there is no existing connection, the method
     * creates one, otherwise, it does nothing. If there is an error in
     * establishing a connection, the method prints out the error message to the
     * console.
     */
    public static void connect() {
        if (conn == null) {
            try {
                conn = (Connection) DriverManager.getConnection(CONNECTIONSTRING);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * The executeQuery method executes a SQL query on the database connection
     * and returns the result set. It takes a query string as an input parameter
     * and returns a ResultSet object, which contains the results of the query.
     * The method first establishes a connection to the database using the
     * connect() method and then creates a statement object using the
     * connection. It then executes the query and returns the result set.
     *
     * @param query
     * @return
     */
    public static ResultSet executeQuery(String query) {
        connect();
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return rs;
    }

    /**
     * This method allows inserting data into the connected database by
     * executing the given query using a Statement object. The method first
     * establishes a connection to the database by calling the connect() method,
     * and then creates a Statement object to execute the query. If the query is
     * successfully executed, the method completes. Otherwise, any SQLExceptions
     * that occur are caught and printed to the console.
     *
     * @param query
     */
    public static void insert(String query) {
        connect();
        try {
            try ( Statement stmt = conn.createStatement()) {
                stmt.execute(query);
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
}
