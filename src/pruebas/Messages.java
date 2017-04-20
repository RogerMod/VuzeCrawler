
package pruebas;


public class Messages {
    public String type;
    public String timestamp;
    public String data;

    public Messages(String type, String timestamp, String data) {
        this.type = type;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }


}
