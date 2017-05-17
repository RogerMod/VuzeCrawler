package org.crawler.websocket;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.crawler.model.VuzeMsg;
import org.crawler.db.*;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    public static boolean init = false;
    HebraMsg hebra2 = new HebraMsg();

    @Inject
    private SessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        System.out.println("Sesion creada");
        sessionHandler.addSession(session);
        if (init != true) {
            try {
                SQLiteTest test = new SQLiteTest();
                test.getConnection();
                System.out.println("Conexion con DB");
                /*
                HebraCoord hebra1 = new HebraCoord();
                hebra1.start();
                
                hebra2 = new HebraMsg(session);
                hebra2.start();
                */
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Algo malo pasa");
            }
            init = true;
        }
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) throws SQLException, InterruptedException {
        System.out.println("MENSAJE RECIBIDO");
        hebra2.stopThread();
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            System.out.println(jsonMessage.getString("type"));
            System.out.println(jsonMessage.getString("object"));
            if ("retrieve".equals(jsonMessage.getString("type"))) {
                if ("PING".equals(jsonMessage.getString("object"))) {
                    System.out.println("Se solicita ping");
                    sessionHandler.retrieveInfo("PING");
                } else if ("FIND_NODE".equals(jsonMessage.getString("object"))) {
                    System.out.println("Se solicita find node");
                    sessionHandler.retrieveInfo("FIND_NODE");
                } else if ("FIND_VALUE".equals(jsonMessage.getString("object"))) {
                    System.out.println("Se solicita find value");
                    sessionHandler.retrieveInfo("FIND_VALUE");
                }
            }
            hebra2.resumeThread();
        }
    }

}
