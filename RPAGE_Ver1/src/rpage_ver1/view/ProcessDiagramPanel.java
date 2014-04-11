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
import rpage_ver1.model.UOW;

import rpage_ver1.view.ProcessDrawingPanel;
import rpage_ver1.view.ProcessPropertyPanel;

/**
 *
 * @author ducluu84
 */
public class ProcessDiagramPanel extends JPanel {

     // Process diagram panel contains: process property panel and process drawing panel
    private ProcessDrawingPanel pDrawingPanel;
    private ProcessPropertyPanel pPropertyPanel;

    //
    private JButton pDiagramNameLabel;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/

    public ProcessDiagramPanel( ProcessPropertyPanel pPropertyPanel,
            ProcessDrawingPanel pDrawingPanel){

        // add process drawing and property panels to this diagram
        this.pDrawingPanel = pDrawingPanel;
        this.pPropertyPanel = pPropertyPanel;
        initComponents();
    }// end constructor

   /** This method is called from within the constructor to
    * initialize the form.
    */
    private void initComponents() {
        // BorderLayout(hgap: int, vgap: int)
        // creates a BorderLayout manageer with a specified number of horizontal gap
        // and vertical gap
        pDiagramNameLabel = new JButton("Process Diagram");
        setLayout(new BorderLayout(5,10));

        // add panels to this diagram
        add(pDiagramNameLabel, BorderLayout.NORTH);
        add(pDrawingPanel, BorderLayout.CENTER);
        add(pPropertyPanel, BorderLayout.SOUTH);
    }

/******************************************************************************/
    // Other methods
/******************************************************************************/
    public void addDefaultController(DefaultController defaultController){
        // default controller will handle events generated in uow property panel
        // and uow drawing panel
        pPropertyPanel.addDefaultController(defaultController);
        pDrawingPanel.addDefaultController(defaultController);
    }

   /**
    * Tell the message bar in process property panel to display new message
    * @param message
    */
    public void notifyDisplayMessage(String message){
        pPropertyPanel.notifyDisplayMessage(message);
    }// end method

    /************************************************************************/
   // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size

   /**
    * @param
    */
    public void displayCProcess(String prefixPName, String suffixPName, int processXCoordinate, int processYCoordinate,
            int processWidth, int processHeight, int processArcWidth, int processArcHeight,
            String processFontName, String processFontSize){

        pPropertyPanel.displayCProcess(prefixPName, suffixPName, processXCoordinate, processYCoordinate,
            processWidth, processHeight, processArcWidth, processArcHeight,
            processFontName, processFontSize);
        validate();
    }// end method

    public void notDisplayCProcess(){
        pPropertyPanel.notDisplayProcess();
    }

    public void displayCMProcess(String prefixPName, String suffixPName, String sOrEs,
            int processXCoordinate, int processYCoordinate,
            int processWidth, int processHeight, int processArcWidth, int processArcHeight,
            String processFontName, String processFontSize){

        pPropertyPanel.displayCMProcess(prefixPName, suffixPName, sOrEs, processXCoordinate, processYCoordinate,
            processWidth, processHeight, processArcWidth, processArcHeight,
            processFontName, processFontSize);
        validate();
    }// end method

    public void notDisplayCMProcess(){
        pPropertyPanel.notDisplayProcess();
    }


    /************************************************************************/
    // PROCESS RELATIONSHIP
    public void displayProcessRel(String fromShapeName, String toShapeName,
            int rectIndexFrom, int rectIndexTo){
        pPropertyPanel.displayProcessRel(fromShapeName, toShapeName, rectIndexFrom, rectIndexTo);
        validate();

    }// end displayUOWRel method

    public void notDisplayProcessRel(){
        pPropertyPanel.notDisplayProcessRel();
    }

    /************************************************************************/
    // PROCESS RELATIONSHIP NAME

   /**
    * @param rpageLabelName
    */
    public void displayProcessRelName(String rpageLabelName, String rpageLabelFontName, String rpageLabelFontSize ){
        pPropertyPanel.displayProcessRelName(rpageLabelName, rpageLabelFontName,rpageLabelFontSize );
        validate();
    }// end displayProcessRelName method


    public void notDisplayProcessRelName(){
        pPropertyPanel.notDisplayProcessRelName();
    }

}
