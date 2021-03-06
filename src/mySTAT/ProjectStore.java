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
    private static final String[] STAKEHOLDER_ATTRIBS = {"name", "power", "legitimacy", "urgency", "cooperation", "threat", "wants", "notes", "strategy", "method", "responsible", "lastengaged"};

    private static final ProjectStore INSTANCE = new ProjectStore();

    private Map<String, String> metaData; //key/value pairs that contain project meta-data
    private Map<String, Stakeholder> stakeholders; //stakeholderId/stakeholder objects
    
    ProjectCrypto crypto;
    private static boolean isEncrypted = false;

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

                //read encrypted tag
                tempAttrib = meta.getChildText("encrypted");
                
                if (tempAttrib != null && !tempAttrib.isEmpty()) {
                    metaData.put("encrypted", tempAttrib);
                    
                    isEncrypted = true;
                }

                //read in project stakeholders
                if(rootNode.getChild("stakeholders") == null)
                    throw new ProjectStoreException("No stakeholders tag found");
                
                List<Element> stakeholdersChildren = rootNode.getChild("stakeholders").getChildren();
                Iterator<Element> stakeItr = stakeholdersChildren.iterator();
                
                if (isEncrypted) {
                    String compStakeholderData = new String();
                    
                    // decrypt stakeholder attributes
                    while (stakeItr.hasNext()) {
                        Element stakeholder = stakeItr.next();
                        
                        // decrypt stakeholder attributes content
                        for(String childElement : STAKEHOLDER_ATTRIBS) {
                            
                            String cipherText = stakeholder.getChildText(childElement);
                            if (cipherText != null && !cipherText.isEmpty()) {
                                String clearText = crypto.decrypt(cipherText);
                                stakeholder.getChild(childElement).setText(clearText);
                                compStakeholderData += cipherText;
                            }
                        }
                        
                        // decrypt stakeholder influences
                        List<Element> stakeholderInfluences = stakeholder.getChild("influences").getChildren("influence");
                        Iterator<Element> influencesItr = stakeholderInfluences.iterator();
                        while (influencesItr.hasNext()) {
                            Element influence = influencesItr.next();
                            String idCipherText = influence.getChild("id").getText();
                            String strengthCipherText = influence.getChild("strength").getText();
                            
                            String idClearText = crypto.decrypt(idCipherText);
                            String strengthClearText = crypto.decrypt(strengthCipherText);
                            
                            influence.getChild("id").setText(idClearText);
                            influence.getChild("strength").setText(strengthClearText);
                            compStakeholderData += idCipherText + strengthCipherText;
                        }
                    }

                    // Verify HMAC
                    if (!crypto.isPassphraseCorrect(metaData.get("encrypted"), compStakeholderData)) {
                        return null;
                    }
                }
                
                // reset iterator
                stakeItr = stakeholdersChildren.iterator();
                
                while (stakeItr.hasNext()) {
                    Map<String, String> stakeholderAttributes = new HashMap<>();
                    Element stakeElement = stakeItr.next();

                    //TODO change this to validate data
                    // "name", "power", "legitimacy", "urgency", "cooperation", "threat", "wants", "notes", "strategy", "method", "responsible", "lastengaged"
                    
                    // read stakeholder name
                    tempAttrib = stakeElement.getChildText("name");
                    if(tempAttrib == null || tempAttrib.isEmpty())
                        throw new ProjectStoreException("Stakeholder name tag is empty or missing");
                    
                    if(tempAttrib.length() > 60)
                        throw new ProjectStoreException("stakeholder>>name tag is too large");
                    
                    stakeholderAttributes.put("name", tempAttrib);
                    
                     //read in stakeholder power
                    tempAttrib = stakeElement.getChildText("power");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(!tempAttrib.equalsIgnoreCase("true") && !tempAttrib.equalsIgnoreCase("false"))
                            throw new ProjectStoreException("stakeholder>>power tag is invalid");
                                       
                        stakeholderAttributes.put("power", tempAttrib);
                    }
                    
                    //read in stakeholder legitimacy
                    tempAttrib = stakeElement.getChildText("legitimacy");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(!tempAttrib.equalsIgnoreCase("true") && !tempAttrib.equalsIgnoreCase("false"))
                            throw new ProjectStoreException("stakeholder>>legitimacy tag is invalid");
                                       
                        stakeholderAttributes.put("legitimacy", tempAttrib);
                    }
                    
                    //read in stakeholder urgency
                    tempAttrib = stakeElement.getChildText("urgency");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(!tempAttrib.equalsIgnoreCase("true") && !tempAttrib.equalsIgnoreCase("false"))
                            throw new ProjectStoreException("stakeholder>>urgency tag is invalid");
                                       
                        stakeholderAttributes.put("urgency", tempAttrib);
                    }
                    
                    //read in stakeholder cooperation
                    tempAttrib = stakeElement.getChildText("cooperation");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(!tempAttrib.equalsIgnoreCase("true") && !tempAttrib.equalsIgnoreCase("false"))
                            throw new ProjectStoreException("stakeholder>>cooperation tag is invalid");
                                       
                        stakeholderAttributes.put("cooperation", tempAttrib);
                    }
                    
                    //read in stakeholder threat
                    tempAttrib = stakeElement.getChildText("threat");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(!tempAttrib.equalsIgnoreCase("true") && !tempAttrib.equalsIgnoreCase("false"))
                            throw new ProjectStoreException("stakeholder>>threat tag is invalid");
                                       
                        stakeholderAttributes.put("threat", tempAttrib);
                    }
                    
                    //read in stakeholder wants
                    tempAttrib = stakeElement.getChildText("wants");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(tempAttrib.length() > 1024)
                            throw new ProjectStoreException("stakeholder>>wants tag is too long");
                                       
                        stakeholderAttributes.put("wants", tempAttrib);
                    }
                    
                    //read in stakeholder notes
                    tempAttrib = stakeElement.getChildText("notes");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(tempAttrib.length() > 1024)
                            throw new ProjectStoreException("stakeholder>>notes tag is too long");
                                       
                        stakeholderAttributes.put("notes", tempAttrib);
                    }
                    
                    //read in stakeholder method
                    tempAttrib = stakeElement.getChildText("method");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(tempAttrib.length() > 1024)
                            throw new ProjectStoreException("stakeholder>>method tag is too long");
                                       
                        stakeholderAttributes.put("method", tempAttrib);
                    }
                    
                    //read in stakeholder responsible
                    tempAttrib = stakeElement.getChildText("responsible");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(tempAttrib.length() > 60)
                            throw new ProjectStoreException("stakeholder>>responsible tag is too long");
                                       
                        stakeholderAttributes.put("responsible", tempAttrib);
                    }
                    
                    //read in stakeholder lastengaged
                    tempAttrib = stakeElement.getChildText("lastengaged");
                    if(tempAttrib != null && !tempAttrib.isEmpty()){
                                
                        if(tempAttrib.length() > 60)
                            throw new ProjectStoreException("stakeholder>>lastengaged tag is too long");
                                       
                        stakeholderAttributes.put("lastengaged", tempAttrib);
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

    public void saveProject(String statFilePath, ArrayList<Stakeholder> stakeholders, String title, String description, String createdBy, String dateCreated, boolean isEncrypted) {
        
        this.isEncrypted = isEncrypted;
        
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
        
       
        if(createdBy == null || createdBy.isEmpty())
            createdBy = System.getProperty("user.name");
        
        metaDataElement.addContent(new Element("createdby").setText(createdBy));
        
        
        if(dateCreated == null || dateCreated.isEmpty()) 
            dateCreated = new Timestamp(date.getTime()).toString();
        
        metaDataElement.addContent(new Element("datecreated").setText(dateCreated));     
        
        metaDataElement.addContent(new Element("datesaved").setText(new Timestamp(date.getTime()).toString()));

        
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
                        
            attrib = tempAttribs.get("method");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("method").setText(attrib));
            
            attrib = tempAttribs.get("lastengaged");
            if(attrib != null && !attrib.isEmpty())
                stakeholderElement.addContent(new Element("lastengaged").setText(attrib));
            
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
        
        // encrypt stakeholder elements
        if (isEncrypted) {
            String compStakeholderData = new String();

            List<Element> stakeholdersChildren = stakeholdersElement.getChildren("stakeholder");
            Iterator<Element> stakeholdersItr = stakeholdersChildren.iterator();
            
            // encrypt each child stakeholder elements in stakeholders element
            while (stakeholdersItr.hasNext()) {
                Element stakeholder = stakeholdersItr.next();
                
                // encrypt stakeholder attributes content
                for(String childElement : STAKEHOLDER_ATTRIBS) {
                    
                    String clearText = stakeholder.getChildText(childElement);
                    if (clearText != null && !clearText.isEmpty()) {
                        String cipherText = crypto.encrypt(clearText);
                        stakeholder.getChild(childElement).setText(cipherText);
                        compStakeholderData += cipherText;
                    }
                }
                
                // encrypt stakeholder influences
                List<Element> stakeholderInfluences = stakeholder.getChild("influences").getChildren("influence");
                Iterator<Element> influencesItr = stakeholderInfluences.iterator();
                while (influencesItr.hasNext()) {
                    Element influence = influencesItr.next();
                    String idClearText = influence.getChild("id").getText();
                    String srengthClearText = influence.getChild("strength").getText();
                    
                    String idCipherText = crypto.encrypt(idClearText);
                    String strengthCipherText = crypto.encrypt(srengthClearText);
                    
                    influence.getChild("id").setText(idCipherText);
                    influence.getChild("strength").setText(strengthCipherText);
                    
                    compStakeholderData += idCipherText + strengthCipherText;
                }
            }
            
            // add MAC to "encrypted" element
            String tag = crypto.getTag(compStakeholderData);
            metaDataElement.addContent(new Element("encrypted").setText(tag));
        }
        
        statDoc.getRootElement().addContent(metaDataElement);
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
    
    public ArrayList<Stakeholder> openEncryptedProjectFile(String statFilePath, String passPhrase) {
        crypto = new ProjectCrypto();
        crypto.setPassphrase(passPhrase);
        isEncrypted = true;

        return openProjectFile(statFilePath);
    }
    
    public void saveEncryptedProject(String statFilePath, ArrayList<Stakeholder> stakeholders, String title, String description, String createdBy, String dateCreated, boolean isEncrypted, String passPhrase) {
        crypto = new ProjectCrypto();
        crypto.setPassphrase(passPhrase);

        saveProject(statFilePath, stakeholders, title, description, createdBy, dateCreated, isEncrypted);
    }
    
    public boolean isEncrypted(String statFilePath) {
        // quick read of metadata to look for MAC; needs improvment
        try {
            SAXBuilder builder = new SAXBuilder();
            File statFile = new File(statFilePath);

            Document document = (Document) builder.build(statFile);
            Element rootNode = document.getRootElement();

            try {
                Element meta = rootNode.getChild("metadata");

                if (meta == null) {
                    throw new ProjectStoreException("No metadata tag found");
                }
                
                String tag = meta.getChildText("encrypted");
                return (tag != null && !tag.isEmpty());
                
            } catch (ProjectStoreException w) {
                JOptionPane.showMessageDialog(null,
                        "Invalid .stat project file. " + w.getMessage(),
                        "Error opening project",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception w) {
            w.printStackTrace();
            System.exit(1);
        }
        return false;
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
