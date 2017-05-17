package org.crawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTest {

    private static Connection con;
    //private static boolean hasData = false;

    public ResultSet displayCoords() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT x, y, h, timestamp FROM coords");
        return res;
    }

    public ResultSet displayMessages() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT type, timestamp, data FROM messages");

        return res;
    }

    public ResultSet displayPings() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
        System.out.println("CALLED displayFindValues()");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT type, timestamp, data FROM messages WHERE type IS 'PING'");
        
        return res;
    }

    public ResultSet displayFindNodes() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT type, timestamp, data FROM messages WHERE type IS 'FIND_NODE'");
        return res;
    }

    public ResultSet displayFindValues() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT type, timestamp, data FROM messages WHERE type IS 'FIND_VALUE'");
        return res;
    }

    public void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:C:/Users/usuario/"
                + "Desktop/netbeaaaans/VuzeCrawler/SQLiteTest1.db");
        initialize();

    }

    private void initialize() throws SQLException {
        /*
        if (!hasData) {
            hasData = true;
        }
        */
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' "
                + "AND name='coords'");
        if (!res.next()) {
            System.out.println("Building the coords table");
            Statement state2 = con.createStatement();
            state2.execute("CREATE TABLE coords(id integer,"
                    + "x double," + "y double,"
                    + "h double," + "timestamp varchar(60),"
                    + "primary key(id));");
        }

        Statement state3 = con.createStatement();
        ResultSet res2 = state3.executeQuery("SELECT name FROM sqlite_master WHERE type='table' "
                + "AND name='messages'");
        if (!res2.next()) {
            System.out.println("Building the messages table");
            Statement state4 = con.createStatement();
            state4.execute("CREATE TABLE messages(id integer,"
                    + "type varchar(60)," + "timestamp varchar(60),"
                    + "data varchar(60)," + "primary key(id));");
        }

    }

    public void addCoords(double xv, double yv, double hv, String timestamp) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("INSERT INTO coords values(?,?,?,?,?);");
        prep.setDouble(2, xv);
        prep.setDouble(3, yv);
        prep.setDouble(4, hv);
        prep.setString(5, timestamp);
        prep.execute();
    }

    public void addMessages(String type, String timestamp, String data) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("INSERT INTO messages values(?,?,?,?);");
        prep.setString(2, type);
        prep.setString(3, timestamp);
        prep.setString(4, data);
        prep.execute();
    }

}
