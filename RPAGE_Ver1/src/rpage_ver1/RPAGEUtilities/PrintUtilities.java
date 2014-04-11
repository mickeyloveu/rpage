/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.RPAGEUtilities;

/**
 *
 * @author ducluu84
 */
import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

// Most codes of this class are based on this website
// http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing.html
// These codes are said to be "freely available for unrestricted use"
public class PrintUtilities implements Printable{
    // component to be printed
    private Component component;

    public static void printComponent(Component component){
        new PrintUtilities(component).print();
    }

    public PrintUtilities(Component component){
        this.component = component;
    }

    public void print(){
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog())
            try {
                printJob.print();
            }
            catch (PrinterException pe) {
            }
    }

    // a Printable's print() method is called every time the printing API wants
    // to print a new page until NO_SUCH_PAGE is returned.
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        int response = NO_SUCH_PAGE;

        Graphics2D g2D = (Graphics2D) g;

        //get size of diagram
        Dimension dimension = component.getSize();

        //width in pixels
        double panelWidth = dimension.width;

        //height in pixels
        double panelHeight = dimension.height;

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

        int totalPages = (int) Math.ceil(scale * panelHeight / pageHeight);

        if (pageIndex >= totalPages) {
            enableDoubleBuffering(component);
            response = NO_SUCH_PAGE;
        }

        else{
            //  for faster printing, turn off double buffering
            disableDoubleBuffering(component);

            // Translate the origin to 0,0 for the top left corner
            // so that (0, 0) becomes the top left corner of the printable area.
            g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            //  beginning of the next page
            g2D.translate(0f, -pageIndex * pageHeight);

            //  scale the page so the width fits.
            g2D.scale(scale, scale);

            //repaint the page for printing
            component.paint(g2D);

            response = Printable.PAGE_EXISTS;
        }
        return response;
    }

    public static void disableDoubleBuffering(Component component){
        RepaintManager currentManager = RepaintManager.currentManager(component);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component component) {
        RepaintManager currentManager = RepaintManager.currentManager(component);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
