
package pruebas;

import java.io.*;


public class archivos {
    
    public static long linea_coordenadas;
    
    public void init(){
        linea_coordenadas=0;
    }
    
    public void readCoordinates(String direccion){
        
        try{
            BufferedReader bf = new BufferedReader(new FileReader(direccion));
            String temp = bf.readLine();
            System.out.println(temp.substring(0,8) + "  ");
            System.out.println(temp.substring(0,8) + "  ");
            System.out.println(temp.substring(0,8) + "  ");
            System.out.println(temp.substring(0,8) + "\n");
            bf.close();
        }catch(Exception e){}
        
    }
    
}
