package pruebas;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HebraCoord extends Thread{
    public static String strLine;
    
     public void run(){
         
        SQLiteTest test = new SQLiteTest();
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
