/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.*;

import rpage_ver1.controller.DefaultController;

import rpage_ver1.model.AbstractShape;
import rpage_ver1.model.ReadOnlyUOW;
import rpage_ver1.model.UOW;

import rpage_ver1.view.UOWDrawingPanel;
import rpage_ver1.view.RPAGEPallettePanel;
import rpage_ver1.view.UOWPropertyPanel;


/**
 *
 * @author ducluu84
 */
public class UOWDiagramPanel extends JPanel{

    // UOW diagram panel contains: UOW property panel and UOW drawing panel
    private UOWDrawingPanel uowDrawingPanel;
    private UOWPropertyPanel uowPropertyPanel;

    private JButton uowDiagramNameLabel;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public UOWDiagramPanel (UOWPropertyPanel uowPropertyPanel, UOWDrawingPanel uowDrawingPanel){

        // add UOW drawing and property panels to this diagram
        this.uowPropertyPanel = uowPropertyPanel;
        this.uowDrawingPanel = uowDrawingPanel;
        initComponents();
    }// end constructor

   /** This method is called from within the constructor to
    * initialize the form.
    */
    private void initComponents() {

        // BorderLayout(hgap: int, vgap: int)
        // creates a BorderLayout manageer with a specified number of horizontal gap
        // and vertical gap
        uowDiagramNameLabel = new JButton("UOW Diagram");
        setLayout(new BorderLayout(5,10));

        // add panels to this diagram
        add(uowDiagramNameLabel, BorderLayout.NORTH);
        add(uowDrawingPanel, BorderLayout.CENTER);
        add(uowPropertyPanel, BorderLayout.SOUTH);
    }

/******************************************************************************/
    // Other methods
/******************************************************************************/
    public void addDefaultController(DefaultController defaultController){
        // default controller will handle events generated in uow property panel
        // and uow drawing panel
        uowPropertyPanel.addDefaultController(defaultController);
        uowDrawingPanel.addDefaultController(defaultController);
    }

   /**
    * Tell the message bar in uow property panel to display new message
    * @param message
    */
    public void notifyDisplayMessage(String message){
        uowPropertyPanel.notifyDisplayMessage(message);
    }// end method

   /**
    * Return uow property panel
    * @param uow property panel
    */
    public UOWPropertyPanel returnUOWPropertyPanel(){
        return uowPropertyPanel;
    }// end method

    /********************************************************************/
    // OSW size
    /**
    * @param
    */
    public void displayOSW(int oswSize){
        uowPropertyPanel.displayOSW(oswSize);
        validate();
    }// end method

    public void notDisplayOSW(){
        uowPropertyPanel.notDisplayOSW();
    }

   /********************************************************************/
   // UOW name, size, Coordinates, font name and font size
   /**
    * @param
    */
    public void displayUOW(String uowName, int uowXCoordinate, int uowYCoordinate,
            int uowSize, String uowFontName, String uowFontSize){
        uowPropertyPanel.displayUOW(uowName, uowXCoordinate, uowYCoordinate,
            uowSize, uowFontName, uowFontSize);
        validate();
    }// end method

    public void notDisplayUOW(){
        uowPropertyPanel.notDisplayUOW();
    }

    /********************************************************************/
    // UOW RELATIONSHIP -
    /**
    * @param
    */
    public void displayUOWRel(String fromUOWName, String toUOWName,
            int rectIndexFrom, int rectIndexTo, String fromShapeType){
        uowPropertyPanel.displayUOWRel(fromUOWName, toUOWName, rectIndexFrom, rectIndexTo, fromShapeType);
        validate();

    }// end displayUOWRel method

    public void notDisplayUOWRel(){
        uowPropertyPanel.notDisplayUOWRel();
    }

    /********************************************************************/
    // uow RELATIONSHIP NAME
   /**
    * @param rpageLabelName
    */
    public void displayUOWRelName(String rpageLabelName, String rpageLabelFontName, String rpageLabelFontSize ){
        uowPropertyPanel.displayUOWRelName(rpageLabelName, rpageLabelFontName, rpageLabelFontSize);
        validate();
    }// end displayUOWLabel method

    public void notDisplayUOWRelName(){
        uowPropertyPanel.notDisplayUOWRelName();
    }

}
