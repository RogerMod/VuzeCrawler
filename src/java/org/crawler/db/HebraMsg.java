package org.crawler.db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.crawler.model.VuzeMsg;
import org.crawler.websocket.SessionHandler;

public class HebraMsg extends Thread {

    public static String strLine;
    public Session ses;
    public int msgid=0;
    public boolean active=true;
    
    public HebraMsg() {
        super();
    }
    
    public HebraMsg(Session sesion) {
        super();
        ses = sesion;
    }
    
    @Override
    public void run() {

        SQLiteTest test = new SQLiteTest();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            ArrayList<VuzeMsg> coord = new ArrayList<VuzeMsg>();
            fis = new FileInputStream("C:\\Users\\usuario\\Desktop\\netbeaaaans\\ping.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String[] strcoord = null;
            while (true) {
                strLine = br.readLine();
                if (strLine == null || !active) {
                    //System.out.println("Actualizado2");
                    Thread.sleep(5000);
                } else {
                    strcoord = strLine.split(",");
                    coord.add(new VuzeMsg(0, strcoord[0], strcoord[1], strcoord[2]));
                    test.addMessages(coord.get(0).type, coord.get(0).timestamp, coord.get(0).data);
                    //addVuzeMessage(new VuzeMsg(0, strcoord[0], strcoord[1], strcoord[2]),ses);
                    coord.clear();
                }
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    public void addVuzeMessage(VuzeMsg msg, Session sesion) {
        msg.setId(msgid);
        msgid++;
        JsonObject addMessage = createAddMessage(msg);
        sendToSession(sesion, addMessage);
    }

    private JsonObject createAddMessage(VuzeMsg msg) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", msg.getId())
                .add("type", msg.getType())
                .add("timestamp", msg.getTimestamp())
                .add("data", msg.getData())
                .build();
        return addMessage;
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopThread(){
        active= false;
    }
    
    public void resumeThread(){
        active=true;
    }
    
}
