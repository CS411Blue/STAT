/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mySTAT;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
/**
 *
 * @author Brian_2
 */
public class StatMxGraph extends mxGraph {
    @Override
      public boolean isCellSelectable(Object cell) {
         if (cell != null) {
            if (cell instanceof mxCell) {
               mxCell myCell = (mxCell) cell;
               if (myCell.isEdge())
                  return false;
            }
         }
         return super.isCellSelectable(cell);
      }
}
