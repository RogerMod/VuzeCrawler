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

/*

Implementación del WebSocket

---------------------------------------------------------------------------------------------------
Descripción:

Es el servidor de la aplicación. Inicia las hebras y los procesos al abrirse y recibe los mensajes que
vienen del cliente. Una inyección de la clase SessionHandler es la que, como su nombre indica, gestiona
las sesiones.

---------------------------------------------------------------------------------------------------
OnOpen:

Al iniciarse se realizan las siguientes tareas:
    1. Se añade la sesión al SessionHandler y a la hebra2 (de la clase HebraMsg)
    2. Si todavía no se han inicializado el resto de procesos:
        2.1. Creamos una instancia de SQLiteDatabase, la clase que gestiona la base de datos
        2.2. Inicializa esta base de datos
        2.3. Inicia la hebra que lee el fichero pings.txt, que contiene la información de los mensajes 
             que Vuze envía.

A la hebra2 también se le da las sesiones para añadirlas y eliminarlas en una lista propia porque 
es la que envía mensajes al cliente del navegador para que añada elementos en la GUI.
----------------------------------------------------------------------------------------------------
OnMessage:

Cuando se recibe un mensaje (que se habrá enviado en formato JSON) este se lee. Si el tipo de mensaje es
'retrieve', entonces se leerá el atributo 'object' del mismo paquete siendo este el objeto que se pide.
'object' es un string que puede valer 'PING', 'FIND_NODE' o 'FIND_VALUE' y lo que se hará al recibir un
mensaje 'retrieve' con uno de estos valores en 'object' será pausar la hebra HebraMsg para que no haya 
una colisión en SQLite (no se pueden leer y escribir valores en una base de datos al mismo tiempo) 
y posteriormente hacer un SQLRequest a través de la clase SessionHandler. Para más información, buscar 
el método retrieveInfo de dicha clase.

------------------------------------------------------------------------------------------------------
OnClose:

Se cierran las sesiones del SessionHandler.

------------------------------------------------------------------------------------------------------
OnError:

Se saca un mensaje de error por la consola indicando lo que ha fallado.

*/

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
        hebra2.newSession(session);
        
        if (init != true) {
            try {
                SQLiteDatabase test = new SQLiteDatabase();
                test.getConnection();
                System.out.println("Conexion con DB");
                /*
                HebraCoord hebra1 = new HebraCoord();
                hebra1.start();
                */
                hebra2 = new HebraMsg(session);
                hebra2.start();
                
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Algo malo pasa: " + e);
            }
            init = true;
        }
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
        hebra2.deleteSession(session);
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
