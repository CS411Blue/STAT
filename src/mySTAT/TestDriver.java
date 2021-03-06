package mySTAT;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.math.*;
import java.util.Iterator;
import javax.swing.border.LineBorder;

/**
 *
 * @author Brian_2
 */
public class TestDriver extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel panel;
    private ArrayList<Stakeholder> stakeholders;
    private JButton makeSHButton;
    private JButton makeRelationsButton;
    private JPanel layoutPanel;
    private JPanel container;
    
    public ArrayList<JRadioButton> layoutButtons;
    public JButton morphButton;
    public JButton updateShListButton;
    public JButton parallelButton;
    public JButton exportMapToPNGButton;
    public JButton exportManagementPlanButton;
    
    public TestDriver(ArrayList<Stakeholder> stakeholders)
    {
        super("Test Engine - FOR TESTING ONLY");
        this.stakeholders = stakeholders;
        setSize(400, 400);
        setLocation(400,10);
        panel = new JPanel();
        makeSHButton = new JButton("Make Stakeholder");
        makeSHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeSHButtonActionPerformed(e);
            }
        });
        panel.add(makeSHButton);
        
        makeRelationsButton = new JButton("Randomize Relations");
        makeRelationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeRelationsButtonActionPerformed(e);
            }
        });
        panel.add(makeRelationsButton);
        
        layoutPanel = new JPanel();
        ButtonGroup rbGroup = new ButtonGroup();
        layoutButtons = new ArrayList<JRadioButton>();
        layoutButtons.add(new JRadioButton("Fast Organic", true));
        layoutButtons.add(new JRadioButton("Circle"));
        layoutButtons.add(new JRadioButton("Hierarchical"));
        layoutButtons.add(new JRadioButton("Compact Tree"));
        layoutButtons.add(new JRadioButton("Parallel"));
        layoutButtons.add(new JRadioButton("Partition"));
        for(JRadioButton rb : layoutButtons){rbGroup.add(rb);}
        for(JRadioButton rb : layoutButtons){layoutPanel.add(rb);}
        
        morphButton = new JButton("Morph");
//        panel.add(morphButton);
        
        parallelButton  = new JButton("Snap Lines to Vertices");
//        panel.add(parallelButton);
        
        updateShListButton = new JButton("Update Stakeholder List");
//        panel.add(updateShListButton);
        
        exportMapToPNGButton = new JButton("Export Relation Map");
//        panel.add(exportMapToPNGButton);
        
        exportManagementPlanButton = new JButton("Export Management Plan");
//        panel.add(exportManagementPlanButton);
        
        panel.setPreferredSize(new java.awt.Dimension(20,80));
        panel.setBorder(new LineBorder(Color.black));
        layoutPanel.setBorder(new LineBorder(Color.black));
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        
        container.add(panel);
        container.add(layoutPanel);
        add(container);
    }
    
    public void updateModel(ArrayList<Stakeholder> shs)
    {
        stakeholders = shs;
    }
    
    public void makeSHButtonActionPerformed(ActionEvent evt)
    {
        String name = "Stakeholder " + (this.stakeholders.size());
        boolean a,b,c,d,e;
        a=b=c=d=e=false;
        int random = (int) (Math.random()*32);
        System.out.println(random);
        if(random >= 16){a = true;random -= 16;}
        if(random >= 8){b = true;random -= 8;}
        if(random >= 4){c = true;random -= 4;}
        if(random >= 2){d = true;random -= 2;}
        if(random >= 1){e = true;random -= 1;}
//        System.out.printf("Stakeholder(%s, ,%b,%b,%b,%b,%b)%n", name, a,b,c,d,e);
        Stakeholder s = new Stakeholder(name, "", a, b, c, d, e);
        stakeholders.add(s);
//        pushAllSHButton.setText("Push " + stakeholders.size()+" to View");
    }
    
    public void makeRelationsButtonActionPerformed(ActionEvent evt)
    {
        //erase all relationships in all stakeholders
        for(Stakeholder s : stakeholders)
        {
            s.setInfluences(new ArrayList<Relationship>());
        }
//        System.out.println("In make relationships");
        //now randomize
        int k = stakeholders.size();
        for (Iterator<Stakeholder> s = stakeholders.iterator(); s.hasNext();)
        {
            Stakeholder stakeholder = s.next();
            int place = (int)Math.pow(2.0, (double)k);
            int random = (int) (Math.random()*place);
            place /= 2;
//            System.out.println(random);
            for(int i = k-1; i >= 0; i--)
            {
//                System.out.printf("if(%d >= %d)\n", random, place);
                if(random >= place)
                {
                    //do nothing if the stakeholder is itself
                    if(stakeholders.get(i).getName().equals(stakeholder.getName())){}
                    else{
                        stakeholder.addInfluence(stakeholders.get(i).getName(), (int)(Math.random()*4));
                    }
                    random -= place;
                }
                place /= 2;
            }
        }
    }
    
}
