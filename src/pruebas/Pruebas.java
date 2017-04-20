
package pruebas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Pruebas {

    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        
        //Pruebas IO
        
        //archivos Aa = new archivos();
        //Aa.readCoordinates("C:\\Users\\usuario\\Desktop\\netbeaaaans\\coordinates.txt");
        //Aa.readCoordinates("C:\\Users\\usuario\\Desktop\\netbeaaaans\\coordinates.txt");
        
        //Pruebas SQLite
        /*
        SQLiteTest test = new SQLiteTest();
        ResultSet rs;
        ResultSet rs2;
        
        FileToDB dibi = new FileToDB();
        ArrayList<Messages> lista2 = new ArrayList<>();
        ArrayList<Coordinates> lista = new ArrayList<>();
        lista2 = dibi.getMessagesFromText("C:\\Users\\usuario\\Desktop\\netbeaaaans\\ping.txt");
        lista = dibi.getCoordinatesFromText("C:\\Users\\usuario\\Desktop\\netbeaaaans\\coordinates.txt");
        ProbInterface inter = new ProbInterface();
        inter.setVisible(true);
        */
        /*
        for(int i=0; i<lista.size();i++){
            test.addCoords(lista.get(i).x,lista.get(i).y,lista.get(i).h,lista.get(i).timestamp);
        }
        lista.clear();
        System.out.println("Coordinates saved on DB");

        for(int i=0; i<lista2.size();i++){
            test.addMessages(lista2.get(i).type, lista2.get(i).timestamp, lista2.get(i).data);
        }
        lista2.clear();
        System.out.println("Messages saved on DB");
        */
        
        /*
        System.out.println("Coordinates:");
        try{
            rs2 = test.displayCoords();
            while(rs2.next()){
                System.out.println(rs2.getDouble("x") + " " + rs2.getDouble("y")+ " " + rs2.getDouble("h")+ " " + rs2.getString("timestamp"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Messages:");
        try{
            rs = test.displayMessages();
            while(rs.next()){
                System.out.println(rs.getString("type") + "  " + rs.getString("timestamp") + "  " + rs.getString("data"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        */
        
        SQLiteTest test = new SQLiteTest();
        test.getConnection();
        
        HebraCoord gey = new HebraCoord();
        gey.start();
        HebraMsg jej = new HebraMsg();
        jej.start();

        //System.out.println(lista.get(0).x + " " + lista.get(0).y + " " + lista.get(0).h);
    }
    
}
