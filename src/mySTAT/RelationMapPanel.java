/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;


import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
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
import java.awt.image.BufferedImage;
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
        
        add(graphComponent);
        graphParent = panGraph.getDefaultParent();
        panGraph.setAllowDanglingEdges(false);
        panGraph.setCellsDisconnectable(false);
        panGraph.setCellsCloneable(false);
        panGraph.setCellsEditable(false);
        panGraph.setCellsSelectable(true);
        //Default layout
        layout = new mxFastOrganicLayout(panGraph);
        panGraph.setMultigraph(true);
        setSize(750, 394);
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
            case STACK: layout = new mxStackLayout(panGraph, true, 10);
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
        //remove all stakeholders currently graphed
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
                        if(secondary.getName() == r.getName()) 
                        {
                            System.out.printf("insertEdge(%s,%s,%s)\n", main.getName(), secondary.getName(), r.getLineStyle());
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
        System.out.println(getSize().toString());
        System.out.println(getVisibleRect().toString());
        System.out.println(panGraph.getView().getGraphBounds().toString());
        System.out.println(panGraph.getGraphBounds().toString());
    }
    
    private void morphLayout()
    {
        panGraph.getModel().beginUpdate();
        try {
            layout.execute(graphParent);
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
        
        layout = new mxStackLayout(panGraph, false, 10, 20, 20, 2);
        morphLayout();
        setMxLayout(currentLayout);
        morphLayout();
        layout = new mxParallelEdgeLayout(panGraph, 20);
        morphLayout();
        graphComponent.setConnectable(false);
        setMxLayout(currentLayout);
    }
    
    public void snapEdgesToFit()
    {
        setMxLayout(PARALLEL);
        morphLayout();
        setMxLayout(currentLayout);
    }
    
    //draws an image of the current map
    public void exportToPNG(String filename)
    {
        try {
            BufferedImage bi = ScreenImage.createImage(graphComponent);
            ScreenImage.writeImage(bi, "panel.png");
        } catch (IOException ex) {
            Logger.getLogger(RelationMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
