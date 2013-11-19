/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

/**
 * Stakeholder Class
 * @author Christian
 */
public class Stakeholder {
    public static final int UNDEFINED = 1;
    public static final int LATENT = 2;
    public static final int EXPECTANT = 3;
    public static final int DEFINITIVE = 4;
    
    public static final int MARGINAL = 1;
    public static final int NONSUPPORTIVE = 2;
    public static final int MIXED = 3;
    public static final int SUPPORTIVE = 4;
    
    private static final String SH_SHAPE = new 
            String("shape=ellipse;perimeter=ellipsePerimeter");
    
    private static final String FORMAT = new String("whiteSpace=wrap;");
    
    private static final int DEFINITIVE_SIZE = 150;
    private static final int EXPECTANT_SIZE = 100;
    private static final int LATENT_SIZE = 50;
    private static final int UNDEFINED_SIZE = 5;
    
    private static final String WHITE = new String("fillColor=white;");
    private static final String BLACK = new String("fillColor=black");
    private static final String GREEN = new String("fillColor=green;");
    private static final String YELLOW = new String("fillColor=yellow;");
    private static final String RED = new String("fillColor=red;");
    private static final String BLUE = new String("fillColor=blue;");
    
    private String Id;
    private String Name;
    private String Wants;
    private boolean Power;
    private boolean Legitimacy;
    private boolean Urgency;
    private String Classification;
    private boolean Cooperation;
    private boolean Threat;
    private String Attitude;
    private String Influence; //the value from the NCSOSE formula. used for the management plan
    private String Strategy;
    private String Engagement;
    private String LastEngaged;
    private String Responsible;
    private String Notes;
    private ArrayList<Relationship> Influences;
    private Object graphNode;
    
    public Stakeholder(){};
    
    public Stakeholder(String name, String wants, boolean power, boolean legitimacy, boolean urgency,
            boolean cooperation, boolean threat){
        this.Name = name;
        this.Wants = wants;
        this.Power = power;
        this.Legitimacy = legitimacy;
        this.Urgency = urgency;
        this.setClassification(power, legitimacy, urgency);
        this.Cooperation = cooperation;
        this.Threat = threat;
        this.setAttitude(cooperation, threat);
        this.Influence = "Pending";
        this.Strategy = "Pending";
        this.Engagement = "Pending";
        this.LastEngaged = "Pending";
        this.Responsible = "Pending";
        this.Notes = "Pending";
        this.Influences = new ArrayList<>();
    }
    //I don't think we'll need this part...
    /*public Stakeholder(String name, String wants, boolean power, boolean legitimacy, boolean urgency,
            boolean cooperation, boolean threat, String influence, String strategy, 
            String engagement, String lastengaged, String responsible, String notes){
        Name = name;
        Wants = wants;
        setClassification(power, legitimacy, urgency);
        setAttitude(cooperation, threat);
        Influence = influence;
        Strategy = strategy;
        Engagement = engagement;
        LastEngaged = lastengaged;
        Responsible = responsible;
        Notes = notes;
    }*/
    
    //for loading from XML file
    public Stakeholder(Map<String, String> attributes, ArrayList<Relationship> influences) {
        Id = attributes.get("id");
        Name = attributes.get("name");
        Wants = attributes.get("wants");
        Classification = attributes.get("classification");
        Attitude = attributes.get("attitude");
        Influence = attributes.get("influence");
        Strategy = attributes.get("strategy");
        Engagement = attributes.get("engagement");
        LastEngaged = attributes.get("lastEngaged");
        Responsible = attributes.get("responsible");
        Notes = attributes.get("notes");
        Influences = influences;
    }
    //Classification Identification
    @SuppressWarnings("UnusedAssignment")
    public void setClassification(boolean power, boolean legitimacy, boolean urgency){
        String thisClassification;
        if ((power==true)&&(legitimacy==true)&&(urgency==true))
            thisClassification = "Definitive";
        else if ((power==false)&&(legitimacy==true)&&(urgency==true))
            thisClassification = "Dependent";
        else if ((power==true)&&(legitimacy==false)&&(urgency==true))
            thisClassification = "Dangerous";
        else if ((power==true)&&(legitimacy==true)&&(urgency==false))
            thisClassification = "Dominant";
        else if ((power==false)&&(legitimacy==false)&&(urgency==true))
            thisClassification = "Demanding";
        else if ((power==true)&&(legitimacy==false)&&(urgency==false))
            thisClassification = "Dormant";
        else if ((power==false)&&(legitimacy==true)&&(urgency==false))
            thisClassification = "Discretionary";
        else
            thisClassification = "Non-Stakeholder";
        Classification = thisClassification;
    }
    //Attitude Identification
     @SuppressWarnings("UnusedAssignment")
    public void setAttitude(boolean cooperation, boolean threat){
        String thisAttitude;
        if ((cooperation==true)&&(threat==true))
            thisAttitude = "Mixed";
        else if ((cooperation==true)&&(threat==false))
            thisAttitude = "Supportive";
        else if ((cooperation==false)&&(threat==true))
            thisAttitude = "Non-Supportive";
        else
            thisAttitude = "Marginal";
        Attitude = thisAttitude;
     }
     
     public int getPlacementRank()
    {
        switch(getPlacement())
        {
            case "Latent":
                    return LATENT;
            case "Expectant":
                    return EXPECTANT;
            case "Definitive":
                    return DEFINITIVE;
            case "Undefined":
                    return UNDEFINED;
            default:
                    return 0; //error case
        }
    }
     
     public int getAttitudeRank()
     {
         switch (this.Attitude)
         {
             case "Marginal":
                 return MARGINAL;
             case "Non-Supportive":
                 return NONSUPPORTIVE;
             case "Mixed":
                 return MIXED;
             case "Supportive":
                 return SUPPORTIVE;
             default:
                 return 0; //error case
         }
     }
     
     public String getPlacement()
     {
        switch (Classification) {
            case "Dormant":
            case "Discretionary":
            case "Demanding":
                return "Latent";
            case "Dominant":
            case "Dangerous":
            case "Dependent":
                return "Expectant";
            case "Definitive":
                return "Definitive";
            default:
                return "Undefined";
        }
     }
    
    //Only to be used with toStyle
    private String getColor()
    {
        switch(getAttitudeRank())
        {
            case Stakeholder.MARGINAL:
                return WHITE;
            case Stakeholder.NONSUPPORTIVE:
                return RED;
            case Stakeholder.MIXED:
                return YELLOW;
            case Stakeholder.SUPPORTIVE:
                return GREEN;
            default:
                return "";
        }
    }

    /*Returns the style string for drawing the 
     * stakeholder on the relationship map*/
    public String getStyle()
    {
        String style = new String();
        style = SH_SHAPE + FORMAT + getColor();
        return style;
    }
    /*Returns the size that the stakeholder will be drawn on the RelPanel*/
    public int getDiameter()
    {
        switch(getPlacementRank())
        {
            case DEFINITIVE:
                    return DEFINITIVE_SIZE;
            case EXPECTANT:
                    return EXPECTANT_SIZE;
            case LATENT:
                    return LATENT_SIZE;
            case UNDEFINED:
                    return UNDEFINED_SIZE;
            default:
                return -1; //error case
        }
    }
     //getter functions
     public String getId() { return Id; }
     public String getName() { return Name; }
     public String getWants() { return Wants; }
     public boolean getPower() { return Power; }
     public boolean getLegitimacy() { return Legitimacy; }
     public boolean getUrgency() { return Urgency; }
     public String getClassification() { return Classification; }
     public boolean getCooperation() { return Cooperation; }
     public boolean getThreat() { return Threat; }
     public String getAttitude() { return Attitude; }
     public String getInfluence() { return Influence; }
     public String getStrategy() { return Strategy; }
     public String getEngagement() { return Engagement; }
     public String getLastEngaged() { return LastEngaged; }
     public String getResponsible() { return Responsible; }
     public String getNotes() { return Notes; }
     public ArrayList<Relationship> getInfluences() { return Influences; }
     public Relationship getInfluenceNumber(int index) { return Influences.get(index); }
     public Object getGraphNode(){return graphNode;}
     
     //setter functions
     public void setName(String name) { this.Name = name; }
     public void setWants(String wants) { this.Wants = wants; }
     public void setClassification(String classification) { this.Classification = classification; }
     public void setAttitude(String attitude) { this.Attitude = attitude; }
     public void setStrategy(String strategy) { this.Strategy = strategy; }
     public void setEngagement(String engagement) { this.Engagement = engagement; }
     public void setLastEngaged(String lastEngaged) { this.LastEngaged = lastEngaged; }
     public void setResponsible(String responsible) { this.Responsible = responsible; }
     public void setNotes(String notes) { this.Notes = notes; }
     public void setInfluences(ArrayList<Relationship> influences) { this.Influences = influences; }
     public void addInfluence(String id, int m){Influences.add(new Relationship(id, m));}
     public void addInfluence(Relationship r){Influences.add(r);}
     public void setGraphNode(Object n){this.graphNode = n;}
    
     public Object removeGraphNode()
     {
         Object o = this.graphNode;
         this.graphNode = new Object();
         return o;
     }
     //for saving to XML
     public Map<String, String> getAttributes() {
        Map<String, String> results = new HashMap<>();
        results.put("id", Id);
        results.put("name", Name);
        results.put("wants", Wants);
        results.put("classification", Classification);
        results.put("attitude", Attitude);
        results.put("strategy", Strategy);
        results.put("engagement", Engagement);
        results.put("lastEngaged", LastEngaged);
        results.put("responsible", Responsible);
        results.put("notes", Notes);
        return results;
    }
    /*Writes this stakeholder as a line on a CSV,
      simply outputs every private member of the stakeholder*/
    public void exportManagementCSV(PrintWriter out)
    {
        String str = new String();
        str = Name+","+Wants+","+Classification+","+Attitude+","+Influence+
                ","+Strategy+","+Engagement+","+LastEngaged+","+Responsible+
                ","+Notes;
        out.println(str);
    }
     //function to update stakeholder data using the edit button
     public void edit(String name, String wants, boolean power, boolean legitimacy,
             boolean urgency, boolean cooperation, boolean threat){
         this.Name = name;
         this.Wants = wants;
         this.Power = power;
         this.Legitimacy = legitimacy;
         this.Urgency = urgency;
         this.setClassification(power, legitimacy, urgency);
         this.Cooperation = cooperation;
         this.Threat = threat;
         this.setAttitude(cooperation, threat);
     }
}
