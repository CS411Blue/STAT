/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;


import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import javax.swing.*;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.*;
import java.util.ArrayList;
import java.math.*;
import java.util.Iterator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 * @author Brian_2
 */

@SuppressWarnings("serial")
public class TestFrame extends JFrame
{
    private JPanel panel;
    private RelationMapPanel testPanel = new RelationMapPanel();
    private ArrayList<Stakeholder> stakeholders = new ArrayList<Stakeholder>();
    private JButton makeSHButton = new JButton("Make Stakeholder");
    private JButton pushAllSHButton = new JButton("Push to View");
    private JButton makeRelationsButton = new JButton("Initialize Relations for All Stakeholders");
    private JPanel makeEdgePanel = new JPanel();
    
    public static void main(String[] args)
    {
        TestFrame t = new TestFrame();
        t.setVisible(true);
    }
    
    public TestFrame()
    {
        super("Control Window");
        setLayout(new BorderLayout());
        setSize(400,300);
        setLocation(500,10);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        makeSHButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeSHButtonActionPerformed(e);
            }
        });
        panel.add(makeSHButton);
        
        JButton graphButton = new JButton("morph");
        graphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphButtonActionPerformed(evt);
                }
            });
        panel.add(graphButton);
        
        pushAllSHButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushAllSHButtonActionPerformed(e);
            }
        });
        panel.add(pushAllSHButton);
        
        makeRelationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeRelationsButtonActionPerformed(e);
            }
        });
        panel.add(makeRelationsButton);
        
        panel.setPreferredSize(new java.awt.Dimension(20,80));
        panel.setBorder(new LineBorder(Color.black));
        JFrame frame = new JFrame("Relationship Frame");
        frame.setSize(500,500);
        frame.setLocation(10, 10);
        frame.getContentPane().add(testPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void graphButtonActionPerformed(ActionEvent evt)
    {
        testPanel.graph();
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
        System.out.printf("Stakeholder(%s, ,%b,%b,%b,%b,%b)%n", name, a,b,c,d,e);
        Stakeholder s = new Stakeholder(name, "", a, b, c, d, e);
        stakeholders.add(s);
        pushAllSHButton.setText("Push " + stakeholders.size()+" to View");
    }
    
    public void pushAllSHButtonActionPerformed(ActionEvent evt)
    {
        testPanel.updateShVertexList(stakeholders);
    }
    
    public void makeRelationsButtonActionPerformed(ActionEvent evt)
    {
        System.out.println("In make relationships");
        int k = stakeholders.size();
        for (Iterator<Stakeholder> s = stakeholders.iterator(); s.hasNext();)
        {
            Stakeholder stakeholder = s.next();
            int place = (int)Math.pow(2.0, (double)k);
            int random = (int) (Math.random()*place);
            place /= 2;
            System.out.println(random);
            for(int i = k-1; i >= 0; i--)
            {
                System.out.printf("if(%d >= %d)\n", random, place);
                if(random >= place)
                {
                    System.out.printf("%s.addI(%s)%n", stakeholder.getName(), stakeholders.get(i).getName());
                    stakeholder.addInfluence(stakeholders.get(i).getName(), (int)(Math.random()*4));
                    random -= place;
                }
                place /= 2;
            }
        }
    }
}
