
package org.crawler.model;


public class Coordinates {

    public double x;
    public double y;
    public double h;
    public String timestamp;
    
    public Coordinates(double x, double y, double h, String timestamp) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getH() {
        return h;
    }
    
    
    
}
