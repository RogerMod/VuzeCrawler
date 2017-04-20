
package pruebas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FileToDB {
    public static ArrayList<Coordinates> getCoordinatesFromText(String filepath){
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        ArrayList<Coordinates> coord = new ArrayList<Coordinates>();
        try{
            fis = new FileInputStream(filepath);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            
            String line= null;
            
            String[]strcoord = null;
            while((line=br.readLine()) != null){
                strcoord = line.split(",");
                coord.add(new Coordinates(Double.parseDouble(strcoord[0]),Double.parseDouble(strcoord[1]),Double.parseDouble(strcoord[2]), strcoord[3] ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                br.close();
                isr.close();
                fis.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        return coord;
    }
    
    public static ArrayList<Messages> getMessagesFromText(String filepath){
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        ArrayList<Messages> ping = new ArrayList<Messages>();
        try{
            fis = new FileInputStream(filepath);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            
            String line= null;
            
            String[]strcoord = null;
            while((line=br.readLine()) != null){
                strcoord = line.split(":");
                ping.add(new Messages(strcoord[0],strcoord[1],strcoord[2].substring(1)));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                br.close();
                isr.close();
                fis.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        return ping;
    }
    
}
