package org.crawler.websocket;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.crawler.db.SQLiteDatabase;

import org.crawler.model.VuzeMsg;

@ApplicationScoped
public class SessionHandler {

    private int messgId = 0;
    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void addMessage(VuzeMsg obj) {
        obj.setId(messgId);
        messgId++;
        JsonObject addMessage = createAddMessage(obj);
        sendToAllConnectedSessions(addMessage);
    }

    private JsonObject createAddMessage(VuzeMsg messg) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", messg.getId())
                .add("timestamp", messg.getTimestamp())
                .add("type", messg.getType())
                .add("data", messg.getData())
                .build();
        return addMessage;
    }

    public void retrieveInfo(String type) throws SQLException {

        SQLiteDatabase test = new SQLiteDatabase();
        ResultSet rs = null;

        switch (type) {
            case "PING":
                try {
                    rs = test.displayPings();
                } catch (SQLException | ClassNotFoundException ex) {
                }
                break;
            case "FIND_NODE":
                try {
                    rs = test.displayFindNodes();
                } catch (SQLException | ClassNotFoundException ex) {
                }
                break;
            case "FIND_VALUE":
                try {
                    rs = test.displayFindValues();
                } catch (SQLException | ClassNotFoundException ex) {
                }
                break;
        }
        VuzeMsg obj = new VuzeMsg();
        while (rs.next()) {
            obj.setData(rs.getString("data"));
            obj.setTimestamp(rs.getString("timestamp"));
            obj.setType(rs.getString("type"));
            //System.out.println(rs.getString("type") + rs.getString("timestamp") + rs.getString("data"));
            addMessage(obj);
        }

    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
