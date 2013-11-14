/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mySTAT;

/**
 *
 * @author ezra
 */
public class testxml {
   public static void main(String[] argv) {
    
    ProjectStore myStore = ProjectStore.getInstance();
    myStore.openProjectFile("/home/ezra/Temp/test.xml");
    myStore.saveProjectFile("/home/ezra/Temp/testOut.xml");
   }
    
}
