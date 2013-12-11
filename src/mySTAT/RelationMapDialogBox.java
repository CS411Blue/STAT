/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mySTAT;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Brian_2
 */
public class RelationMapDialogBox extends javax.swing.JDialog {

    /**
     * Creates new form RelationMiniMapDialog
     */
    
    public RelationMapDialogBox(java.awt.Frame parent, boolean modal, 
            mxGraphOutline outLine) {
        super(parent, modal);
        outline = outLine;
        initComponents();  
    }
    
    public RelationMapDialogBox(java.awt.Frame parent, String title,
            boolean modal, mxGraphOutline outLine) {
        super(parent, title, modal);
        outline = outLine;
        initComponents();
    }
    
    public RelationMapDialogBox(java.awt.Frame parent, String title,
            boolean modal, mxGraphComponent graphComponent) {
        super(parent, title, modal);
        outline = new mxGraphOutline(graphComponent);
        outline.setPreferredSize(new Dimension(100, 100));
        initComponents();
    }

                             
    private void initComponents() {

        layoutGroup = new javax.swing.ButtonGroup();
        miniMapPanel = new javax.swing.JPanel();
        layoutPanel = new javax.swing.JPanel();
        layoutLabel = new javax.swing.JLabel();
        fastOrganicBtn = new javax.swing.JRadioButton();
        circleBtn = new javax.swing.JRadioButton();
        hierarchicalBtn = new javax.swing.JRadioButton();
        buttonPanel = new javax.swing.JPanel();
        buttonLabel = new javax.swing.JLabel();
        graphBtn = new javax.swing.JButton();
        lineBtn = new javax.swing.JButton();
        exportBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        miniMapPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        javax.swing.GroupLayout miniMapPanelLayout = new javax.swing.GroupLayout(miniMapPanel);
        miniMapPanel.setLayout(miniMapPanelLayout);
        miniMapPanelLayout.setHorizontalGroup(
            miniMapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miniMapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outline, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );
        miniMapPanelLayout.setVerticalGroup(
            miniMapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miniMapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outline, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );

        layoutLabel.setText("Map Layout: ");

        layoutGroup.add(fastOrganicBtn);
        fastOrganicBtn.setSelected(true);
        fastOrganicBtn.setText("Organic");

        layoutGroup.add(circleBtn);
        circleBtn.setText("Circle");

        layoutGroup.add(hierarchicalBtn);
        hierarchicalBtn.setText("Tree");

        layoutPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        javax.swing.GroupLayout layoutPanelLayout = new javax.swing.GroupLayout(layoutPanel);
        layoutPanel.setLayout(layoutPanelLayout);
        layoutPanelLayout.setHorizontalGroup(
            layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(layoutLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layoutPanelLayout.createSequentialGroup()
                        .addGroup(layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hierarchicalBtn)
                            .addComponent(circleBtn)
                            .addComponent(fastOrganicBtn))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layoutPanelLayout.setVerticalGroup(
            layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layoutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fastOrganicBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(circleBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hierarchicalBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonLabel.setText("Graph Operations: ");

        graphBtn.setText("Re-render Layout");

        lineBtn.setText("Re-render Lines");
        
        exportBtn.setText("Export to PNG");

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(graphBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lineBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exportBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(graphBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lineBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exportBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(miniMapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(layoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(miniMapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>                        

    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setLocation(10, 10);
        RelationMapPanel rPanel = new RelationMapPanel();
        ArrayList<Stakeholder> stakes = new ArrayList<>();
        Stakeholder A = new Stakeholder("A", null, true, true, false, true, true);
        Stakeholder B = new Stakeholder("B", null, true, true, true, true, true);
        A.addRelationship("B", 2);
        stakes.add(A);
        stakes.add(B);
        rPanel.updateShVertexList(stakes);
        frame.add(rPanel);
        frame.setVisible(true);
        RelationMapDialogBox miniMap = new RelationMapDialogBox(frame, "Mini Map", false, rPanel.getGraphOutline());
        miniMap.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void addOrganicListener(ActionListener listener)
    {fastOrganicBtn.addActionListener(listener);}

    public void addCircleListener(ActionListener listener)
    {circleBtn.addActionListener(listener);}

    public void addTreeListener(ActionListener listener)
    {hierarchicalBtn.addActionListener(listener);}
    
    public void addGraphBtnListener(ActionListener listener)
    {graphBtn.addActionListener(listener);}
    
    public void addLineBtnListener(ActionListener listener)
    {lineBtn.addActionListener(listener);}
    
    public void addExportBtnListener(ActionListener listener)
    {exportBtn.addActionListener(listener);}
    
    private mxGraphOutline outline;

    // Variables declaration - do not modify                     
    private javax.swing.JLabel buttonLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JRadioButton circleBtn;
    private javax.swing.JRadioButton fastOrganicBtn;
    private javax.swing.JButton graphBtn;
    private javax.swing.JRadioButton hierarchicalBtn;
    private javax.swing.ButtonGroup layoutGroup;
    private javax.swing.JLabel layoutLabel;
    private javax.swing.JPanel layoutPanel;
    private javax.swing.JButton lineBtn;
    private javax.swing.JButton exportBtn;
    private javax.swing.JPanel miniMapPanel;
    // End of variables declaration                   
}
