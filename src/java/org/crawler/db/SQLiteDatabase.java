package org.crawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*

Base de datos con SQLite

---------------------------------------------------------------------------------------------------------
Descripción:

Esta clase contiene todas las funciones necesarias para:

    1. Comprobar si existe la base de datos y crearla si no es encuentra.
    2. Crear las tablas en esa nueva base de datos.
    3. Hacer lecturas y escrituras en la misma.

---------------------------------------------------------------------------------------------------------
Inicialización:

La clase tiene una variable privada llamada 'con', de la clase connection, la cual tendrá valor null a no 
ser que haya sido inicializada.

En cada método que tiene esta clase (salvo aquellos métodos que se usan precisamente para inicializarla)
se comprueba si la variable 'con' tiene el valor null, y en caso afirmativo llama consecutivamente a dos 
métodos:

1. getConnection() -> Crea el archivo .db en el directorio especificado y llama a initialize()
2. initialize() -> Crea dos tablas dentro de la base de datos. El esquema cada una es:

Coordenadas: coords(id integer, x double, y double, h double, timestamp varchar(60), primary key(id))
Mensajes: messages(id integer, type varchar(60), timestamp varchar(60), data varchar(60), primary key(id))
    
---------------------------------------------------------------------------------------------------------
Métodos para la escritura:

    addCoords(double xv, double yv, double hv, String timestamp)
    addMessages(String type, String timestamp, String data)

Como sus propios nombres indican, añaden a la base de datos unas coordenadas y un mensaje en las tablas 
coords y messages respectivamente mediante una sentencia SQL.

---------------------------------------------------------------------------------------------------------
Métodos para la lectura:


                           ||  Tabla que consulta   ||  Información que extrae
                           ||                       ||
    displayCoords()        ||       coords          ||   Todos los valores x,y,h y timestamp
    displayPings()         ||       messages        ||   Tipo, timestamp y datos de los mensajes PING
    displayFindNodes()     ||       messages        ||   Tipo, timestamp y datos de los mensajes FIND_NODE
    displayFindNodes()     ||       messages        ||   Tipo, timestamp y datos de los mensajes FIND_VALUE
    displayMessages()      ||       messages        ||   Tipo, timestamp y datos de todos los mensajes


*/

public class SQLiteDatabase {

    private static Connection con;

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
                + "Desktop/netbeaaaans/Crawleer/VuzeDatabase.db");
        initialize();

    }

    private void initialize() throws SQLException {
        
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
