package org.crawler.db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.crawler.model.VuzeMsg;
import org.crawler.websocket.SessionHandler;

/*

Hebra dedicada al transpaso de los mensajes generados por Vuze

---------------------------------------------------------------------------------------------------------
Descripción:

Esta hebra juega un papel fundamental en la aplicación, y tiene varias funciones


*/

public class HebraMsg extends Thread {

    public static String strLine;
    public final Set<Session> ses = new HashSet<>();
    public int msgid=0;
    public boolean active=true;
    
    public HebraMsg() {
        super();
    }
    
    public HebraMsg(Session sesion) {
        super();
        ses.add(sesion);
    }
    
    @Override
    public void run() {

        SQLiteDatabase test = new SQLiteDatabase();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        String minute_new;
        String minute_mem = null;
        String temp = null;
        int counterPing=0;
        int counterFindN = 0;
        int counterFindV = 0;

        try {
            fis = new FileInputStream("C:\\Users\\usuario\\Desktop\\netbeaaaans\\ping.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String[] strcoord = null;
            
            boolean init=false;
            while(!init){
                strLine = br.readLine();
                if (strLine == null || !active) {
                    //System.out.println("Actualizado2");
                    Thread.sleep(1000);
                } else {
                    strcoord = strLine.split(",");
                    //test.addMessages(coord.get(0).type, coord.get(0).timestamp, coord.get(0).data);
                    //addVuzeMessage(new VuzeMsg(0, strcoord[0], strcoord[1], strcoord[2]),ses);
                    minute_mem = strcoord[1].substring(14,16);
                    minute_mem = String.valueOf(Integer.parseInt(minute_mem)-1);
                    temp = strcoord[1];
                    System.out.println(temp);
                    init=true;
                }  
            }
            
            while (true) {
                strLine = br.readLine();
                if (strLine == null || !active) {
                    //System.out.println("Actualizado2");
                    Thread.sleep(5000);
                } else {
                    strcoord = strLine.split(",");
                    
                    //test.addMessages(coord.get(0).type, coord.get(0).timestamp, coord.get(0).data);
                    addVuzeMessage(new VuzeMsg(0, strcoord[0], strcoord[1], strcoord[2]));
                    minute_new = strcoord[1];
                    minute_new = minute_new.substring(14, 16);
                    if (!minute_new.equals(minute_mem)) {
                        addGraphMessage(temp.substring(0, 14) + minute_mem + ":00", counterPing + counterFindN + counterFindV, 0);
                        addGraphMessage(temp.substring(0, 14) + minute_mem + ":00", counterPing, 1);
                        addGraphMessage(temp.substring(0, 14) + minute_mem + ":00", counterFindN, 2);
                        addGraphMessage(temp.substring(0, 14) + minute_mem + ":00", counterFindV, 3);

                        counterPing = 0;
                        counterFindN = 0;
                        counterFindV = 0;

                        temp = strcoord[1];
                        minute_mem = minute_new;
                    }
                    //System.out.println(strcoord[0]);
                    switch (strcoord[0]) {
                        case "PING":
                            counterPing++;
                            break;
                        case "FIND_NODE":
                            counterFindN++;
                            break;
                        case "FIND_VALUE":
                            counterFindV++;
                            break;
                        default:
                            System.out.println("WFT");
                            break;

                    }
                    //System.out.println("Minuto:" + minute.substring(14, 16));
                }
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    public void newSession(Session newSession){
        ses.add(newSession);
    }
    
    public void deleteSession(Session newSession){
        ses.remove(newSession);
    }

    public void addVuzeMessage(VuzeMsg msg) {
        msg.setId(msgid);
        msgid++;
        JsonObject addMessage = createAddMessage(msg);
        sendToAllConnectedSessions(addMessage);
    }
    
    public void addGraphMessage(String x, int y, int group) {
        JsonObject graphMessage = createGraphMessage(x,y,group);
        sendToAllConnectedSessions(graphMessage);
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
    
        private JsonObject createGraphMessage(String x, int y, int group) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "graph")
                .add("x", x)
                .add("y", y)
                .add("group", group)
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
    
    private void sendToAllConnectedSessions(JsonObject message) {
        ses.forEach((session) -> {
            sendToSession(session, message);
        });
    }
    
    public void stopThread(){
        active= false;
    }
    
    public void resumeThread(){
        active=true;
    }
    
}
