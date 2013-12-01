/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
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

    private static final String[] METADATA_ATTRIBS = {"title", "description", "created", "updated", "encrypted"};
    private static final String[] STAKEHOLDER_ATTRIBS = {"name", "power", "legitimacy", "urgency", "cooperation", "threat", "wants", "notes", "strategy", "method", "responsible"};

    private static final ProjectStore INSTANCE = new ProjectStore();

    private Map<String, String> metaData; //key/value pairs that contain project meta-data
    private Map<String, Stakeholder> stakeholders; //stakeholderId/stakeholder objects

    private ProjectStore() {
        metaData = new HashMap<>();
        stakeholders = new HashMap<>();
    }

    public ArrayList<Stakeholder> openProjectFile(String statFilePath) {
        ArrayList<Stakeholder> stakeholdersList = new ArrayList<>();
        
        try {
            SAXBuilder builder = new SAXBuilder();
            File statFile = new File(statFilePath);

            Document document = (Document) builder.build(statFile);
            Element rootNode = document.getRootElement();

            //read in project meta-data
            try {
                Element meta = rootNode.getChild("metadata");

                if (meta == null) {
                    throw new ProjectStoreException("No metadata tag found");
                }

                String tempAttrib;
                
                //read title tag
                tempAttrib = meta.getChildText("title");

                if (tempAttrib != null && tempAttrib.length() > 50) {
                    throw new ProjectStoreException("metadata>>title is too large");
                }

                metaData.put("title", tempAttrib);

                //read description tag
                tempAttrib = meta.getChildText("description");

                if (tempAttrib != null && tempAttrib.length() > 256) {
                    throw new ProjectStoreException("metadata>>description is too large");
                }

                metaData.put("description", tempAttrib);

                //read createdby tag
                tempAttrib = meta.getChildText("createdby");

                if (tempAttrib == null) {
                    throw new ProjectStoreException("No metadata>>createdby tag found");
                }
                if (tempAttrib.length() > 256) {
                    throw new ProjectStoreException("metadata>>createdby is too large");
                }

                metaData.put("createdby", tempAttrib);
                
                //read datecreated tag
                tempAttrib = meta.getChildText("datecreated");

                if (tempAttrib == null) {
                    throw new ProjectStoreException("No metadata>>datecreated tag found");
                }
                try {
                    Timestamp.valueOf(tempAttrib);
                } catch (IllegalArgumentException ex) {
                    throw new ProjectStoreException("Invalid metadata>>datecreated tag");
                }

                metaData.put("datecreated", tempAttrib);

                //read datesaved tag
                tempAttrib = meta.getChildText("datesaved");

                if (tempAttrib == null) {
                    throw new ProjectStoreException("No metadata>>datesaved tag found");
                }
                try {
                    Timestamp.valueOf(tempAttrib);
                } catch (IllegalArgumentException ex) {
                    throw new ProjectStoreException("Invalid metadata>>datesaved tag");
                }

                metaData.put("datesaved", tempAttrib);
                
               

                //read in project stakeholders
                if(rootNode.getChild("stakeholders") == null)
                    throw new ProjectStoreException("No stakeholders tag found");
                
                List<Element> stakeholdersChildren = rootNode.getChild("stakeholders").getChildren();
                Iterator<Element> stakeItr = stakeholdersChildren.iterator();
                while (stakeItr.hasNext()) {
                    Map<String, String> stakeholderAttributes = new HashMap<>();
                    Element stakeElement = stakeItr.next();

                    //TODO change this to validate data
                    for (String attrib : STAKEHOLDER_ATTRIBS) {
                        stakeholderAttributes.put(attrib, stakeElement.getChildText(attrib));
                    }

                    List<Element> influenceList = stakeElement.getChild("influences").getChildren("influence");
                    Iterator<Element> influenceItr = influenceList.iterator();

                    ArrayList<Relationship> influences = new ArrayList<>();

                    //read in all influences for this stakeholder
                    while (influenceItr.hasNext()) {
                        Element influenceElement = influenceItr.next();

                        String infId = influenceElement.getChildText("id");
                        int strength = Integer.parseInt(influenceElement.getChildText("strength"));
                        influences.add(new Relationship(infId, strength));
                    }

                    stakeholdersList.add(new Stakeholder(stakeholderAttributes, influences));
                }
            } catch (ProjectStoreException ex) {
                JOptionPane.showMessageDialog(null,
                        "Invalid .stat project file. " + ex.getMessage(),
                        "Error opening project",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        return stakeholdersList;
    }

    public void saveProject(String statFilePath, ArrayList<Stakeholder> stakeholders, String title, String description, String createdBy, String dateCreated, boolean isEncrypted, String passPhrase) {
        //TODO add asserts for valid data
        Date date= new Date();
        Element stat = new Element("stat");
        Document statDoc = new Document(stat);

        //load meta-data elements
        Element metaDataElement = new Element("metadata");
        
        if(title != null && !title.isEmpty())
            metaDataElement.addContent(new Element("title").setText(title));
        
        if(description != null && !description.isEmpty())
            metaDataElement.addContent(new Element("description").setText(description));
        
        // take this out when implemented
        if(createdBy == null)
            createdBy = "temp";
        
        metaDataElement.addContent(new Element("createdby").setText(createdBy));
        
        // take this out when implemented
        if(dateCreated == null) 
            dateCreated = new Timestamp(date.getTime()).toString();
        
        metaDataElement.addContent(new Element("datecreated").setText(dateCreated));     
        
        metaDataElement.addContent(new Element("datesaved").setText(new Timestamp(date.getTime()).toString()));
        
        metaDataElement.addContent(new Element("encrypted").setText((Boolean.toString(isEncrypted))));
        
        statDoc.getRootElement().addContent(metaDataElement);
        
        
        //load stakeholders
        Element stakeholdersElement = new Element("stakeholders");
        for(Stakeholder stakeholder : stakeholders) {
            Element stakeholderElement = new Element("stakeholder");
            Map<String, String> tempAttribs = stakeholder.getAttributes();
            
            //TODO add assert
            String attrib = tempAttribs.get("name");
            stakeholderElement.addContent(new Element("name").setText(attrib));
            
            attrib = tempAttribs.get("power");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("power").setText(attrib));
            
            attrib = tempAttribs.get("legitimacy");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("legitimacy").setText(attrib));
            
            attrib = tempAttribs.get("urgency");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("urgency").setText(attrib));
            
            attrib = tempAttribs.get("cooperation");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("cooperation").setText(attrib));
            
            attrib = tempAttribs.get("threat");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("threat").setText(attrib));
            
            attrib = tempAttribs.get("wants");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("wants").setText(attrib));
            
            attrib = tempAttribs.get("strategy");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("strategy").setText(attrib));
            
            attrib = tempAttribs.get("method");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("method").setText(attrib));
            
            attrib = tempAttribs.get("responsible");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("responsible").setText(attrib));
            
            attrib = tempAttribs.get("notes");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("notes").setText(attrib));
            
            //load influences for this stakeholder
            Element influencesElement = new Element("influences");
            ArrayList<Relationship> influences = stakeholder.getInfluences();
            for (Relationship relationship : influences) {
                Element influenceElement = new Element("influence");
                influenceElement.addContent(new Element("id").setText(relationship.getName()));
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
    
    public void saveProjectFile(String statFilePath) {

        Element stat = new Element("stat");
        Document statDoc = new Document(stat);

        //load meta-data elements
        Element metaDataElement = new Element("metadata");
        for (String attrib : METADATA_ATTRIBS) {
            String value = metaData.get(attrib);
            if (value != null && !value.isEmpty()) {
                metaDataElement.addContent(new Element(attrib).setText(value));
            }
        }

        statDoc.getRootElement().addContent(metaDataElement);

        //load stakeholders
        Element stakeholdersElement = new Element("stakeholders");
        for (Stakeholder stakeHolder : stakeholders.values()) {
            Element stakeholderElement = new Element("stakeholder");
            Map<String, String> tempAttribs = stakeHolder.getAttributes();
            for (String attrib : STAKEHOLDER_ATTRIBS) {
                String value = tempAttribs.get(attrib);
                if (value != null && !value.isEmpty()) {
                    stakeholderElement.addContent(new Element(attrib).setText(value));
                }
            }

            //load influences for this stakeholder
            Element influencesElement = new Element("influences");
            ArrayList<Relationship> influences = stakeHolder.getInfluences();
            for (Relationship relationship : influences) {
                Element influenceElement = new Element("influence");
                influenceElement.addContent(new Element("id").setText(relationship.getName()));
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

    public String getStakeholderName(String id) {
        return stakeholders.get(id).getName();
    }

    public String getStakeholderWants(String id) {
        return stakeholders.get(id).getWants();
    }

    public String getStakeholderClassification(String id) {
        return stakeholders.get(id).getClassification();
    }

    public String getStakeholderAttitude(String id) {
        return stakeholders.get(id).getAttitude();
    }

    public String getStakeholderStrategy(String id) {
        return stakeholders.get(id).getStrategy();
    }

    public String getStakeholderEngagement(String id) {
        return stakeholders.get(id).getEngagement();
    }

    public String getStakeholderLastEngaged(String id) {
        return stakeholders.get(id).getLastEngaged();
    }

    public String getStakeholderResponsible(String id) {
        return stakeholders.get(id).getResponsible();
    }

    public String getStakeholderNotes(String id) {
        return stakeholders.get(id).getNotes();
    }

    public Stakeholder getStakeholder(String id) {
        return stakeholders.get(id);
    }

    public Map<String, Stakeholder> getAllStakeholders() {
        return stakeholders;
    }

    //public void putStakeholder(Stakeholder stakeholder) {
    //    stakeholders.put(stakeholder.getId(), stakeholder);
    //}

    public void putAllStakeholders(Map<String, Stakeholder> newStakeholders) {
        stakeholders = newStakeholders;
    }

    public static ProjectStore getInstance() {
        return INSTANCE;
    }

    /**
     * For throwing Exceptions that happen when reading or writing XML files.
     */
    public class ProjectStoreException extends Exception {

        public ProjectStoreException(String message) {
            super(message);
        }

        public ProjectStoreException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }

}
