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
class RelTest extends JPanel
{   
    private final mxGraph panGraph = new mxGraph();
    private mxIGraphLayout layout;
    mxGraphComponent graphComponent;
    private Object parent;
    
    RelTest()
    {
        super(new BorderLayout());
        setSize(500, 500);
        init();
    }
    
    final private void init()
    {
        graphComponent = new mxGraphComponent(panGraph);
        add(BorderLayout.CENTER, graphComponent);
        parent = panGraph.getDefaultParent();
        layout = new mxFastOrganicLayout(panGraph);
    }
    
    public void updateShVertexList(ArrayList<Stakeholder> SHList)
    {
        //remove all stakeholders currently graphed
        panGraph.removeCells(panGraph.getChildVertices(panGraph.getDefaultParent()), true);
        
        //add all stakeholders to the graph first, no edges
        for(Iterator<Stakeholder> iter = SHList.iterator(); iter.hasNext();)
        {
            Stakeholder s = iter.next();
            Object v = new Object();
            if(s.getPlacementRank()>Stakeholder.UNDEFINED)
            {
                v = panGraph.insertVertex(parent, null, s.getName(), 100, 100, 
                        s.getDiameter(), s.getDiameter(), s.getStyle());
                s.setGraphNode(v);
            }
            else
            {
                v = panGraph.insertVertex(parent, null, null, 100, 100, s.getDiameter(), s.getDiameter(), s.getStyle());
                s.setGraphNode(v);
            }
        }
        //now add all edges
        for(Iterator<Stakeholder> mainShIter = SHList.iterator(); mainShIter.hasNext();)
        {
            Stakeholder main = mainShIter.next();
            for (Iterator<Relationship> it = main.getInfluences().iterator(); it.hasNext();) 
            {
                Relationship r = it.next();
                for (Iterator<Stakeholder> secondaryShIter = SHList.iterator(); secondaryShIter.hasNext();) 
                {
                    Stakeholder secondary = secondaryShIter.next();
                    if(secondary.getName() == r.getId()) 
                    {
                        System.out.printf("insertEdge(%s,%s,%s)\n", main.getName(), secondary.getName(), r.getLineStyle());
                        panGraph.insertEdge(parent, null, null, 
                                main.getGraphNode(), secondary.getGraphNode(), r.getLineStyle());
                    }
                }
            }
        }
        morphLayout();
    }
    
    private void morphLayout()
    {
        panGraph.getModel().beginUpdate();
        try {
            layout.execute(parent);
        } finally {
            mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

            morph.addListener(mxEvent.DONE, new mxEventSource.mxIEventListener() {
                public void invoke(Object arg0, mxEventObject arg1) {
                    panGraph.getModel().endUpdate();
                }
            });

            morph.startAnimation();
        }
    }
    
    public void graph()
    {
        morphLayout();
    }
}


/*==================================================================*/


@SuppressWarnings("serial")
public class TestFrame extends JFrame
{
    private static JPanel panel;
    private static RelTest testPanel = new RelTest();
    private static ArrayList<Stakeholder> stakeholders = new ArrayList<Stakeholder>();
    private static JButton makeSHButton = new JButton("Make Stakeholder");
    private static JButton pushAllSHButton = new JButton("Push to View");
    private static JButton makeRelationsButton = new JButton("Initialize Relations for All Stakeholders");
    private static JPanel makeEdgePanel = new JPanel();
    
    public static void main(String[] args)
    {
        TestFrame t = new TestFrame();
        //Create RelTest JPanel and JFrame
        
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
    }

    public static void graphButtonActionPerformed(ActionEvent evt)
    {
        testPanel.graph();
    }
    
    public static void makeSHButtonActionPerformed(ActionEvent evt)
    {
        String name = "Stakeholder " + (stakeholders.size());
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
    
    public static void pushAllSHButtonActionPerformed(ActionEvent evt)
    {
        testPanel.updateShVertexList(stakeholders);
    }
    
    public static void makeRelationsButtonActionPerformed(ActionEvent evt)
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
