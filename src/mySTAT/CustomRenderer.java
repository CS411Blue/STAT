/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mySTAT;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

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

        //if(table.getValueAt(row, column).equals("Y")){
        //    cellComponent.setBackground(Color.YELLOW);
        //} else if(table.getValueAt(row, column).equals("N")){
        //    cellComponent.setBackground(Color.GRAY);
        //}
        if (row == column)
            cellComponent.setBackground(new java.awt.Color(70, 70, 70));
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
