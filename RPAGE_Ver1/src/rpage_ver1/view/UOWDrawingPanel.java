/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import rpage_ver1.controller.DefaultController;
import rpage_ver1.controller.PropertyController;
import rpage_ver1.controller.ShapeMode;
import rpage_ver1.model.UOW;
import rpage_ver1.view.UOWDrawingArea;
/**
 *
 * @author ducluu84
 */


public class UOWDrawingPanel extends JPanel {

     //  The controller used by this view
    private DefaultController defaultController;

    //
    private JScrollPane jScrollPane;
    private UOWDrawingArea uowDrawingArea;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public UOWDrawingPanel(UOWDrawingArea uowDrawingArea){
        this.uowDrawingArea = uowDrawingArea;
        initComponents();
    }

   /** This method is called from within the constructor to
    * initialize the form.
    */
    private void initComponents() {
        //set up the drawing area.
        uowDrawingArea.setBackground(Color.white);

        //put the drawing area in a scroll pane.
        jScrollPane = new JScrollPane(uowDrawingArea);
        jScrollPane.setPreferredSize(new Dimension(600,800));

        setLayout(new BorderLayout());
        add(jScrollPane, BorderLayout.CENTER);
    }
/******************************************************************************/

    public void addDefaultController(DefaultController defaultController){
        this.defaultController = defaultController;
    }
}
