package pruebas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HebraMsg extends Thread{
    public static String strLine;
    
     public void run(){
         
        SQLiteTest test = new SQLiteTest();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try{
            ArrayList<Messages> coord = new ArrayList<Messages>();
            fis = new FileInputStream("C:\\Users\\usuario\\Desktop\\netbeaaaans\\ping.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String[]strcoord = null;
            while (true){
                strLine = br.readLine();
                if(strLine == null) {
                    System.out.println("Actualizado2");
                    Thread.sleep(5000);
                }
                else{
                    strcoord = strLine.split(",");
                    coord.add(new Messages(strcoord[0],strcoord[1],strcoord[2]));
                    test.addMessages(coord.get(0).type,coord.get(0).timestamp,coord.get(0).data);
                    //System.out.println(coord.get(0).timestamp);
                    coord.clear();
                }
                
            }
        }
    catch (Exception ex){
        System.err.println("Error: " + ex.getMessage());
    }
    
    }
}
