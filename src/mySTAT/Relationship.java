/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

/**
 *
 * @author Christian
 */
public class Relationship {
    public final static int HIGH = 3;
    public final static int MEDIUM = 2;
    public final static int LOW = 1;   
    private String id; //the name of the stakeholder that is being influenced
    private int magnitude; //values of 1, 2, or 3 only
    //private boolean direction; //true if they are influencing the other, false if they are being influenced
    
public Relationship(String id, int magnitude) {
        this.id = id;
        this.magnitude = magnitude;
    }
    
    public String getId() { return id; }
    public int getMagnitude() { return magnitude; }
}