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
import java.awt.event.*;
import java.util.ArrayList;
import java.math.*;
/**
 *
 * @author Brian_2
 */
@SuppressWarnings("serial")
class RelTest extends JPanel implements ActionListener
{
    
    private final mxGraph panGraph = new mxGraph();
    private mxIGraphLayout layout;
    mxGraphComponent graphComponent;
    private Object parent;
    private int numNewNodes;
    private ArrayList<Object> vertView;
//    private ArrayList<Vertex> vertModel;
    
    RelTest()
    {
        super(new BorderLayout());
        setSize(500, 500);
        init();
    }
    
    final private void init()
    {
        vertView = new ArrayList<Object>();
//        vertModel = new ArrayList<Vertex>();
        numNewNodes = 0;
        graphComponent = new mxGraphComponent(panGraph);
        add(BorderLayout.CENTER, graphComponent);
        parent = panGraph.getDefaultParent();
        layout = new mxFastOrganicLayout(panGraph);
    }
    
//    public void addVertex(Vertex v)
//    {
//        vertModel.add(v);
//    }
    
    public int getNumSH()
    {
        return vertView.size();
    }
    
    public void addSHvertex(Stakeholder s)
    {
        Object v;
        if(s.getPlacementRank()>Stakeholder.UNDEFINED)
        {
            v = panGraph.insertVertex(parent, null, s.getName(), 100, 100, 
                    s.getDiameter(), s.getDiameter(), s.getStyle());
        }
        else
        {
            v = panGraph.insertVertex(parent, null, null, 100, 100, s.getDiameter(), s.getDiameter(), s.getStyle());
        }
        vertView.add(v);
    }
    
    private void morphLayout()
    {
        panGraph.getModel().beginUpdate();
        try {
            layout.execute(parent);
        } finally {
            mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

            morph.addListener(mxEvent.DONE, new mxEventSource.mxIEventListener() {

                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    panGraph.getModel().endUpdate();
                    // fitViewport();
                }

            });

            morph.startAnimation();
        }
    }
    
    public void graph()
    {
//        panGraph.getModel().beginUpdate();
//        try
//        {
//            for(int i = 0; i < vertModel.size(); i++)
//            {
//                Vertex v = vertModel.get(i);
//                Object o1 = panGraph.insertVertex(parent, null, v.name, 100, 100, v.getDiameter(), v.getDiameter(), v.getStyle());
//                vertView.add(o1);
//            }
//        }
//        finally
//        {
//            panGraph.getModel().endUpdate();
//        }
//        
//        // define layout
//        layout = new mxFastOrganicLayout(panGraph);
//        // layout using morphing
        morphLayout();
    }
    
    public void actionPerformed(ActionEvent e)
    {
//        String name = "new node " + (vertView.size() + 1);
//        
//        Object o1 = panGraph.insertVertex(parent, null, v1.name,100, 
//                100, v1.getDiameter(), v1.getDiameter(), v1.getStyle());
//        panGraph.insertEdge(parent,null, null, o1, vertView.get(0));
//        vertView.add(o1);
//        addVertex(v1);
        morphLayout();
    }

    
//    private class Vertex extends Object
//    {
//        public String name;
//        public int sizeRank;
//        public int colorRank;
//        public Vertex(Stakeholder s)
//        {
//            super();
//            name = s.getName();
//            sizeRank = s.getPlacementRank();
//            colorRank = s.getAttitudeRank();
//        }
//        public Vertex(String n, int size, int color)
//        {
//            super();
//            name = n;
//            sizeRank = size;
//            colorRank = color;
//        }
//        private String getColor()
//        {
//            switch(colorRank)
//            {
//                case Stakeholder.MARGINAL:
//                    return WHITE;
//                case Stakeholder.NONSUPPORTIVE:
//                    return RED;
//                case Stakeholder.MIXED:
//                    return YELLOW;
//                case Stakeholder.SUPPORTIVE:
//                    return GREEN;
//                default:
//                    return "";
//            }
//        }
//        public String getStyle()
//        {
//            String style = new String();
//            style = SH_SHAPE + FORMAT + getColor();
//            return style;
//        }
//        public int getDiameter()
//        {
//            switch(sizeRank)
//            {
//                case Stakeholder.DEFINITIVE:
//                        return DEFINITIVE_SIZE;
//                case Stakeholder.EXPECTANT:
//                        return EXPECTANT_SIZE;
//                case Stakeholder.LATENT:
//                        return LATENT_SIZE;
//                case Stakeholder.UNDEFINED:
//                        return UNDEFINED_SIZE;
//                default:
//                    return -1; //error case
//            }
//        }
//    }
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
    
    public static void main(String[] args)
    {
        TestFrame t = new TestFrame();
        //Create RelTest JPanel and JFrame
        
        makeSHButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                makeSHButtonActionPerformed(e);
            }
        });
        panel.add(makeSHButton);
        
        JButton graphButton = new JButton("graph");
        graphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphButtonActionPerformed(evt);
                }
            });
        panel.add(graphButton);
        
        pushAllSHButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushAllSHButtonActionPerformed(e);
            }
        });
        panel.add(pushAllSHButton);
        
        makeRelationsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                makeRelationsButtonActionPerformed(e);
            }
        });
        panel.add(makeRelationsButton);
//        Stakeholder s1 = new Stakeholder("Jimmy", "muffins", true, false, true, false, true);
//        Stakeholder s2 = new Stakeholder("James", "", true, true, true, true, false);
//        Stakeholder s3 = new Stakeholder("PHIL", "nothing", false, false, true, false, false);
//        Stakeholder s4 = new Stakeholder("PHIL BIG", "nothing", false, false, true, true, true);
//        testPanel.addSHvertex(s1);
//        testPanel.addSHvertex(s3);
//        testPanel.addSHvertex(s2);
//        testPanel.addSHvertex(s4);
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
        setSize(300,300);
        setLocation(500,10);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        panel = new JPanel();
        getContentPane().add(panel);
    }
    public void addMyActionButton(ActionListener listener)
    {
        JButton button = new JButton("morph");
        button.addActionListener(listener);
        panel.add(button);
        repaint();
    }
    public static void graphButtonActionPerformed(ActionEvent evt)
    {
        testPanel.graph();
    }
    
    public static void makeSHButtonActionPerformed(ActionEvent evt)
    {
        String name = "Stakeholder " + (testPanel.getNumSH()+stakeholders.size());
        boolean a,b,c,d,e;
        a=false;
        b=false;
        c=false;
        d=false;
        e=false;
        int random = (int) (Math.random()*31);
        System.out.println(random);
        if(random >= 16){a = true;random -= 16;}
        if(random >= 8){b = true;random -= 8;}
        if(random >= 4){c = true;random -= 4;}
        if(random >= 2){d = true;random -= 2;}
        if(random >= 1){e = true;random -= 1;}
        System.out.printf("Stakeholder(%s, ,%b,%b,%b,%b,%b)%n", name, a,b,c,d,e);
        Stakeholder s = new Stakeholder(name, "", a, b, c, d, e);
        stakeholders.add(s);
        pushAllSHButton.setText("" + stakeholders.size());
    }
    
    public static void pushAllSHButtonActionPerformed(ActionEvent evt)
    {
        int s = stakeholders.size();
        for(int i = 0; i < s; i++)
        {
            testPanel.addSHvertex(stakeholders.remove(0));
        }
        pushAllSHButton.setText("" + stakeholders.size());
    }
    
    public static void makeRelationsButtonActionPerformed(ActionEvent evt)
    {
        int k = stakeholders.size();
        for(int s = 0; s < k; s++)
        {
            int place = (int)Math.pow(2.0, (double)k);
            int random = (int) (Math.random()*place*2) - 1;
            for(int i = k; i > 0; i--)
            {
                if(random >= place)
                {
                    stakeholders.get(s).addInfluence(stakeholders.get(i).getName(), (int)Math.random()*4);
                    random -= place;
                }
                place /= 2;
            }
        }
    }
}