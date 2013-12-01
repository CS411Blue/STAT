/*///////////////////////////////////////////////////////////
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
    
    public final static String HIGH_SIZE = new String("strokeWidth=6");
    public final static String MEDIUM_SIZE = new String("strokeWidth=3;");
    public final static String LOW_SIZE = new String("strokeWidth=1;");
    public final static String UNDEFINED_SIZE = new String("strokeWidth=0;strokeColor=#CCCCCC");
    private String name; //the name of the stakeholder that is being influenced
    private int magnitude; //values of 1, 2, or 3 only
    //private boolean direction; //true if they are influencing the other, false if they are being influenced
    
public Relationship(){}

public Relationship(Relationship r) {
    this.name = r.name;
    this.magnitude = r.magnitude;
}
    
public Relationship(String name, int magnitude) {
        this.name = name;
        this.magnitude = magnitude;
        //System.out.printf("\t%s %d\n", name, magnitude);
    }
    
    public String getName() { return name; }
    public int getMagnitude() { return magnitude; }
    public String getLineStyle()
    {
        switch(magnitude)
        {
            case HIGH:
                return HIGH_SIZE;
            case MEDIUM:
                return MEDIUM_SIZE;
            case LOW:
                return LOW_SIZE;
            default:
                return "";
        }
    }
    
    public String toString()
    {
        String str;
        str = new String("Relationship{"+name+","+magnitude+"}");
        return str;
    }
}