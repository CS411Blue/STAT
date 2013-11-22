/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Christian
 */
public class AutoComboBox extends JComboBox {
    private boolean layingOut;
    private boolean isAutoPopupWidth = true;
    private int popupWidth;

    public AutoComboBox(){}
    
    public AutoComboBox(Vector items) {
        super(items);
    }

    public AutoComboBox(ArrayList items) {
        this(items.toArray());
    }

    public AutoComboBox(ComboBoxModel comboBoxModel) {
        super(comboBoxModel);
    }

    public AutoComboBox(Object[] items) {
        super(items);
    }

    /**
     * Overriden to handle the popup Size
     */
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        }
        finally {
            layingOut = false;
        }
    }

    /**
     * Overriden to handle the popup Size
     */
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut) {
            if (isAutoPopupWidth) {
                popupWidth = getOptimumPopupWidth();
            }
            if (popupWidth != 0) {
                dim.width = popupWidth;
            }
        }
        return dim;
    }

    // return the greater of the combo width and the optimum popup width.
    private int getOptimumPopupWidth() {
        return Math.max(super.getSize().width, calculateOptimumPopupWidth());
    }

    // Find the maximum item text width
    private int calculateOptimumPopupWidth() {
        int width = 0;
        FontMetrics fontMetrics = getFontMetrics(getFont());
        for (int i = 0; i < getItemCount(); i++) {
            String text = getItemAt(i).toString();
            width = Math.max(width, fontMetrics.stringWidth(text));
        }
        // kludge to allow for BasicComboPopup insets (default 1 left + 1 right) which are subtracted from the
        // returned width (see BasicComboPopup.getPopupLocation()).
        return width + 3;
    }

    /**
     * Set the popup width to be the optimum (as for setOptimumPopupWidth) before displaying it.
     *
     * @param autoPopupWidth true to set the optimum width, false for the default behaviour.
     */
    public void setAutoPopupWidth(boolean autoPopupWidth) {
        isAutoPopupWidth = autoPopupWidth;
        if (!autoPopupWidth) {
            popupWidth = 0;
        }
    }

    /**
     * Indicates whether the popup width is automatically optimised before display.
     *
     * @return true if the popup width is automatically optimised, false if not.
     */
    public boolean isAutoPopupWidth() {
        return isAutoPopupWidth;
    }
}
