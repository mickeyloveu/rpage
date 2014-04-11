/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.JScrollPane;
import rpage_ver1.controller.DefaultController;
import rpage_ver1.controller.PropertyController;
/**
 *
 * @author ducluu84
 */
public class ProcessDrawingPanel extends JPanel {

     //  The controller used by this view
    private DefaultController defaultController;

    //
    private JScrollPane jScrollPane;
    private ProcessDrawingArea pDrawingArea;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public ProcessDrawingPanel(ProcessDrawingArea fCutDrawingArea){
        this.pDrawingArea = fCutDrawingArea;
        initComponents();
    }

   /** This method is called from within the constructor to
    * initialize the form.
    */
    private void initComponents() {
        //set up the drawing area.
        pDrawingArea.setBackground(Color.white);

        //put the drawing area in a scroll pane.
        jScrollPane = new JScrollPane(pDrawingArea);
        jScrollPane.setPreferredSize(new Dimension(600,800));

        setLayout(new BorderLayout());
        add(jScrollPane, BorderLayout.CENTER);
    }
/******************************************************************************/
    // Other methods
/******************************************************************************/

    public void addDefaultController(DefaultController defaultController){
        this.defaultController = defaultController;
    }
}