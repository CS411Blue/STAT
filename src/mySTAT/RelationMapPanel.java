/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;


import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import javax.swing.*;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.math.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
/**
 * @author Brian_2
 */

public class RelationMapPanel extends JPanel
{   
    private static final long serialVersionUID = 1L;
    private StatMxGraph panGraph;
    private mxGraphLayout layout;
    private mxGraphComponent graphComponent;
    private Object graphParent;
    private int currentLayout;
    private JPanel toolbar;
    
    public static final int FASTORGANIC = 0;
    public static final int CIRCLE = 1;
    public static final int HIERARCHICAL = 2;
    public static final int COMPACTTREE = 3;
    public static final int PARALLEL = 4;
    public static final int STACK = 5;

    public RelationMapPanel() {
        super();
        init();
    }
    
    public RelationMapPanel(LayoutManager l)  {
        super(l);
        init();
    }

    public RelationMapPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    public RelationMapPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }
    
    
    
    private void init()
    {
        panGraph = new StatMxGraph();
        graphComponent = new mxGraphComponent(panGraph);
        
        add(graphComponent, BorderLayout.CENTER);
        graphParent = panGraph.getDefaultParent();
        
//        toolbar = new JPanel();
//        toolbar.setLayout(new BorderLayout());
        
//        mxGraphOutline outline = new mxGraphOutline(graphComponent);
//        outline.setPreferredSize(new Dimension(200, 200));
//        toolbar.add(outline, BorderLayout.WEST);
        
//        add(toolbar, BorderLayout.SOUTH);
        
        panGraph.setAllowDanglingEdges(false);
        panGraph.setCellsDisconnectable(false);
        panGraph.setCellsCloneable(false);
        panGraph.setCellsEditable(false);
        panGraph.setCellsSelectable(true);
        //Default layout
        layout = new mxFastOrganicLayout(panGraph);
        panGraph.setMultigraph(true);
//        setSize(750, 394);
//        setPreferredSize(new Dimension(400, 400));
    }
    
    public void setMxLayout(int l)
    {
        currentLayout = l;
        switch(currentLayout)
        {
            case FASTORGANIC: layout = new mxFastOrganicLayout(panGraph);
                break;
            case CIRCLE: layout = new mxCircleLayout(panGraph);
                break;
            case HIERARCHICAL: layout = new mxHierarchicalLayout(panGraph, SwingConstants.NORTH);
                break;
            case COMPACTTREE: layout = new mxCompactTreeLayout(panGraph,false);
                break;
            case PARALLEL: layout = new mxParallelEdgeLayout(panGraph, 20);
                break;
            case STACK: layout = new mxPartitionLayout(panGraph, true, 10, 2);
                break;
            default:
            {
                System.out.println("Resulted to default");
                layout = new mxFastOrganicLayout(panGraph);
            }
        }
    }
    
    public void updateShVertexList(ArrayList<Stakeholder> SHList)
    {
        //remove all vertices and edges currently graphed
        panGraph.removeCells(panGraph.getChildVertices(panGraph.getDefaultParent()), true);
        
        //add all stakeholders to the graph first, no edges
        for(Iterator<Stakeholder> iter = SHList.iterator(); iter.hasNext();)
        {
            Stakeholder s = iter.next();
            Object v = new Object();
            if(s.getPlacementRank()>Stakeholder.UNDEFINED)
            {
                panGraph.getModel().beginUpdate();
                try{v = panGraph.insertVertex(graphParent, null, s.getName(), 100, 100, 
                        s.getDiameter(), s.getDiameter(), s.getStyle());}
                finally{panGraph.getModel().endUpdate();}
                s.setGraphNode(v);
            }
            //What to do if a stakeholder is a "non-stakeholder"
            else
            {
//                v = panGraph.insertVertex(graphParent, null, null, 100, 100, s.getDiameter(), s.getDiameter(), s.getStyle());
//                s.setGraphNode(v);
            }
        }
        //now add all edges
        for(Iterator<Stakeholder> mainShIter = SHList.iterator(); mainShIter.hasNext();)
        {
            Stakeholder main = mainShIter.next();
            for (Iterator<Relationship> it = main.getInfluences().iterator(); it.hasNext();) 
            {
                Relationship r = it.next();
                //If the relationship magnitude is zero, don't graph it.
                if(r.getMagnitude() != 0)
                {
                    for (Iterator<Stakeholder> secondaryShIter = SHList.iterator(); secondaryShIter.hasNext();) 
                    {
                        Stakeholder secondary = secondaryShIter.next();
                        if(secondary.getName().equals(r.getName())) 
                        {
//                            System.out.printf("insertEdge(%s,%s,%s)\n", main.getName(), secondary.getName(), r.getLineStyle());
                            panGraph.getModel().beginUpdate();
                            try{panGraph.insertEdge(graphParent, null, null, 
                                    main.getGraphNode(), secondary.getGraphNode(), r.getLineStyle());}
                            finally{panGraph.getModel().endUpdate();}
                        }
                    }
                }
            }
        }
        graph();
    }
    
    private void morphLayout()
    {
        panGraph.getModel().beginUpdate();
        layout.execute(graphParent);
        panGraph.getModel().endUpdate();
//        fitToSize();
    }
    
    public void graph()
    {
        if(currentLayout == FASTORGANIC)
        {
            layout = new mxStackLayout(panGraph, false, 10, 20, 20, 2);
            morphLayout();
            setMxLayout(currentLayout);
            morphLayout();
            layout = new mxParallelEdgeLayout(panGraph, 20);
            morphLayout();
            setMxLayout(currentLayout);
        }
        else if(currentLayout == CIRCLE)
        {
            morphLayout();
            layout = new mxParallelEdgeLayout(panGraph, 20);
            morphLayout();
            setMxLayout(currentLayout);
        }
        else
        {
            morphLayout();
        }
        graphComponent.setConnectable(false);
    }
    
    public void snapEdgesToFit()
    {
        layout = new mxParallelEdgeLayout(panGraph);
        morphLayout();
        setMxLayout(currentLayout);
    }
    
    //draws an image of the current map
    public void exportToPNG()
    {
        JFileChooser saveFileChooser = new JFileChooser();
        int returnVal = saveFileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = saveFileChooser.getSelectedFile();
            String filename = file.getName();
            if(!filename.contains("."))
                filename = file.getPath() + ".png";
            else
                filename = file.getPath();
            try {
                JFrame frame = new JFrame();
                mxGraphComponent graphCpnt = new mxGraphComponent(panGraph);
                frame.setContentPane(graphCpnt);
                frame.pack();
                BufferedImage bi = ScreenImage.createImage(graphCpnt);
                ScreenImage.writeImage(bi, filename);
            } catch (IOException ex) {
                Logger.getLogger(RelationMapPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public mxGraphOutline getGraphOutline()
    {
        mxGraphOutline outline = new mxGraphOutline(graphComponent);
        outline.setPreferredSize(new Dimension(200, 200));
        return outline;
    }
    
    public mxGraphComponent getGraphComponent()
    {
        return graphComponent;
    }
    
    public void fitToSize()
    {
        double newScale = 1;

        Dimension graphSize = graphComponent.getGraphControl().getSize();
        Dimension viewPortSize = graphComponent.getViewport().getSize();

        int gw = (int) graphSize.getWidth();
        int gh = (int) graphSize.getHeight();

        if (gw > 0 && gh > 0) {
            int w = (int) viewPortSize.getWidth();
            int h = (int) viewPortSize.getHeight();

            newScale = Math.min((double) w / gw, (double) h / gh);
        }

        graphComponent.zoom(newScale);
    }
    
    public ActionListener getFastOrganicActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMxLayout(FASTORGANIC);
            }
        };
    }
    
    public ActionListener getCircleActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMxLayout(CIRCLE);
            }
        };
    }
    
    public ActionListener getHierarchicalActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMxLayout(HIERARCHICAL);
            }
        };
    }
    
    public ActionListener getGraphBtnActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                graph();
            }
        };
    }
    
    public ActionListener getLineSnapActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                snapEdgesToFit();
            }
        };
    }
    
    public ActionListener getExportMapActionListener()
    {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportToPNG();
            }
        };
    }
    
//    @Override
//    public void repaint()
//    {
//        super.repaint();
//        System.out.println("RelationMapPanel repaint()");
//    }
//    
//    @Override
//    public void repaint(Rectangle r)
//    {
//        super.repaint(r);
//        System.out.println("RelationMapPanel repaint(rect)");
//    }
//    
//    @Override
//    public void repaint(long tm)
//    {
//        super.repaint(tm);
//        System.out.println("RelationMapPanel repaint(tm)");
//    }
//    
//    @Override
//    public void repaint(int x, int y, int width, int height)
//    {
//        super.repaint(x, y, width, height);
//        System.out.println("RelationMapPanel repaint(x,y,w,h)");
//    }
//    
//    @Override
//    public void repaint(long tm, int x, int y, int width, int height)
//    {
//        super.repaint(tm, x, y, width, height);
//        System.out.printf("RelationMapPanel repaint(%d,%d,%d,%d,%d)\n", 
//                tm, x, y, width, height);
//    }
}
