package org.crawler.db;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.crawler.model.Coordinates;
import org.crawler.model.VuzeMsg;
import org.crawler.websocket.SessionHandler;

public class HebraCoord extends Thread{
    public static String strLine;
    
    public Session ses;

    public HebraCoord(Session sesion) {
        super();
        ses=sesion;
    }

     public void run(){
         
        SQLiteDatabase test = new SQLiteDatabase();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try{
            ArrayList<Coordinates> coord = new ArrayList<Coordinates>();
            fis = new FileInputStream("C:\\Users\\usuario\\Desktop\\netbeaaaans\\coordinates.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String[]strcoord = null;
            while (true){
                strLine = br.readLine();
                if(strLine == null) {
                    Thread.sleep(5000);
                    System.out.println("Actualizado1");
                }
                else{
                    strcoord = strLine.split(",");
                    coord.add(new Coordinates(Double.parseDouble(strcoord[0]),Double.parseDouble(strcoord[1]),Double.parseDouble(strcoord[2]), strcoord[3] ));
                    test.addCoords(coord.get(0).x,coord.get(0).y,coord.get(0).h,coord.get(0).timestamp);
                    coord.clear();
                }
                
            }
        }
    catch (Exception ex){
        System.err.println("Error: " + ex.getMessage());
    }
    
    }
     
      
      
}
