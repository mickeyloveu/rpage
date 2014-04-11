/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1;

import rpage_ver1.controller.PropertyController;
import rpage_ver1.controller.DefaultController;

import rpage_ver1.model.AbstractShape;
import rpage_ver1.model.UOW;

import rpage_ver1.view.PropertyPanelInterface;
import rpage_ver1.view.UI;
import rpage_ver1.view.UOWDrawingPanel;
import rpage_ver1.view.RPAGEPallettePanel;
import rpage_ver1.view.UOWDiagramPanel;
import rpage_ver1.view.UOWPropertyPanel;
import rpage_ver1.view.ProcessDrawingPanel;
import rpage_ver1.view.ProcessPropertyPanel;
import rpage_ver1.view.ProcessDiagramPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import rpage_ver1.view.ProcessDrawingArea;
import rpage_ver1.view.UOWDrawingArea;

/**
 *
 * @author ducluu84
 */
public class Main {

    private RPAGEPallettePanel rpagePallettePanel;
    private UOWDrawingPanel uowDrawingPanel;
    private UOWPropertyPanel uowPropertyPanel;
    private UOWDiagramPanel uowDiagramPanel;

    private ProcessDrawingPanel pDrawingPanel;
    private ProcessPropertyPanel pPropertyPanel;
    private ProcessDiagramPanel pDiagramPanel;

    private UI ui;

    private PropertyController propertyController;
    private DefaultController defaultController;

    /** Creates a new instance of Main */
    public Main() {
        System.out.println("Main");
        // create Controller for RPAGE
        propertyController = new PropertyController();
        defaultController = DefaultController.getDefaultController(propertyController);

        // create uow property, 1st cut property panel
        uowPropertyPanel = new UOWPropertyPanel();
        pPropertyPanel = new ProcessPropertyPanel(propertyController);

        // create uow pallette panel
        rpagePallettePanel = new RPAGEPallettePanel(defaultController);

        // create uow drawing area, uow drawing panel
        UOWDrawingArea uowDrawingArea = new UOWDrawingArea(defaultController);
        uowDrawingPanel = new UOWDrawingPanel(uowDrawingArea);

        // create first cut drawing area, first cut drawing panel
        ProcessDrawingArea fCutDrawingArea = new ProcessDrawingArea(defaultController);
        pDrawingPanel = new ProcessDrawingPanel(fCutDrawingArea);

        // create uow diagram , 1st cut diagram panel
        uowDiagramPanel = new UOWDiagramPanel(uowPropertyPanel, uowDrawingPanel);
        pDiagramPanel = new ProcessDiagramPanel(pPropertyPanel, pDrawingPanel);

        // register uow diagram, first cut diagram panel with default controller
        defaultController.regUOWDiagramPanel(uowDiagramPanel);
        defaultController.regPDiagramPanel(pDiagramPanel);

        // register uow property panel, cprocess property panel, cmprocess property panel
        propertyController.regPropertyPanel(uowPropertyPanel);
        propertyController.regPropertyPanel(pPropertyPanel);

        ui = new UI(uowDiagramPanel, pDiagramPanel, rpagePallettePanel,
                defaultController);

        ui.setTitle("RPAGE 1.0");
        ui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        ui.pack();

        ui.setSize(850, 600);
        ui.setVisible(true);
        ui.setLocationRelativeTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // create the frame on the event dispatching thread.
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                Main main = new Main();
            }
        });

    }
}
