/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mySTAT;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Christian
 */
class CustomRenderer extends DefaultTableCellRenderer 
{
private static final long serialVersionUID = 6703872492730589499L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment( JLabel.CENTER );
        if (row == column)
        {
            cellComponent.setBackground(new java.awt.Color(60, 60, 60));
            this.setText("N/A");
        }
        else
        {
            if (row%2 == 0)
                cellComponent.setBackground(Color.white);
            else
                cellComponent.setBackground(new java.awt.Color(242, 242, 242));
        }    
        return cellComponent;
    }
}
