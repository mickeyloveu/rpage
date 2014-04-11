/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import rpage_ver1.controller.DefaultController;

/**
 *
 * @author ducluu84
 */
public class PrintPreview extends JFrame{
    // mediator for this frame
    private DefaultController defaultController;

    // Background thread for generating the print preview
    private Thread previewThread = null;

    private JMenu previewMenu;
    /**************************************************************************/
    // New, Open, Save, Print, Exit in File
    private JMenuItem printMenuItem;
    private JMenuItem printPreviewMenuItem;
    private JMenuItem exitMenuItem;
    /**************************************************************************/

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public PrintPreview(DefaultController defaultController) {
        this.defaultController = defaultController;
        defaultController.regPrintPreview(this);
        initComponents();
    }

     public void initComponents() {
         JMenu previewMenu = new JMenu("Preview");

        // create New, Open, Save, Print, Exit in File menu
        printMenuItem = new JMenuItem("Print");
        printPreviewMenuItem = new JMenuItem("Print Preview");
        exitMenuItem = new JMenuItem("Exit");

        // add New, Open, Save, Print, Exit in File menu
        previewMenu.add(printMenuItem);
        previewMenu.add(printPreviewMenuItem);
        previewMenu.addSeparator();
        previewMenu.add(exitMenuItem);

         // set icons for Print

        // ICON COPYRIGHT
        // the author use icons from www.pixel-mixer.com
        // these icons are free for commercial use
        printMenuItem.setIcon(new ImageIcon("resource/images/print_16.png"));
     }

/******************************************************************************/
     //Other methods
/******************************************************************************/
   /**
    * Show the print preview frame and start generating preview images.
    */
    public void showPreview(UI ui, int panelWidth, int panelHeigth) {
        if (previewThread == null) {
            replaceUI(ui);

            previewThread = new Thread(new Runnable() {
                public void run() {

                    }
                });
                previewThread.start();
            }
        }

    /**
     * Replace the user interface
     */
    private void replaceUI(UI ui){
        this.setSize(ui.getSize());
        this.setLocation(ui.getLocation());
        this.setVisible(true);
        ui.setVisible(false);
    }

   /**
    * Generate print preview images for the cached text component.
    */
    private void generatePreview(int panelWidth, int panelHeight) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // Change default page format settings if necessary.
        PageFormat pageFormat = printerJob.defaultPage();
        //format.setOrientation(PageFormat.LANDSCAPE);


        // the PageFormat parameter describes the printable area of the page.
        // To find the vertical span of the page use the following code fragement
        // double pageHeight = pageFormat.getImagebleHeight();

        // the area of the printable area
        //height of printer page
        double pageHeight = pageFormat.getImageableHeight();

        //width of printer page
        double pageWidth = pageFormat.getImageableWidth();

        double scale = 1;
        if (panelWidth >= pageWidth){
            scale = pageWidth/panelWidth;
        }

        // the number of panels need to display this diagram
        int totalPanel = (int) Math.ceil(scale * panelHeight / pageHeight);
    }
}

