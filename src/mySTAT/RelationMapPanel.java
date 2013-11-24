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
import java.util.ArrayList;
import java.math.*;
import java.util.Iterator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
/**
 * @author Brian_2
 */

public class RelationMapPanel extends JPanel
{   
    private static final long serialVersionUID = 1L;
    private mxGraph panGraph;
    private mxIGraphLayout layout;
    private mxGraphComponent graphComponent;
    private Object parent;
    private JScrollPane pane;
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
        panGraph = new mxGraph();
        graphComponent = new mxGraphComponent(panGraph);
        
        add(graphComponent);
        parent = panGraph.getDefaultParent();
        panGraph.setAllowDanglingEdges(false);
        panGraph.setEnabled(true);
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
            //What to do if a stakeholder is a "non-stakeholder"
            else
            {
//                v = panGraph.insertVertex(parent, null, null, 100, 100, s.getDiameter(), s.getDiameter(), s.getStyle());
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
                if(r.getMagnitude() != 0)
                {
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
