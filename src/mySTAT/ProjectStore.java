/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author ezra
 */
public class ProjectStore {
    private static final String[] METADATA_ATTRIBS = {"title","description","created","updated"};
    private static final String[] STAKEHOLDER_ATTRIBS = {"id","name","wants","notes","classification","attitude","strategy","engagement","lastengaged", "responsible"};
    
    private static final ProjectStore INSTANCE = new ProjectStore();

    private Map<String, String> metaData; //key/value pairs that contain project meta-data
    private Map<String, Stakeholder> stakeholders; //stakeholderId/stakeholder objects

    private ProjectStore() {
        metaData = new HashMap<>();
        stakeholders = new HashMap<>();
    }

    public void openProjectFile(String statFilePath) {
        try {
            SAXBuilder builder = new SAXBuilder();
            File statFile = new File(statFilePath);

            Document document = (Document) builder.build(statFile);
            Element rootNode = document.getRootElement();

            //read in project meta-data
            Element meta = rootNode.getChild("metadata");
            for(String attrib : METADATA_ATTRIBS) 
                metaData.put(attrib, meta.getChildText(attrib));
            
            //read in project stakeholders
            List<Element> stakeholdersChildren = rootNode.getChild("stakeholders").getChildren();
            Iterator<Element> stakeItr = stakeholdersChildren.iterator();
            while(stakeItr.hasNext()) {
                Map<String, String> stakeholderAttributes = new HashMap<>();
                Element stakeElement = stakeItr.next();
                
                for(String attrib : STAKEHOLDER_ATTRIBS) 
                    stakeholderAttributes.put(attrib, stakeElement.getChildText(attrib));
                                                
                List<Element> influenceList = stakeElement.getChild("influences").getChildren("influence");
                Iterator<Element> influenceItr = influenceList.iterator();
                
                ArrayList<Relationship> influences = new ArrayList<>();
                
                //read in all influences for this stakeholder
                while(influenceItr.hasNext()) {
                    Element influenceElement = influenceItr.next();
                    
                    String infId = influenceElement.getChildText("id");
                    int strength = Integer.parseInt(influenceElement.getChildText("strength"));
                    influences.add(new Relationship(infId, strength));
                }
                                
                stakeholders.put(stakeholderAttributes.get("id"), new Stakeholder(stakeholderAttributes, influences));
            }            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    public void saveProjectFile(String statFilePath) {
        
        Element stat = new Element("stat");
        Document statDoc = new Document(stat);
        
        //load meta-data elements
        Element metaDataElement = new Element("metadata");        
        for(String attrib : METADATA_ATTRIBS) {
            String value = metaData.get(attrib);
            if(value != null && !value.isEmpty())
                metaDataElement.addContent(new Element(attrib).setText(value));
        }        
        
        statDoc.getRootElement().addContent(metaDataElement);
        
        //load stakeholders
        Element stakeholdersElement = new Element("stakeholders");
        for(Stakeholder stakeHolder : stakeholders.values()) {
            Element stakeholderElement = new Element("stakeholder");
            Map<String, String> tempAttribs = stakeHolder.getAttributes();
            for(String attrib : STAKEHOLDER_ATTRIBS) {
                String value = tempAttribs.get(attrib);
                if(value != null && !value.isEmpty())
                    stakeholderElement.addContent(new Element(attrib).setText(value));
            }
            
            //load influences for this stakeholder
            Element influencesElement = new Element("influences");
            ArrayList<Relationship> influences = stakeHolder.getInfluences();
            for(Relationship relationship : influences) {
                Element influenceElement = new Element("influence");
                influenceElement.addContent(new Element("id").setText(relationship.getId()));
                influenceElement.addContent(new Element("strength").setText(String.valueOf(relationship.getMagnitude())));
                influencesElement.addContent(influenceElement);
            }
            
            stakeholderElement.addContent(influencesElement);
            stakeholdersElement.addContent(stakeholderElement);
          
        }
        statDoc.getRootElement().addContent(stakeholdersElement);
        
        XMLOutputter statOutput = new XMLOutputter(Format.getPrettyFormat());
        try {
            statOutput.output(statDoc, new FileWriter(statFilePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    public String getStakeholderName(String id) { return stakeholders.get(id).getName(); }
    
    public String getStakeholderWants(String id) { return stakeholders.get(id).getWants(); }
    
    public String getStakeholderClassification(String id) { return stakeholders.get(id).getClassification(); }
    
    public String getStakeholderAttitude(String id) { return stakeholders.get(id).getAttitude(); }
    
    public String getStakeholderStrategy(String id) { return stakeholders.get(id).getStrategy(); }
    
    public String getStakeholderEngagement(String id) { return stakeholders.get(id).getEngagement(); }
    
    public String getStakeholderLastEngaged(String id) { return stakeholders.get(id).getLastEngaged(); }
    
    public String getStakeholderResponsible(String id) { return stakeholders.get(id).getResponsible(); }
    
    public String getStakeholderNotes(String id) { return stakeholders.get(id).getNotes(); }

    public Stakeholder getStakeholder(String id) { return stakeholders.get(id); }
    
    public Map<String, Stakeholder> getAllStakeholders() { return stakeholders; }
    
    public void putStakeholder(Stakeholder stakeholder) { stakeholders.put(stakeholder.getId(), stakeholder); }
    
    public void putAllStakeholders(Map<String, Stakeholder> newStakeholders) { stakeholders = newStakeholders; }
    
    public static ProjectStore getInstance() { return INSTANCE; }
}
