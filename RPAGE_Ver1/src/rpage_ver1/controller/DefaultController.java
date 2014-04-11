/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rpage_ver1.RPAGEUtilities.PrintUtilities;
import rpage_ver1.RPAGEUtilities.StringUtilities;

import rpage_ver1.controller.PropertyController;

import rpage_ver1.model.AbstractShape;
import rpage_ver1.model.Process;
import rpage_ver1.model.OutSideWorld;
import rpage_ver1.model.ProcessType;
import rpage_ver1.model.RPAGELabel;
import rpage_ver1.model.ReadOnlyUOW;
import rpage_ver1.model.UOW;
import rpage_ver1.model.RPAGERelationship;

import rpage_ver1.view.ProcessPropertyPanel;
import rpage_ver1.view.ProcessDiagramPanel;
import rpage_ver1.view.ProcessDrawingArea;
import rpage_ver1.view.PropertyPanelInterface;
import rpage_ver1.view.PrintPreview;
import rpage_ver1.view.UI;
import rpage_ver1.view.UOWDiagramPanel;
import rpage_ver1.view.UOWDrawingArea;
import rpage_ver1.view.UOWPropertyPanel;

/**
 *
 * @author ducluu84
 */
public class DefaultController{
     private ArrayList<UOW> arrayOfUOWs;

     // relationship name in the uow diagrma or process diagram
     private ArrayList<RPAGELabel> arrayOfUOWRelNames;
     private ArrayList<RPAGELabel> arrayOfProcessRelNames;

     private ArrayList<OutSideWorld> arrayOfOSWSymbols;
     private ArrayList<Process> arrayOfProcesses;

     // contains which kind of shape the user has clicked that is UOW, process and so on...
     private ShapeMode shapeMode = ShapeMode.NONE;

     // the default controller needs to delegates some tasks to the property controller that is
     // handling events relating to change model properties
     private PropertyController propertyController;

     // default controller has reference to uow diagram, first cut diagram, 2nd cut diagram,
     // uow drawing are, first cut drawing are, and the general user interface
     private UOWDiagramPanel uowDiagramPanel;
     private ProcessDiagramPanel pDiagramPanel;
     private UI ui;
     private UOWDrawingArea uowDrawingArea;
     private ProcessDrawingArea pDrawingArea;
     private PrintPreview printPreview;

     // new uow created by users (mouse click)
     private UOW newUOW;

     // temporary uow created by moving mouse
     private UOW uowCreatedByMouseMove;

     private RPAGERelationship uowRelCreatedByMouseMove;
     private RPAGERelationship processRelCreatedByMouseMove;

     private OutSideWorld oswCreatedByMouseMove;
     private Process processCreatedByMouseMove;
     private RPAGELabel uowRelNameCreatedByMouseMove;
     private RPAGELabel pRelNameCreatedByMouseMove;

     private RPAGERelationship newUOWRel;
     private RPAGERelationship newProcessRel;

     private RPAGELabel newRPAGELabel;
     private OutSideWorld newOSWSymbol;

     // index to start search for UOW shapes
     // Note that if a UOW is nested within another,
	 // we will eventually choose both (after multiple clicks) since each
	 // search starts where the previous one finished.
     private int uowStartIndex = 0;

     // index to start search for case process shapes
     // Note that if a case process is nested within another,
	 // we will eventually choose both (after multiple clicks) since each
	 // search starts where the previous one finished.
     private int processStartIndex = 0;

     private int uowWithRelContainsXYIndex = -1;
     private int processWithRelContainsXYIndex = -1;

     // if uowSelectIndex >= 0, user has selected an existing UOW
     private int uowSelectIndex = -1;
     // if processSelectIndex >= 0, user has selected an existing process
     private int processSelectIndex = -1;

     // if firstUOWRelIndex >= 0, user first selects this UOW (in creating a new relationship)
     private int firstUOWRelIndex = -1;
     // if secondUOWRelIndex >= 0, user selects this UOW (in creating a new relationship)
     private int secondUOWRelIndex = -1;

     // if firstProcessRelIndex >= 0, user first selects this process (in creating a new relationship)
     private int firstProcessRelIndex = -1;
     // if secondProcessRelIndex >= 0, user selects this process (in creating a new relationship)
     private int secondProcessRelIndex = -1;

     private int firstUOWRectIndex = -1;
     private int secondUOWRectIndex = -1;

     private int firstProcessRectIndex = -1;
     private int secondProcessRectIndex = -1;

     private int firstOSWRectIndex = -1;

     private int uowMouseMovedIndex = -1;
     private int processMouseMovedIndex = -1;

     private int oswMouseMovedIndex = -1;

     // if uowRelNameIndex >= 0, user has selected a uow relationship name
     private int uowRelNameIndex = -1;
     // if cpRelNameIndex >= 0, user has selected an process relationship name
     private int processRelNameIndex = -1;

     // if oswSelectIndex  >= 0, user has selected an existing outside world symbol
     private int oswSelectIndex = -1;

     // if oswRelIndex >= 0, user first selects this outside world symbol (in creating a new relationship)
     private int oswRelIndex = -1;

     private int uowX,uowY;
     private int cpX, cpY;
     private int mouseX, mouseY;
     private int mousePressedX, mousePressedY;
     private int mouseDraggedX, mouseDraggedY;
     private int mouseClickedX, mouseClickedY;
     private int mouseMovedX, mouseMovedY;

     private int uowRelX1Coordinate, uowRelY1Coordinate, uowRelX2Coordinate,
             uowRelY2Coordinate;
     private int processRelX1Coordinate, processRelY1Coordinate, processRelX2Coordinate,
             processRelY2Coordinate;

     private int x1Coordinate, y1Coordinate, x2Coordinate, y2Coordinate;

     // this flag is used to turn on or turn off uow grid
     private Boolean turnOnUOWGridFlag;
     private Boolean turnOnProcessGridFlag;


     // when user click the uow relationship symbol in rpage pallette this flag is raised
     private Boolean firstClickedFlag;
     private Boolean secondClickedFlag;
     private Boolean selectedUOWRelFlag;

     private Boolean allowCreatedUOWRelFlag;
     private Boolean allowCreatedProcessRelFlag;

     private Boolean isUOWRelSelected;

     private Boolean isUOWRelControlPointModified;
     private Boolean isProcessRelControlPointModified;

     private Boolean isDrawCloud = false;

     private Boolean isUOWDrawingSelected = true;
     private Boolean isProcessDrawingSelected = false;

     // if selectedUOWRel is not null, user has selected an exisiting uow relationship
     private RPAGERelationship selectedUOWRel = new RPAGERelationship();
     // if selectedPRel is not null, user has selected an existing process relationship
     private RPAGERelationship selectedProcessRel = new RPAGERelationship();

     private JTextField uowRelNameTextField;

     private String filePath;

     private AffineTransform affineTransform = new AffineTransform ();
     private float uowZoomFactor = 1;
     private float processZoomFactor = 1;

     private boolean isLastSaved = true;
     private static DefaultController instance = null;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
     private DefaultController (PropertyController propertyController){
         arrayOfUOWs = new ArrayList<UOW>();
         arrayOfUOWRelNames = new ArrayList<RPAGELabel>();
         arrayOfProcessRelNames = new ArrayList<RPAGELabel>();
         arrayOfOSWSymbols = new ArrayList<OutSideWorld>();
         arrayOfProcesses = new ArrayList<Process>();

         this.propertyController = propertyController;
         initDefault();
     }

    public static DefaultController getDefaultController(PropertyController propertyController){
        if (instance == null){
            // we can call the constructor
            instance = new DefaultController(propertyController);
            return instance;
        }
        return instance;
    }

     private void initDefault(){
         firstClickedFlag = false;
         secondClickedFlag = false;
         turnOnUOWGridFlag = false;
         turnOnProcessGridFlag = false;
         isUOWRelSelected = false;

         isUOWRelControlPointModified =false;
         isProcessRelControlPointModified = false;

         for(UOW uow: arrayOfUOWs){
             uow.setDisplayRect(false);
             uow.setUOWColor(Color.black);
         }

         for(Process process: arrayOfProcesses){
             process.setDisplayRect(false);
             process.setProcessColor(Color.black);
         }

         for (OutSideWorld osw: arrayOfOSWSymbols){
             osw.setDisplayRect(false);
             osw.setColor(Color.black);
         }

         for (RPAGELabel rpageLabel: arrayOfUOWRelNames){
             rpageLabel.setColor(Color.black);
         }

         for (RPAGELabel rpageLabel: arrayOfProcessRelNames){
             rpageLabel.setColor(Color.black);
         }

         for (Process cp: arrayOfProcesses){
             cp.setProcessColor(Color.black);
         }

         uowCreatedByMouseMove = null;

         uowRelCreatedByMouseMove = null;
         processRelCreatedByMouseMove = null;

         oswCreatedByMouseMove = null;
         processCreatedByMouseMove = null;
         uowRelNameCreatedByMouseMove = null;
         pRelNameCreatedByMouseMove = null;

         if (uowDiagramPanel != null){
             uowDiagramPanel.notifyDisplayMessage("To display some message");

             // repaint uow drawing area
             uowDiagramPanel.repaint();
         }

         if (pDiagramPanel != null){
            pDiagramPanel.notifyDisplayMessage("To display some message");

            // repaint fcut drawing area
            pDiagramPanel.repaint();
         }
     }

     private void notSelectAnyShape(){
         uowWithRelContainsXYIndex = -1;
         processWithRelContainsXYIndex = -1;

         uowSelectIndex = -1;
         processSelectIndex = -1;

         firstUOWRelIndex = -1;
         secondUOWRelIndex = -1;

         firstProcessRelIndex = -1;
         secondProcessRelIndex = -1;

         firstUOWRectIndex = -1;
         secondUOWRectIndex = -1;

         firstProcessRectIndex = -1;
         secondProcessRectIndex = -1;

         firstOSWRectIndex = -1;

         uowMouseMovedIndex = -1;
         processMouseMovedIndex = -1;

         oswMouseMovedIndex = -1;

         uowRelNameIndex = -1;
         processRelNameIndex = -1;

         oswSelectIndex = -1;
         oswRelIndex = -1;

     }
/******************************************************************************/
    // Methods relating to register VIEW with DefaultController
/******************************************************************************/
    /**
     * Register uow diagram panel with this controller
     * As uow diagram panel contains two other panels (uow property panel and uow drawing panel)
     * the events in these two panels will be handle by this controller
     * @param
     */
     public void regUOWDiagramPanel(UOWDiagramPanel uowDiagramPanel){
        this.uowDiagramPanel = uowDiagramPanel;
        this.uowDiagramPanel.addDefaultController(this);
     }// end method


     public void regUOWDrawingArea(UOWDrawingArea uowDrawingArea){
        this.uowDrawingArea = uowDrawingArea;
    }

    /**
     * Register process diagram panel with this controller
     * As process diagram panel contains two other panels (property panel and drawing panel)
     * the events in these two panels will be handle by this controller
     * @param g this object contain information about the graphics context
     */
     public void regPDiagramPanel(ProcessDiagramPanel pDiagramPanel){
        this.pDiagramPanel = pDiagramPanel;
        this.pDiagramPanel.addDefaultController(this);
     }// end method

    public void regPDrawingArea(ProcessDrawingArea pDrawingArea){
        this.pDrawingArea = pDrawingArea;
    }

    public void regPrintPreview(PrintPreview printPreview){
        this.printPreview = printPreview;
    }

    public void regUI(UI ui){
        this.ui = ui;
    }

/******************************************************************************/
    // Methods relating to UOW model, UOW relationships, uow relationship names
    // Outside world symbols-
    // UOW DIAGRAMS
/******************************************************************************/
    /**
     * Draw the uow into the UOW drawing area
     * @param g this object contain information about the graphics context
     */
     public void drawUOWs(Graphics g){


        for (UOW uow: arrayOfUOWs) {
            uow.drawUOW(g);
        }
        drawUOWCreatedByMouseMove(g);
        drawUOWRelCreatedByMouseMove(g);
        drawOSWSymbolCreatedByMouseMove(g);

     }// end drawUOWs method

    /**
     * Draw the uow relationships into the UOW drawing area
     * @param g this object contain information about the graphics context
     */
     public void drawUOWRels(Graphics g){
         for (UOW uow: arrayOfUOWs){
             uow.drawUOWRels(g);
         }
     }// end drawUOWRelationships method

    /**
     * Draw the uow relationship names into the UOW drawing area
     * @param g this object contain information about the graphics context
     */
     public void drawUOWRelNames(Graphics g){
         for (RPAGELabel uowRelName: arrayOfUOWRelNames){
             uowRelName.drawRPAGELabel(g);
         }
         drawUOWRelNameCreatedByMouseMove(g);
     }// end drawUOWRelNames method

     /**
     * Draw the out side world symbol into the UOW drawing area
     * @param g this object contain information about the graphics context
     */
     public void drawOSWSymbols(Graphics g){
         for (OutSideWorld outSideWorld: arrayOfOSWSymbols){
             outSideWorld.draw(g);
         }
     }// end drawOSWSymbols method

   /**
    * Draw the uow created by mouse move event into the UOW drawing area
    * @param g this object contain information about the graphics context
    */
    private void drawUOWCreatedByMouseMove(Graphics g){
        if (uowCreatedByMouseMove != null){
            uowCreatedByMouseMove.drawUOW(g);
        }
    }// end method

   /**
    * Draw the uow relationship name created by mouse move event into the UOW drawing area
    * @param g this object contain information about the graphics context
    */
    private void drawUOWRelNameCreatedByMouseMove(Graphics g){
        if (uowRelNameCreatedByMouseMove != null){
            uowRelNameCreatedByMouseMove.drawRPAGELabel(g);
        }
    }// end method

    private void drawOSWSymbolCreatedByMouseMove(Graphics g){
        if (oswCreatedByMouseMove != null){
            oswCreatedByMouseMove.draw(g);
        }

    }

    /**
    * Draw the uow relationship created by mouse move event into the UOW drawing area
    * @param g this object contain information about the graphics context
    */
    private void drawUOWRelCreatedByMouseMove(Graphics g){
        if (uowRelCreatedByMouseMove != null){
            uowRelCreatedByMouseMove.drawUOWRel(g);
        }
    }// end method

   /**
    * Remove an existing uow from the array of UOWs
    * @param uowSelectIndex the uow index needs to be removed from the array of UOWs
    */
    public void removeUOW(int uowSelectIndex) {
        // delete relationship of this uow first first
        arrayOfUOWs.get(uowSelectIndex).removeAllUOWRel();

        // tell other uows to also remove the deleted relationship
        for (UOW uow: arrayOfUOWs){
            // remove all relationship that fromUOWName is DELETED or toUOWName is DELETED
            uow.removeRel();
        }

        // tells other outside world symbol to also delete relating relationships
        for (OutSideWorld outSideWorld: arrayOfOSWSymbols){
            // remove all relationship that fromUOWName is DELETED or toUOWName is DELETED
            outSideWorld.removeRel();
        }

        // after having deleted all relationships belong to this uow, delete this uow
        arrayOfUOWs.remove(uowSelectIndex);
    }

   /**
    * Remove an existing uow from the array of UOWs
    * @param uowName
    */
    public void removeUOW(String uowName) {
        int uowSelectIndex = -1;
        for (int i=0; i< arrayOfUOWs.size(); i++){
            if (arrayOfUOWs.get(i).getUOWName().equalsIgnoreCase(uowName)){
                uowSelectIndex = i;
            }
        }

        if (uowSelectIndex >= 0){
            removeUOW(uowSelectIndex);
        }
    }

   /**
    * Remove an existing Outside world symbol from the array of OSWSymbols
    * @param oswSelectIndex
    */
    public void removeOSW(int oswSelectIndex) {
        // delete all uow relationship belongs to this OSW symbol first
        arrayOfOSWSymbols.get(oswSelectIndex).removeAllUOWRel();
        // tells other outside world symbol to also delete relating relationships
        for (OutSideWorld outSideWorld: arrayOfOSWSymbols){
            // remove all relationship that fromUOWName is DELETED or toUOWName is DELETED
            outSideWorld.removeRel();
        }
        for (UOW uow: arrayOfUOWs){
            // remove all relationship that fromUOWName is DELETED or toUOWName is DELETED
            uow.removeRel();
        }
        arrayOfOSWSymbols.remove(oswSelectIndex);
    }

    /**
     * Return the index of the selected UOW if it (not including its rectangles)
     * contains x, y
     * @param x
     * @param y
     * @param startIndex
     */
    public int getUOWSelectedIndex(int x,int y, int startIndex){
        for (int i = 0; i < arrayOfUOWs.size(); i++) {
            int current = (i + startIndex) % arrayOfUOWs.size();
            if (arrayOfUOWs.get(current).createPolygon().contains(x, y)){
                // change UOW colour
                arrayOfUOWs.get(current).setUOWColor(Color.red);
                return current;
            }
        }
        return -1;
    }// end getUOWSelected method

   /**
    * Return the index of the selected UOW if its rectangles contains x, y
    * This method is used when user wants to select the rectangle of the uow
    * when creating UOW relationship
    * @param x
    * @param y
    * @param startIndex
    */
    public int getUOWRectSelectedIndex(int xCoordinate,int yCoordinate, int startIndex){
        for (int i = 0; i < arrayOfUOWs.size(); i++) {
            int current = (i + startIndex) % arrayOfUOWs.size();
            if (arrayOfUOWs.get(current).isWithinRects(xCoordinate,yCoordinate)){
                return current;
            }
        }
        return -1;
    }// end getUOWSelected method

   /**
    * Return the index of the selected UOW relationship name if it
    * contains xCoordinate, yCoordinate
    * @param x
    * @param y
    */
    public int getUOWRelNameSelectedIndex(int xCoordinate, int yCoordinate){
        for (int i = 0; i < arrayOfUOWRelNames.size(); i++){
            if (arrayOfUOWRelNames.get(i).contains(xCoordinate, yCoordinate)){
                return i;
            }
        }
        return -1;
    }

   /**
    * Return the index of the selected UOW if it (not including its rectangles)
    * contains xCoordinate, yCoordinate
    * @param xCoordinate
    * @param yCoordinate
    */
    public int getOSWSelectedIndex(int xCoordinate, int yCoordinate){
        for (int i = 0; i < arrayOfOSWSymbols.size(); i++){
            if (arrayOfOSWSymbols.get(i).contains(xCoordinate, yCoordinate)){
                return i;
            }
        }
        return -1;
    }

   /**
    * Return the index of the selected OSW if its rectangles contains x, y
    * @param x
    * @param y
    */
    public int getOSWRectSelectedIndex(int xCoordinate, int yCoordinate){
       for (int i = 0; i < arrayOfOSWSymbols.size(); i++){

            if (arrayOfOSWSymbols.get(i).isWithinRects(xCoordinate,yCoordinate)){
                return i;
            }
        }
        return -1;
    }

   /**
    * Return the uow with a particular name
    * @param uowName
    */
    private UOW getUOW(String uowName){
        for (UOW uow: arrayOfUOWs){
            if (uow.getUOWName().equalsIgnoreCase(uowName)){
                return uow;
            }
        }
        return null;
    }// end getUOW method

   /**
    * Return the outside world symbol by using its name
    * @param uowName
    */
    private OutSideWorld getOSW(String oswName){
        for (OutSideWorld osw: arrayOfOSWSymbols){
            if (osw.getName().equalsIgnoreCase(oswName)){
                return osw;
            }
        }
        return null;
    }// end getUOW method

    /**
     * Return the index of the uow that contains the relationship containing (x,y)
     * @param x
     * @param y
     * @param startIndex
     */
    public int getUOWRelSelected(int x,int y){
        for (int i = 0; i < arrayOfUOWs.size(); i++){
            if (arrayOfUOWs.get(i).isRelContains(x,y)){
                return i;
            }
        }
        return -1;
    }// end getUOWSelected method

    /**
     * Modify the color property of a UOW
     * @param uowSelectIndex >= 0 the user clicks an existing UOW
     * @param color the new color value
     */
    public void setUOWColor(int uowSelectIndex, Color color){
        if (arrayOfUOWs.get(uowSelectIndex) != null){
            arrayOfUOWs.get(uowSelectIndex).setUOWColor(color);
        }
    }

    /**
     * Modify the X Coordinate of a UOW
     * @param uowSelectIndex >= 0 the user clicks an existing UOW
     * @param x the new value
     */
    public void setUOWXCoordinate(int uowSelectIndex, int x){
        arrayOfUOWs.get(uowSelectIndex).setUOWXCoordinate(x);
    }// end method

    /**
     * Modify the Y Coordinate of a UOW
     * @param uowSelectIndex >= 0 the user clicks an existing UOW
     * @param y the new value
     */
    public void setUOWYCoordinate(int uowSelectIndex, int y){
        if (uowSelectIndex >=0) arrayOfUOWs.get(uowSelectIndex).setUOWYCoordinate(y);
    }// end method

    public boolean isExistingUOWName(String uowName){
        for (UOW uow: arrayOfUOWs){
            if (uow.getUOWName().equalsIgnoreCase(uowName)){
                return true;
            }
        }
        return false;
    }

    public boolean isExistingOSWName(String oswSymbolName){
        for (OutSideWorld osw: arrayOfOSWSymbols){
            if (osw.getName().equalsIgnoreCase(oswSymbolName)){
                return true;
            }
        }
        return false;
    }

/******************************************************************************/
    // Methods relating to CASE PROCESS model, process relationship names
    // PROCESS DIAGRAMS
/******************************************************************************/
    /**
     * Draw the case processes into the first cut drawing area
     * @param g this object contain information about the graphics context
     */
     public void drawProcesses(Graphics g){
        for (Process process: arrayOfProcesses) {
            process.draw(g);
        }
        drawPCreatedByMouseMove(g);
        drawProcessRelCreatedByMouseMove(g);
     }// end drawCPs method

   /**
    * Draw the case process created by mouse move event into the first cut drawing area
    * @param g this object contain information about the graphics context
    */
    private void drawPCreatedByMouseMove(Graphics g){
        if (processCreatedByMouseMove != null){
            processCreatedByMouseMove.draw(g);
        }
    }// end method

   /**
    * Draw the process relationship created by mouse move event into the prcoess drawing area
    * @param g this object contain information about the graphics context
    */
    private void drawProcessRelCreatedByMouseMove(Graphics g){
        if (processRelCreatedByMouseMove != null){
            processRelCreatedByMouseMove.drawUOWRel(g);
        }
    }// end method

     /**
     * Draw the process relationship names into the first cut diagram
     * @param g this object contain information about the graphics context
     */
     public void drawProcessRelNames(Graphics g){
         for (RPAGELabel processRelName: arrayOfProcessRelNames){
             processRelName.drawRPAGELabel(g);
         }
         drawProcessRelNameCreatedByMouseMove(g);
     }// end drawUOWRelNames method

   /**
    * Draw the process relationship name created by mouse move event into the first cut diagram
    * @param g this object contain information about the graphics context
    */
    private void drawProcessRelNameCreatedByMouseMove(Graphics g){
        if (pRelNameCreatedByMouseMove != null){
            pRelNameCreatedByMouseMove.drawRPAGELabel(g);
        }
    }// end method

    /**
     * Return the index of the selected process if it (not including its rectangles)
     * contains x, y
     * @param x
     * @param y
     * @param startIndex
     */
    public int getProcessSelectedIndex(int xCoordinate,int yCoordinate, int startIndex){
        for (int i = 0; i < arrayOfProcesses.size(); i++) {
            int current = (i + startIndex) % arrayOfProcesses.size();
            if (arrayOfProcesses.get(current).contains(xCoordinate, yCoordinate)){
                // change case process colour
                arrayOfProcesses.get(current).setProcessColor(Color.red);
                return current;
            }
        }
        return -1;
    }// end getUOWSelected method

   /**
    * Return the index of the selected Process if its rectangles contains x, y
    * This method is used when user wants to select the rectangle of the uow
    * when creating UOW relationship
    * @param x
    * @param y
    * @param startIndex
    */
    public int getProcessRectSelectedIndex(int xCoordinate,int yCoordinate, int startIndex){
        for (int i = 0; i < arrayOfProcesses.size(); i++) {
            int current = (i + startIndex) % arrayOfProcesses.size();
            if (arrayOfProcesses.get(current).isWithinRects(xCoordinate,yCoordinate)){
                return current;
            }
        }
        return -1;
    }// end getUOWSelected method

    /**
     * Return the index of the process that contains the relationship containing (x,y)
     * @param x
     * @param y
     */
    public int getProcessRelSelected(int x,int y){
        for (int i = 0; i < arrayOfProcesses.size(); i++){
            if (arrayOfProcesses.get(i).isRelContains(x,y)){
                return i;
            }
        }
        return -1;
    }// end getUOWSelected method

    public int getProcessRelNameSelectedIndex(int xCoordinate, int yCoordinate){
        for (int i = 0; i < arrayOfProcessRelNames.size(); i++){
            if (arrayOfProcessRelNames.get(i).contains(xCoordinate, yCoordinate)){
                return i;
            }
        }
        return -1;
    }

    public boolean isExistingProcessName(String processName){
        for (Process process: arrayOfProcesses){
            if (process.getSuffixPName().equalsIgnoreCase(processName)){
                return true;
            }
        }
        return false;
    }

   /**
    * Remove an existing processs from the array of processes
    * @param uowSelectIndex the uow index needs to be removed from the array of UOWs
    */
    public void removeProcess(int processSelectIndex) {
        // delete all relationships of this process first
        arrayOfProcesses.get(processSelectIndex).removeAllProcessRel();

        // tell other processes to also remove the deleted relationship
        for (Process process: arrayOfProcesses){
            // remove all relationship that fromUOWName is DELETED or toUOWName is DELETED
            process.removeRel();
        }

        // after having deleted all relationships belong to this process, delete this process
        arrayOfProcesses.remove(processSelectIndex);
    }

    /**
    * Remove an existing case process and case management process from the array of CPs
    * @param uowName the uow that this case process deals with
    */
    public void removeProcess(String uowName) {
        int indexOfDeletedCP = -1;
        int indexOfDeletedCMP = -1;

        // remove case process first
        for (int i = 0; i < arrayOfProcesses.size(); i++){
            if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(uowName)
                    && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_PROCESS){
                indexOfDeletedCP = i;
            }
        }

        if (indexOfDeletedCP >=0){
            removeProcess(indexOfDeletedCP);
        }

        // then remove case management process
        for (int i = 0; i < arrayOfProcesses.size(); i++){
            if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(uowName)
                    && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                indexOfDeletedCMP = i;
            }
        }
        if (indexOfDeletedCMP >=0){
            removeProcess(indexOfDeletedCMP);
        }
    }

   /**
    * Return the process with a particular name
    * @param processName: this is process full name. For example, Handle a <UOW> or
    * Manage the flow of <UOWs>
    */
    private Process getProcess(String processName){
        for (Process process: arrayOfProcesses){
            if (process.getProcessFullName().equalsIgnoreCase(processName)){
                return process;
            }
        }
        return null;
    }// end getProcess method

   /**
    * Return the case process with a particular uow name
    * @param uowName: this is the uow that this process deals with.
    */
    private Process getCProcess(String uowName){
        for (Process process: arrayOfProcesses){
            if (process.getSuffixPName().equalsIgnoreCase(uowName) &&
                    process.getProcessType() == ProcessType.CASE_PROCESS){
                return process;
            }
        }
        return null;
    }// end getCProcess method

   /**
    * Return the case management process with a particular uow name
    * @param uowName: this is the uow that relates to this management process.
    */
    private Process getCMProcess(String uowName){
        for (Process process: arrayOfProcesses){
            if (process.getSuffixPName().equalsIgnoreCase(uowName) &&
                    process.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                return process;
            }
        }
        return null;
    }// end getCProcess method
/******************************************************************************/
    // Methods relating to UOW property panel
/******************************************************************************/
    /**
     * Tells UOW property panel to display the property values of the selected UOW
     * @param uowSelectIndex >= 0 the user clicks an existing UOW
     */
    public void displayUOW(int uowSelectIndex){
        if (uowSelectIndex >= 0){
            String uowName = arrayOfUOWs.get(uowSelectIndex).getUOWName();
            int uowXCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWXCoordinate();
            int uowYCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWYCoordinate();
            int uowSize = arrayOfUOWs.get(uowSelectIndex).getUOWSize();
            String uowFontName = arrayOfUOWs.get(uowSelectIndex).getUOWFontName();
            String uowFontSize = arrayOfUOWs.get(uowSelectIndex).getUOWFontSize().toString();
            uowDiagramPanel.displayUOW(uowName, uowXCoordinate, uowYCoordinate,
                    uowSize, uowFontName, uowFontSize);
        }
    }// end displayUOW method

   /**
    * Tells UOW property panel to display the property values of the selected outside world symbol
    * @param oswSelectIndex >= 0 the user clicks an existing OSW symbol
    */
    public void displayOSW(int oswSelectIndex){
        if (oswSelectIndex >= 0){
            int oswSize = arrayOfOSWSymbols.get(oswSelectIndex).getOSWSize();
            uowDiagramPanel.displayOSW(oswSize);
        }
    }// end displayUOW method


   /**
    * Tells UOW property panel to display the property values of the selected UOW relationship name
    * @param selectIndex >= 0 the user clicks an existing UOW
    */
    public void displayUOWRelName(int uowRelNameIndex){
        if (uowRelNameIndex >= 0){
            String rpageLabelName = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelName();
            String rpageLabelFontName = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelFontName();
            String rpageLabelFontSize = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelFontSize().toString();
            uowDiagramPanel.displayUOWRelName(rpageLabelName, rpageLabelFontName, rpageLabelFontSize);
        }
    }// end displayUOWRelName method

   /**
    * Tells process property panel to display the property values of the selected process relationship name
    * @param processSelectIndex >= 0 the user clicks an existing UOW
    */
    public void displayProcessRelName(int processRelNameIndex){
        if (processRelNameIndex >= 0){
            String rpageLabelName = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelName();
            String rpageLabelFontName = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelFontName();
            String rpageLabelFontSize = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelFontSize().toString();
            pDiagramPanel.displayProcessRelName(rpageLabelName, rpageLabelFontName, rpageLabelFontSize);
        }
    }// end displayUOWRelName method

   /**
    * Tells first cut property panel to display the property values of the selected case process
    * @param selectIndex >= 0 the user clicks an existing case proces
    */
    public void displayProcess(int processSelectIndex){

        if (processSelectIndex >= 0){
            String prefixPName = arrayOfProcesses.get(processSelectIndex).getPrefixPName();
            String suffixPName = arrayOfProcesses.get(processSelectIndex).getSuffixPName();

            int processXCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessXCoordinate();
            int processYCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessYCoordinate();
            int processWidth = arrayOfProcesses.get(processSelectIndex).getProcessWidth();
            int processHeight = arrayOfProcesses.get(processSelectIndex).getProcessHeight();
            int processArcWidth = arrayOfProcesses.get(processSelectIndex).getProcessArcWidth();
            int processArcHeight = arrayOfProcesses.get(processSelectIndex).getProcessArcHeight();

            String processFontName = arrayOfProcesses.get(processSelectIndex).getProcessFontName();
            String processFontSize = arrayOfProcesses.get(processSelectIndex).getProcessFontSize().toString();
            String sOrEs = arrayOfProcesses.get(processSelectIndex).getSOrEs();
            // tells process diagram to display the selected case process
            if (arrayOfProcesses.get(processSelectIndex).getProcessType() == ProcessType.CASE_PROCESS){
                pDiagramPanel.displayCProcess(prefixPName, suffixPName, processXCoordinate, processYCoordinate,
                    processWidth, processHeight, processArcWidth, processArcHeight,
                    processFontName, processFontSize);
            }

            else{
                 pDiagramPanel.displayCMProcess(prefixPName, suffixPName, sOrEs, processXCoordinate, processYCoordinate,
                    processWidth, processHeight, processArcWidth, processArcHeight,
                    processFontName, processFontSize);
            }

        }

    }// end displayCP method

/******************************************************************************/
    // Handle events in RPAGE Palette Panel
/******************************************************************************/

   /********************************************************************/
    // UOW Palette Toll
   /********************************************************************/
   /**
    * Handle events in RPAGE Palette Panel - Create new UOW

    * @param actionEvent
    */
    public void uowPalUOWSymbolButton(ActionEvent actionEvent){
        // user presses uow button in the RPAGE Palette tool
        shapeMode = ShapeMode.UOW;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();

        propertyController.removeShape();
        propertyController.removePropertyPanel();

        uowDiagramPanel.notifyDisplayMessage("Use Mouse to Draw UOW");
    }

    /**
     * Handle mouse events in RPAGE Palette Panel - Create new UOW relationship
     * @param actionEvent
     */
    public void uowPalRelButton(ActionEvent actionEvent){
        // user presses uow relationship button in the RPAGE Palette tool
        shapeMode = ShapeMode.UOW_RELATIONSHIP;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();
        propertyController.removeShape();
        propertyController.removePropertyPanel();

        // display rectangles within uow so that user can choose
        for ( UOW uow: arrayOfUOWs){
             uow.setDisplayRect(true);
        }

        // display rectangles within outside world symbol so that user can choose
        for ( OutSideWorld outSideWorld: arrayOfOSWSymbols){
             outSideWorld.setDisplayRect(true);
        }
        uowDiagramPanel.notifyDisplayMessage("Use Mouse to Draw UOW Relationship");

        // user is allowed to click twice
        firstClickedFlag = true;
        secondClickedFlag = true;

        //repaint UOW DrawingPanel
        uowDiagramPanel.repaint();

    }

   /**
    * Handle mouse events in RPAGE Pallette Panel -
    * Create new new relationship name in UOW diagram
    * @param actionEvent
    */
    public void uowPalRelNameButton(ActionEvent actionEvent){
        // user presses uow relationship name button in the RPAGE Palette tool
        shapeMode = ShapeMode.UOW_RELATIONSHIP_NAME;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();

        propertyController.removeShape();
        propertyController.removePropertyPanel();

        uowDiagramPanel.notifyDisplayMessage("Use Mouse to Draw UOW relationship name");

    }// end method

   /**
    * Handle mouse events in RPAGE Pallette Panel -
    * Create new new outside world symbol
    * @param actionEvent
    */
    public void uowPalOutsideWorldButton(ActionEvent actionEvent){
        // user presses uotside world symbol button in the RPAGE Palette tool
        shapeMode = ShapeMode.OUTSIDE_WORLD;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();

        propertyController.removeShape();
        propertyController.removePropertyPanel();

        uowDiagramPanel.notifyDisplayMessage("Use Mouse to Draw outside world symbol");

    }

   /********************************************************************/
    // PROCESS Palette Toll
   /********************************************************************/
   /**
    * Handle action events in RPAGE Palette Panel - Process Palette Panel
    * Create a new case process in process diagram
    * @param actionEvent
    */
    public void pPalCPSymbolButton(ActionEvent actionEvent){
        shapeMode = shapeMode.CASE_PROCESS;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();

        propertyController.removeShape();
        propertyController.removePropertyPanel();

        pDiagramPanel.notifyDisplayMessage("Use mouse to draw case process");

        //repaint process DrawingPanel
        pDiagramPanel.repaint();

    }

   /**
    * Handle mouse events in RPAGE Pallette Panel -
    * Create a new case management process in process diagram
    * @param actionEvent
    */
    public void pPalCMPSymbolButton(ActionEvent actionEvent){
        shapeMode = shapeMode.CASE_MANAGEMENT_PROCESS;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();

        propertyController.removeShape();
        propertyController.removePropertyPanel();

        pDiagramPanel.notifyDisplayMessage("Use mouse to draw case management process");

        //repaint process DrawingPanel
        pDiagramPanel.repaint();

    }

   /**
    * Handle mouse events in RPAGE Pallette Panel -
    * Create a new process relationship in process diagram
    * @param actionEvent
    */
    public void pPalRelSymbolButton(ActionEvent actionEvent){
        shapeMode = shapeMode.PROCESS_RELATIONSHIP;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();
        propertyController.removeShape();
        propertyController.removePropertyPanel();

        // display rectangles within process so that user can choose
        for (Process process: arrayOfProcesses){
             process.setDisplayRect(true);
        }


        // user is allowed to click twice
        firstClickedFlag = true;
        secondClickedFlag = true;

        pDiagramPanel.notifyDisplayMessage("Use mouse to draw process relatioship");

        //repaint process DrawingPanel
        pDiagramPanel.repaint();

    }

    public void pPalRelNameButton(ActionEvent actionEvent){
        // user presses process relationship name button in the RPAGE Palette tool
        shapeMode = ShapeMode.PROCESS_RELATIONSHIP_NAME;
        initDefault();

        // user not selects any shape in the drawing area
        notSelectAnyShape();
        propertyController.removeShape();
        propertyController.removePropertyPanel();

        pDiagramPanel.notifyDisplayMessage("Use mouse to draw process relationship name");

        //repaint process DrawingPanel
        pDiagramPanel.repaint();

    }

/******************************************************************************/
    // Handle mouse events in RPAGE Palette Panel
/******************************************************************************/
   /**
    * Handle mouse events in RPAGE Palette Panel - MouseMoved
    * @param mouseEvent
    */
    public void paletteMouseMoved(MouseEvent mouseEvent){
        // request focus so that key events can be handled
        ((JPanel)mouseEvent.getSource()).requestFocus();
    }
/******************************************************************************/
    // Handle key pressed events in RPAGE Palette Panel
/******************************************************************************/
   /**
    * Handle key events in RPAGE Paletter Panel
    * When user presses a key on the keyboard, events described by the type KeyEvent are generated
    * @param keyEvent
    */
    public void keyPressedInPalette(KeyEvent keyEvent){
        int keyCode = keyEvent.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
        switch (keyCode){
            case KeyEvent.VK_ESCAPE:
                shapeMode = ShapeMode.NONE;
                initDefault();
                ((JPanel)keyEvent.getSource()).repaint();
                break;
        }
    }
/******************************************************************************/
    // Handle mouse events in UOW Drawing Panel
/******************************************************************************/
    /**
     * Handle mouse events in UOW drawing Panel - MOUSE PRESSED
     * @param mouseEvent
     */
    public void uowDrawingMousePressed(MouseEvent mouseEvent){
        // uow drawing panel requests to be in focus so that it can handle keyboard events
        ((JPanel)mouseEvent.getSource()).requestFocus();

        // x,y coordinates of mouse when clicked
        mousePressedX = mouseEvent.getX();
        mousePressedY = mouseEvent.getY();

         // unbind current registered shape or panel
         propertyController.removeShape();
         propertyController.removePropertyPanel();

         // 1. user wants to create an OUTSIDE WORLD SYMBOL
         if (shapeMode == ShapeMode.OUTSIDE_WORLD){

             // default size of this outside world symbol
             int oswSize = 40;

             int i = 0;
             while (isExistingOSWName("OutSideWorld" + i)) {
                 i++;
             }

             // name of outside world symbol ("OutSideWorld0, OutSideWorld1...)
             String oswSymbolName = "OutSideWorld" + i;

             // create a new outside world symbol
             newOSWSymbol = new OutSideWorld(oswSize, mousePressedX, mousePressedY, oswSymbolName);

             // add this new outside world symbol to model
             arrayOfOSWSymbols.add(newOSWSymbol);

             // oswSelectIndex >= 0
             oswSelectIndex = arrayOfOSWSymbols.size() - 1;
         }

         // 2. user wants to create a new UOW.
         // There is no mouse released event after user has created a new UOW
         // When user creates a new UOW, a case process and case management process
         // will be created
         else if (shapeMode == ShapeMode.UOW){

             // x, y coordinate of the mouse
             uowX = mousePressedX;
             uowY = mousePressedY;

             // input dialog for user to enter uow name
             String newUOWName = JOptionPane.showInputDialog(
                      ((JPanel)mouseEvent.getSource()),
                      "Please type in uow name: ");

             // user has not entered any name
             if (newUOWName == null || newUOWName.equalsIgnoreCase("")
                     || StringUtilities.deleteSpace(newUOWName).equalsIgnoreCase("")){
                 // exit
                 shapeMode =ShapeMode.NONE;
                 initDefault();
             }

             // user has entered a new UOW name and this name does not exist so
             // a new uow can be created
             else if (newUOWName != null 
                     && !isExistingUOWName(StringUtilities.deleteSpace(newUOWName))
                     && !StringUtilities.deleteSpace(newUOWName).equalsIgnoreCase("")){
                 
                 newUOWName = StringUtilities.deleteSpace(newUOWName);

                 // restore to default state
                 shapeMode = ShapeMode.NONE;
                 initDefault();

                 // create a new uow
                 newUOW = new UOW(uowX,uowY, newUOWName);
                 newUOW.initDefault();

                 // add this new UOW into array of UOWs
                 arrayOfUOWs.add(newUOW);

                 // user not selects any shape in the drawing area
                 notSelectAnyShape();

                 // user selects this new shape
                 uowSelectIndex = arrayOfUOWs.size() - 1;

                 // create a case process and a case management process
                 // "Handle a <UOW>" and "Manage the flow of <UOWs>"
                 Process newCP = new Process( mousePressedX, mousePressedY, 105, 45, "Handle a ",
                         newUOW.getUOWName(), ProcessType.CASE_PROCESS);
                 arrayOfProcesses.add(newCP);

                 Process newCMP = new Process(mousePressedX + 120, mousePressedY, 105, 45, "Manage the flow of ",
                         newUOW.getUOWName(), ProcessType.CASE_MANAGEMENT_PROCESS);
                 arrayOfProcesses.add(newCMP);

                 // add this new UOW into property controller
                 // There is no mouse released event after user has created a new UOW
                 propertyController.addShape(arrayOfUOWs.get(uowSelectIndex));

                 // needs only one view
                 propertyController.regUOWPropertyPanel();

                 // tell UOW property panel to display the selected UOW shape's properties
                 displayUOW(uowSelectIndex);

                  // the new UOW is red
                  arrayOfUOWs.get(uowSelectIndex).setUOWColor(Color.red);

                  // the user has modified the diagram without saving
                  isLastSaved = false;

                  uowDiagramPanel.notifyDisplayMessage("UOW " + "<" + arrayOfUOWs.get(uowSelectIndex).getUOWName() +
                    ">" + " has been created");
             }

             // user has entered a new UOW name and this name has already been in use
             else {
                 int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)mouseEvent.getSource()),
                         "Would you like to reenter a new name?",
                         "This name has already been in use",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

                 // user wants to reenter a new name
                 if (userInput == JOptionPane.YES_OPTION){
                     shapeMode = ShapeMode.UOW;
                 }

                 // user wants to cancel
                 else{
                     initDefault();
                     shapeMode = ShapeMode.NONE;

                      // user not selects any shape in the drawing area
                     notSelectAnyShape();
                 }
             }
         }//end else if - user wants to create a new UOW.

         // 3. user want to create a new UOW relationship.
         // User has to click twice

         // 3.1 This is FIRST CLICK
         else if (shapeMode == ShapeMode.UOW_RELATIONSHIP && (firstClickedFlag == true)){

            uowRelX1Coordinate = mouseEvent.getX();
            uowRelY1Coordinate = mouseEvent.getY();

            // the index of uow contains the x, y coordinate
            // the new relationship will go from this uow
            firstUOWRelIndex = getUOWRectSelectedIndex
                    (uowRelX1Coordinate, uowRelY1Coordinate,
                    uowStartIndex);

            // the new relationship will altenatively go from an outside world symbol
            oswRelIndex = getOSWRectSelectedIndex
                    (uowRelX1Coordinate, uowRelY1Coordinate);

            // 3.1.1 user first click an existing UOW
            if (firstUOWRelIndex >= 0){
                oswRelIndex = -1;

                // the index of the rectangles within the UOW
                firstUOWRectIndex = arrayOfUOWs.get(firstUOWRelIndex).
                    getUOWRectIndex(uowRelX1Coordinate,
                    uowRelY1Coordinate);

                if (firstUOWRectIndex >= 0){
                    arrayOfUOWs.get(firstUOWRelIndex).setRectColor(
                        firstUOWRectIndex, Color.red);

                    uowRelX1Coordinate =
                        arrayOfUOWs.get(firstUOWRelIndex).getXCoordinateOfPoint(firstUOWRectIndex);

                    uowRelY1Coordinate =
                        arrayOfUOWs.get(firstUOWRelIndex).getYCoordinateOfPoint(firstUOWRectIndex);

                    // user clicks within a rectangle of a uow
                    allowCreatedUOWRelFlag = true;
                }

                // user not clicks within a rectangle of a uow
                else allowCreatedUOWRelFlag = false;
            }

            // 3.1.2 user first click an outside world symbol
            else if (oswRelIndex >= 0){
                 firstOSWRectIndex = arrayOfOSWSymbols.get(oswRelIndex).
                    getOSWRectIndex(uowRelX1Coordinate,
                    uowRelY1Coordinate);

                 if (firstOSWRectIndex >= 0){
                    arrayOfOSWSymbols.get(oswRelIndex).setRectColor(
                        firstOSWRectIndex, Color.red);

                    uowRelX1Coordinate =
                        arrayOfOSWSymbols.get(oswRelIndex).getXCoordinateOfPoint(firstOSWRectIndex);

                    uowRelY1Coordinate =
                        arrayOfOSWSymbols.get(oswRelIndex).getYCoordinateOfPoint(firstOSWRectIndex);

                    // user clicks within a rectangle of a outside world symbol
                    allowCreatedUOWRelFlag = true;
                 }

                 // user not clicks within a rectangle of a outside world symbol
                 else allowCreatedUOWRelFlag = false;
            }

            // user has used one click
            firstClickedFlag = false;

         }//end if

         // 3.2 This is SECOND CLICK (create a new uow relationship)
         else if (shapeMode == ShapeMode.UOW_RELATIONSHIP && (firstClickedFlag == false)
                 && (secondClickedFlag = true)){

            uowRelX2Coordinate = mouseEvent.getX();
            uowRelY2Coordinate = mouseEvent.getY();

            secondUOWRelIndex = getUOWRectSelectedIndex(
                    uowRelX2Coordinate,
                    uowRelY2Coordinate, uowStartIndex);

            if (secondUOWRelIndex >= 0){
                secondUOWRectIndex = arrayOfUOWs.get(secondUOWRelIndex).
                    getUOWRectIndex(uowRelX2Coordinate,
                    uowRelY2Coordinate);

                if (secondUOWRectIndex >= 0 && allowCreatedUOWRelFlag == true){

                    arrayOfUOWs.get(secondUOWRelIndex).setRectColor(
                        secondUOWRectIndex, Color.red);

                    uowRelX2Coordinate =
                        arrayOfUOWs.get(secondUOWRelIndex).getXCoordinateOfPoint(secondUOWRectIndex);

                    uowRelY2Coordinate =
                        arrayOfUOWs.get(secondUOWRelIndex).getYCoordinateOfPoint(secondUOWRectIndex);

                   // a new relationship between two UOWs
                   // UOW_A -> UOW_B
                   if (firstUOWRelIndex >= 0){
                       String fromUOWName = arrayOfUOWs.get(firstUOWRelIndex).getUOWName();
                       String toUOWName = arrayOfUOWs.get(secondUOWRelIndex).getUOWName();
                       int rectIndexFrom = firstUOWRectIndex;
                       int rectIndexTo = secondUOWRectIndex;

                       newUOWRel = new RPAGERelationship(uowRelX1Coordinate,
                                  uowRelY1Coordinate, uowRelX2Coordinate,
                                  uowRelY2Coordinate, fromUOWName, toUOWName,
                                  rectIndexFrom, rectIndexTo, "UOW" );

                       arrayOfUOWs.get(firstUOWRelIndex).addUOWRel(newUOWRel);
                       arrayOfUOWs.get(secondUOWRelIndex).addUOWRel(newUOWRel);

                       // the user has modified the diagram without saving
                       isLastSaved = false;

                       // generate the basic service relationship between corresponding processes
                       if (getCProcess(fromUOWName) != null && getCMProcess(toUOWName) != null &&
                               getCProcess(toUOWName) != null){

                           String fromProcessName = getCProcess(fromUOWName).getProcessFullName(); // UOW_A
                           String toProcessName = getCMProcess(toUOWName).getProcessFullName(); // UOW_B

                           // first generate the relationship between Handle a <UOW_A> and Manage the flow of <UOW_Bs>
                           // check whether this relationship already exists
                           if (!getCProcess(fromUOWName).isExistingRelationship(fromProcessName, toProcessName)){
                               firstProcessRectIndex = 0;
                               processRelX1Coordinate = getCProcess(fromUOWName).getXCoordinateOfPoint(firstProcessRectIndex);
                               processRelY1Coordinate = getCProcess(fromUOWName).getYCoordinateOfPoint(firstProcessRectIndex);

                               secondProcessRectIndex = 3;
                               processRelX2Coordinate = getCMProcess(toUOWName).getXCoordinateOfPoint(secondProcessRectIndex);
                               processRelY2Coordinate = getCMProcess(toUOWName).getYCoordinateOfPoint(secondProcessRectIndex);

                               newProcessRel = new RPAGERelationship(processRelX1Coordinate,
                                       processRelY1Coordinate, processRelX2Coordinate, processRelY2Coordinate,
                                       fromProcessName, toProcessName, firstProcessRectIndex, secondProcessRectIndex, "PROCESS");

                               getCProcess(fromUOWName).addProcessRel(newProcessRel);
                               getCMProcess(toUOWName).addProcessRel(newProcessRel);
                           }

                           // generate the relationship between Manage the flow of <UOW_Bs> and Handle a <UOW_B>

                           fromProcessName = getCMProcess(toUOWName).getProcessFullName(); // UOW_B - CMP
                           toProcessName = getCProcess(toUOWName).getProcessFullName(); // UOW_B - CP

                           // check whether this relationship already exists
                           if (!getCMProcess(toUOWName).isExistingRelationship(fromProcessName, toProcessName)){
                                firstProcessRectIndex = 5;
                                processRelX1Coordinate = getCMProcess(toUOWName).getXCoordinateOfPoint(firstProcessRectIndex);
                                processRelY1Coordinate = getCMProcess(toUOWName).getYCoordinateOfPoint(firstProcessRectIndex);

                                secondProcessRectIndex = 2;
                                processRelX2Coordinate = getCProcess(toUOWName).getXCoordinateOfPoint(secondProcessRectIndex);
                                processRelY2Coordinate = getCProcess(toUOWName).getYCoordinateOfPoint(secondProcessRectIndex);

                                newProcessRel = new RPAGERelationship(processRelX1Coordinate,
                                        processRelY1Coordinate, processRelX2Coordinate, processRelY2Coordinate,
                                        fromProcessName, toProcessName, firstProcessRectIndex, secondProcessRectIndex, "PROCESS");

                                getCMProcess(toUOWName).addProcessRel(newProcessRel);
                                getCProcess(toUOWName).addProcessRel(newProcessRel);
                           }

                           // generate the relationship between Handle a <UOW_B> and Handle a <UOW_A>
                           fromProcessName = getCProcess(toUOWName).getProcessFullName(); // UOW_B - CP
                           toProcessName = getCProcess(fromUOWName).getProcessFullName(); // UOW_A - CP

                           // check whether this relationship already exists
                           if (!getCProcess(fromUOWName).isExistingRelationship(fromProcessName, toProcessName)){
                               firstProcessRectIndex = 3;
                               processRelX1Coordinate = getCProcess(toUOWName).getXCoordinateOfPoint(firstProcessRectIndex);
                               processRelY1Coordinate = getCProcess(toUOWName).getYCoordinateOfPoint(firstProcessRectIndex);

                               secondProcessRectIndex = 8;
                               processRelX2Coordinate = getCProcess(fromUOWName).getXCoordinateOfPoint(secondProcessRectIndex);
                               processRelY2Coordinate = getCProcess(fromUOWName).getYCoordinateOfPoint(secondProcessRectIndex);

                               newProcessRel = new RPAGERelationship(processRelX1Coordinate,
                                       processRelY1Coordinate, processRelX2Coordinate, processRelY2Coordinate,
                                       fromProcessName, toProcessName, firstProcessRectIndex, secondProcessRectIndex, "PROCESS");

                               getCProcess(toUOWName).addProcessRel(newProcessRel);
                               getCProcess(fromUOWName).addProcessRel(newProcessRel);
                           }
                       }// end if (generate relationships between corresponding processes
                   }// end if (a new relationship between two UOWs)

                   // a new relationship between outside world and a UOW
                   else if (oswRelIndex >= 0){
                       String fromUOWName = arrayOfOSWSymbols.get(oswRelIndex).getName();
                       String toUOWName = arrayOfUOWs.get(secondUOWRelIndex).getUOWName();
                       int rectIndexFrom = firstOSWRectIndex;
                       int rectIndexTo = secondUOWRectIndex;

                       newUOWRel = new RPAGERelationship(uowRelX1Coordinate,
                                  uowRelY1Coordinate, uowRelX2Coordinate,
                                  uowRelY2Coordinate, fromUOWName, toUOWName,
                                  rectIndexFrom, rectIndexTo, "OSW" );

                       arrayOfOSWSymbols.get(oswRelIndex).addUOWRel(newUOWRel);
                       arrayOfUOWs.get(secondUOWRelIndex).addUOWRel(newUOWRel);

                       // the user has modified the diagram without saving
                       isLastSaved = false;

                   }// end else if (a new relationship between outside world and a UOW)

                   allowCreatedUOWRelFlag = false;
                }
            }

            // user has finished drawing relationship so shapeMode = NONE;
            initDefault();
            shapeMode = ShapeMode.NONE;

            // user not selects any shape in the drawing area
            notSelectAnyShape();

            secondClickedFlag = false;
         }//end else if (user want to create a new UOW relationship SECOND CLICK)

         // 4. user wants to create a new uow relationship name
         else if (shapeMode == ShapeMode.UOW_RELATIONSHIP_NAME){
             mousePressedX = mouseEvent.getX();
             mousePressedY = mouseEvent.getY();

             String uowRelName = JOptionPane.showInputDialog(
                      ((JPanel)mouseEvent.getSource()),
                      "Please type in uow relationship name: ", "generates (m:n)");

             // user has not enter any name
             if (uowRelName == null || StringUtilities.deleteSpace(uowRelName).equalsIgnoreCase("")){
                 shapeMode =ShapeMode.NONE;
                 initDefault();
             }

             // user has entered the relationship name
             // There is no mouse released event after user has created a new uow relationship name
             else {
                 newRPAGELabel = new RPAGELabel(mousePressedX, mousePressedY, uowRelName);
                 newRPAGELabel.setZoomFactor(uowZoomFactor);
                 arrayOfUOWRelNames.add(newRPAGELabel);

                 initDefault();
                 shapeMode = ShapeMode.NONE;

                  // user not selects any shape in the drawing area
                 notSelectAnyShape();

                 // user selects this new shape
                 uowRelNameIndex = arrayOfUOWRelNames.size() - 1;

                 propertyController.addShape(arrayOfUOWRelNames.get(uowRelNameIndex));
                 propertyController.regUOWPropertyPanel();
                 displayUOWRelName(uowRelNameIndex);

                 // change UOW relationship name to RED color
                 arrayOfUOWRelNames.get(uowRelNameIndex).
                 setColor(Color.red);

                 // the user has modified the diagram without saving
                 isLastSaved = false;

                 uowDiagramPanel.notifyDisplayMessage("A new relationship name has been created");
             }
         }

         // 5. user click on the drawing panel without having chosen the shape mode
         else if (this.shapeMode == ShapeMode.NONE){
             mousePressedX = mouseEvent.getX();
             mousePressedY = mouseEvent.getY();

             // find what UOW the mouse is pointing at
             uowSelectIndex = getUOWSelectedIndex(mousePressedX,mousePressedY,
                     uowStartIndex); // 5.1 user wants to select an existing uow

             uowWithRelContainsXYIndex = getUOWRelSelected(mousePressedX,
                     mousePressedY); // 5.2 user wants to select an existing relationship

             uowRelNameIndex = getUOWRelNameSelectedIndex(mousePressedX,
                     mousePressedY); // 5.3 user wants to select an existing relationship name

             oswSelectIndex = getOSWSelectedIndex(mousePressedX,
                     mousePressedY);// 5.4 user wants to select an existing outside world symbol

                                    // 5.5 user selects nothing

             // can only handle each selected shape at a time. So if uowSelectIndex >= 0
             // uowWithRelContainsXYIndex = -1
             // uowRelationshipNameIndex = -1
             // 5.1 user selects an existing UOW
             if (uowSelectIndex >= 0){
                 uowStartIndex = (uowSelectIndex + 1) % arrayOfUOWs.size();

                 uowWithRelContainsXYIndex = -1;
                 uowRelNameIndex = -1;
                 oswSelectIndex = -1;

                 //x1Coordinate, x2Coordinate are used when user wants to drag this existing uow
                 x1Coordinate = mouseEvent.getX();
                 y1Coordinate = mouseEvent.getY();
             }

             // 5.2 user wants to select an existing UOW relationship
             if (uowWithRelContainsXYIndex >= 0){

                // user is selecting a relationship.
                // User clicks the mouse to move the control point of this relationship
                // but as this control point is
                // near another relationship mouse clicked point to this nearby relationship.
                // Therefore, we should change processWithRelContainsXYIndex to -1
                if (selectedUOWRel != null){
                   uowWithRelContainsXYIndex = -1;
                }

                 else if (selectedUOWRel == null){
                    selectedUOWRel = arrayOfUOWs.get(
                        uowWithRelContainsXYIndex).returnUOWRelContains(mousePressedX, mousePressedY);

                    initDefault();

                    selectedUOWRel.setIsDrawCircle(true);
                    selectedUOWRel.setRPAGERelColor(Color.red);

                    // default controller will handle changes to this existing uow relationship
                    // therefore no need to register this uow relationship with property controller
                    // propertyController.addShape();

                    // tell UOW property panel to display the selected UOW relationship properties
                    uowDiagramPanel.displayUOWRel(selectedUOWRel.getFromShapeName(),
                            selectedUOWRel.getToShapeName(),
                            selectedUOWRel.getRectIndexFrom(),
                            selectedUOWRel.getRectIndexTo(), selectedUOWRel.getFromShapeType());

                    if(selectedUOWRel.circleContains(mousePressedX, mousePressedY)){
                        isUOWRelControlPointModified = true;
                    }
                }

                uowRelNameIndex = -1;
                oswSelectIndex = -1;
             }

             // user want to select the circle of the relationship
             // user has selected the relationship and then wants to select the circle
             if (uowWithRelContainsXYIndex == -1 &&
                     selectedUOWRel != null){

                 if(selectedUOWRel.circleContains(mousePressedX, mousePressedY)){
                     isUOWRelControlPointModified = true;
                 }
                 // deselect the existing relationship
                 else {
                     selectedUOWRel.setIsDrawCircle(false);
                     selectedUOWRel.setRPAGERelColor(Color.black);
                     selectedUOWRel = null;
                 }
             }

             // user
             if ((uowWithRelContainsXYIndex == -1) && (selectedUOWRel == null)){
                 isUOWRelControlPointModified = false;
                 initDefault();
             }

             // user wants to select an existing uow relationship name
             if (uowRelNameIndex >= 0){

                //x1Coordinate, x2Coordinate are used when user wants to drag this existing uow rel name
                x1Coordinate = mouseEvent.getX();
                y1Coordinate = mouseEvent.getY();

                oswSelectIndex = -1;
             }

             // user wants to select an existing out side world symbol
             if (oswSelectIndex >= 0){
                //x1Coordinate, x2Coordinate are used when user wants to drag this existing outside world symbol
                x1Coordinate = mouseEvent.getX();
                y1Coordinate = mouseEvent.getY();
             }
         } // end else if 5.user click on the drawing panel without having chosen the shape mode

         // repaint()
         ((JPanel)mouseEvent.getSource()).repaint();
    }

   /**
    * Handle mouse events in UOW drawing Panel - Mouse Clicked
    * @param mouseEvent
    */
    public void uowDrawingMouseClicked(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 2){
             mouseClickedX = mouseEvent.getX();
             mouseClickedY = mouseEvent.getY();
             uowRelNameIndex = getUOWRelNameSelectedIndex
                     (mouseClickedX, mouseClickedY);

             // user wants to modify an existing uow relationship name i.e. RPAGE Label
             if (uowRelNameIndex >= 0){
                 newRPAGELabel = arrayOfUOWRelNames.
                         get(uowRelNameIndex);

                 String uowRelName = JOptionPane.showInputDialog(
                      ((JPanel)mouseEvent.getSource()),
                      "Please type in uow relationship name: ",
                      newRPAGELabel.getRPAGELabelName());

                 // user has not enter any name
                 if (uowRelName == null){
                     shapeMode =ShapeMode.NONE;
                 }

                 // user has entered the relationship name
                 else{
                    newRPAGELabel.setRPAGELabelName(uowRelName);
                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    // repaint()
                    ((JPanel)mouseEvent.getSource()).repaint();
                 }
             }
         }
    }

    /**
     * Handle mouse events in UOW drawing Panel - Mouse Released
     * @param mouseEvent
     */
    public void uowDrawingMouseReleased(MouseEvent mouseEvent){
        // user has clicked twice

        // IMPORTANT CODE - For reference
        /*if ((secondClickedFlag == false) || (shapeMode != ShapeMode.UOW_RELATIONSHIP)){
            shapeMode = ShapeMode.NONE;
        }*/

        // 1. user click to select an existing UOW shape
        // When user create a new uow, there is no mouse released event or mouse clicked event
        // as user pressed enter to type the uow name
        if (uowSelectIndex >=0 && shapeMode != ShapeMode.UOW_RELATIONSHIP){

            // register UOW shape and property panel to display its properties
            propertyController.addShape(arrayOfUOWs.get(uowSelectIndex));
            propertyController.regUOWPropertyPanel();

            // tell UOW property panel to display the selected UOW shape's properties
            displayUOW(uowSelectIndex);

            // exit
            shapeMode =ShapeMode.NONE;
            initDefault();

            // set the color of the selected uow to red
            arrayOfUOWs.get(uowSelectIndex).setUOWColor(Color.red);

            uowDiagramPanel.notifyDisplayMessage("UOW " + "<" + arrayOfUOWs.get(uowSelectIndex).getUOWName() +
                    ">" + " is being selected");
        }

        // 2. user click to select an existing out side world symbol or
        // a new ouside world symbol has been created
        else if (oswSelectIndex >=0 && shapeMode != ShapeMode.UOW_RELATIONSHIP){
            propertyController.regUOWPropertyPanel();

            // register OSW shape and property panel to display its properties
            propertyController.addShape(arrayOfOSWSymbols.get(oswSelectIndex));

            // tell UOW property panel to display the selected Outside World shape's properties
            displayOSW(oswSelectIndex);

            // exit
            shapeMode =ShapeMode.NONE;
            initDefault();

            // set the color of the selected outside world symbol to red
            arrayOfOSWSymbols.get(oswSelectIndex).setColor(Color.red);

            uowDiagramPanel.notifyDisplayMessage("A new outside world symbol is being selected");
        }

        // 3. user clicks to space
        // when user has finished creating a new uow relationship
        // uowSelectIndex == -1, uowRelNameIndex == -1,  shapeMode == ShapeMode.NONE
        // else if would make an error. Therefore, add newUOWRel != null
        else if  (uowSelectIndex == -1 && uowRelNameIndex == -1 && oswSelectIndex == -1 &&
                shapeMode == ShapeMode.NONE && selectedUOWRel == null ){
            initDefault();
            // user not selects any shape in the drawing area
                 notSelectAnyShape();
            propertyController.removeShape();
            propertyController.removePropertyPanel();
            uowDiagramPanel.notDisplayUOW();
            uowDiagramPanel.notDisplayUOWRelName();
            uowDiagramPanel.notDisplayUOWRel();
        }

        // 4. user wants to select an existing uow relationship name
        else if (uowRelNameIndex >= 0){
            // register UOW shape and property panel to display its properties
            propertyController.addShape(arrayOfUOWRelNames.get(uowRelNameIndex));
            propertyController.regUOWPropertyPanel();
            displayUOWRelName(uowRelNameIndex);

            // exit
            shapeMode =ShapeMode.NONE;
            initDefault();

            // change UOW relationship name to RED color
            arrayOfUOWRelNames.get(uowRelNameIndex).
                 setColor(Color.red);

            uowDiagramPanel.notifyDisplayMessage("A relationship name is being selected");
        }

        // repaint()
        ((JPanel)mouseEvent.getSource()).repaint();

    }// end mouseReleased

    /**
     * Handle mouse events in UOW drawing Panel - Mouse Dragged
     * @param mouseEvent
     */
    public void uowDrawingMouseDragged(MouseEvent mouseEvent){
        mouseDraggedX = mouseEvent.getX();
        mouseDraggedY = mouseEvent.getY();

        if (mouseDraggedX < 0){
            mouseDraggedX = 0;
        }

        if (mouseDraggedY < 0){
            mouseDraggedY = 0;
        }

        // 1. user wants to drag an existing Out side world symbol
        if (shapeMode == ShapeMode.NONE && (oswSelectIndex >= 0)){

            int xCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getXCoordinate();
            int yCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getYCoordinate();
            xCoordinate = xCoordinate + mouseDraggedX - x1Coordinate;
            yCoordinate = yCoordinate + mouseDraggedY - y1Coordinate;
            x1Coordinate = mouseDraggedX;
            y1Coordinate = mouseDraggedY;

            if (xCoordinate < 0){
                xCoordinate = 0;
            }

            if (yCoordinate < 0){
                yCoordinate = 0;
            }
            arrayOfOSWSymbols.get(oswSelectIndex).setXCoordinate(xCoordinate);
            arrayOfOSWSymbols.get(oswSelectIndex).setYCoordinate(yCoordinate);

            // the user has modified the diagram without saving
            isLastSaved = false;
        }

        // 2. user wants to drag an existing UOW
        else if (shapeMode == ShapeMode.NONE && (uowSelectIndex >= 0)){
            propertyController.removeShape();

            int xCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWXCoordinate();
            int yCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWYCoordinate();

            xCoordinate = xCoordinate + mouseDraggedX - x1Coordinate;
            yCoordinate = yCoordinate + mouseDraggedY - y1Coordinate;
            x1Coordinate = mouseDraggedX;
            y1Coordinate = mouseDraggedY;
            //When change both x and y cannot move the UOW shape
            //defaultController.setUOWXYCoordinate(selectIndex,x,y)
            // it's ok to change x then y.

            if (xCoordinate < 0){
                xCoordinate = 0;
            }

            if (yCoordinate < 0){
                yCoordinate = 0;
            }

            arrayOfUOWs.get(uowSelectIndex).setUOWXCoordinate(xCoordinate);
            arrayOfUOWs.get(uowSelectIndex).setUOWYCoordinate(yCoordinate);

            // the user has modified the diagram without saving
            isLastSaved = false;
         }

        // 3. user wants to drag the control point of uow relationship
        else if (isUOWRelControlPointModified == true
                && selectedUOWRel != null){
             selectedUOWRel.setControlPoint(mouseDraggedX, mouseDraggedY);
        }

        // 4. user wants to drag an existing uow relationship name
        else if (shapeMode == ShapeMode.NONE && uowRelNameIndex >= 0){

            int xCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).
                    getRPAGELabelXCoordinate();
            int yCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).
                    getRPAGELabelYCoordinate();

            xCoordinate = xCoordinate + mouseDraggedX - x1Coordinate;
            yCoordinate = yCoordinate + mouseDraggedY - y1Coordinate;
            x1Coordinate = mouseDraggedX;
            y1Coordinate = mouseDraggedY;
            //When change both x and y cannot move the UOW shape
            //defaultController.setUOWXYCoordinate(selectIndex,x,y)
            // it's ok to change x then y.

            if (xCoordinate < 0){
                xCoordinate = 0;
            }

            if (yCoordinate < 0){
                yCoordinate = 0;
            }

            arrayOfUOWRelNames.get(uowRelNameIndex).
                    setRPAGELabelXCoordinate(xCoordinate);

            arrayOfUOWRelNames.get(uowRelNameIndex).
                    setRPAGELabelYCoordinate(yCoordinate);

            // the user has modified the diagram without saving
            isLastSaved = false;
        }
         // repaint()
         ((JPanel)mouseEvent.getSource()).repaint();
    }// end mouse dragged

    /**
    * Handle mouse events in UOW drawing Panel - MouseMoved
    * @param mouseEvent
    */
    public void uowDrawingMouseMoved(MouseEvent mouseEvent){
        // request focus so that key events can be handled
        ((JPanel)mouseEvent.getSource()).requestFocus();

        // create an outside world symbol when user moves the mouse
        if (shapeMode == ShapeMode.OUTSIDE_WORLD){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();
            int size = 40;

            oswCreatedByMouseMove = new OutSideWorld(40, mouseMovedX, mouseMovedY, "Sample OSW");
            oswCreatedByMouseMove.setColor(Color.red);

            // repaint()
            expandUOWDrawingArea(mouseEvent, oswCreatedByMouseMove);
            ((JPanel)mouseEvent.getSource()).repaint();
        }// end if

        // create a new uow relationship name when user moves the mouse
        else if (shapeMode == ShapeMode.UOW_RELATIONSHIP_NAME){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            String uowRelName = "generates (multiplicity: multiplicity)";
            uowRelNameCreatedByMouseMove = new RPAGELabel(mouseMovedX,mouseMovedY, uowRelName);
            uowRelNameCreatedByMouseMove.setZoomFactor(uowZoomFactor);
            uowRelNameCreatedByMouseMove.setColor(Color.red);

            // repaint()
            ((JPanel)mouseEvent.getSource()).repaint();
        }// end else if

        // create a new uow when users move the mouse
        else if (shapeMode == ShapeMode.UOW){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            int i = 0;
            while (isExistingUOWName("Sample UOW" + i)) {
                 i++;
            }
            String uowName = "Sample UOW" + i;
            uowCreatedByMouseMove = new UOW(mouseMovedX,mouseMovedY, uowName);
            uowCreatedByMouseMove.setUOWColor(Color.red);

            // repaint()
            expandUOWDrawingArea(mouseEvent, uowCreatedByMouseMove);
            ((JPanel)mouseEvent.getSource()).repaint();
        }// end else if

        // create a new uow relationship when user moves the mouse - First click
        else if (shapeMode == ShapeMode.UOW_RELATIONSHIP && firstClickedFlag == true){
             mouseMovedX = mouseEvent.getX();
             mouseMovedY = mouseEvent.getY();

             uowMouseMovedIndex = getUOWRectSelectedIndex(mouseMovedX,
                    mouseMovedY, uowStartIndex);

             oswMouseMovedIndex = getOSWRectSelectedIndex(mouseMovedX,
                     mouseMovedY);

             if (uowMouseMovedIndex >= 0 || oswMouseMovedIndex >= 0){
                 //change mouse cursor
                 Cursor crossHairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
                 ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
             }

             else {
                 Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                 ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
             }

        }// end else if

        // create a new uow relationship when user moves the mouse - Second click
        else if (shapeMode == ShapeMode.UOW_RELATIONSHIP && secondClickedFlag == true){
             mouseMovedX = mouseEvent.getX();
             mouseMovedY = mouseEvent.getY();

             uowMouseMovedIndex = getUOWRectSelectedIndex(mouseMovedX,
                    mouseMovedY, uowStartIndex);

             oswMouseMovedIndex = getOSWRectSelectedIndex(mouseMovedX,
                     mouseMovedY);

             if (uowMouseMovedIndex >=0 || oswMouseMovedIndex >= 0){
                 //change mouse cursor
                 Cursor crossHairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
                 ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
             }

             else {
                 Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                 ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
             }

             uowRelCreatedByMouseMove = new RPAGERelationship(
                     uowRelX1Coordinate, uowRelY1Coordinate,
                     mouseMovedX, mouseMovedY, "UOW");
             uowRelCreatedByMouseMove.setRPAGERelColor(Color.red);
             //uowRelCreatedByMouseMove.setZoomFactor(uowZoomFactor);

            // repaint()
            ((JPanel)mouseEvent.getSource()).repaint();
         }

        else if (shapeMode == ShapeMode.NONE && isUOWRelControlPointModified == true){
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                  ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
        }

        else if (shapeMode == ShapeMode.NONE && isUOWRelControlPointModified == false){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            if (getUOWRelSelected(mouseMovedX, mouseMovedY) >= 0){
                 Cursor crossHairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
            }

             else {
              Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
              ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
            }
        }

        else {
              Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
              ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
        }
    }

/******************************************************************************/
    // Handle key pressed events in UOW Drawing Panel
/******************************************************************************/
    /**
    * Handle key events in UOW drawing Panel
    * When user presses a key on the keyboard, events described by the type KeyEvent are generated
    * @param keyEvent
    */
    public void keyPressedInUOWDrawing(KeyEvent keyEvent){
        int keyCode = keyEvent.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
        switch (keyCode){
            case KeyEvent.VK_ESCAPE:
                shapeMode = ShapeMode.NONE;
                initDefault();
                ((JPanel)keyEvent.getSource()).repaint();
                break;

            case KeyEvent.VK_DELETE:
                // 1. user wants to delete an existing uow
                if (uowSelectIndex >= 0){
                    int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)keyEvent.getSource()),
                         "Would you like to delete this uow?",
                         "Deleting UOW",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

                    // user confirms deleting
                    if (userInput == JOptionPane.YES_OPTION){
                        propertyController.removeShape();
                        propertyController.removePropertyPanel();

                        // Tells UOW property panel to remove all property values of the selected UOW
                        uowDiagramPanel.notDisplayUOW();

                        // deletes also the case process and case management process
                        // the parameter is the uow name that this case process or case management process deals with
                        removeProcess(arrayOfUOWs.get(uowSelectIndex).getUOWName());

                        // after delete the case process and case management process, delete the uow
                        removeUOW(uowSelectIndex);
                        uowSelectIndex = -1;

                        // the user has modified the diagram without saving
                        isLastSaved = false;
                    }

                    // user wants to cancel deleting the uow
                    else{
                        initDefault();
                        shapeMode = ShapeMode.NONE;

                        // user not selects any shape in the drawing area
                        notSelectAnyShape();
                    }

                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // 2. use wants to delete an existing UOW relationship
                else if (uowWithRelContainsXYIndex >= 0
                        && selectedUOWRel != null){

                    String fromUOWName = selectedUOWRel.getFromShapeName();
                    String toUOWName = selectedUOWRel.getToShapeName();
                    // delete relationship in from UOW

                    // the relationship is from an outsideworld symbol
                    if (getUOW(fromUOWName) == null){
                        getOSW(fromUOWName).removeRel(selectedUOWRel);
                    }

                    else {
                        getUOW(fromUOWName).
                        removeRel(selectedUOWRel);
                    }

                    // delete relationship in to UOW
                    getUOW(toUOWName).
                        removeRel(selectedUOWRel);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // 3. user wants to delete an outside world symbol
                else if (oswSelectIndex >= 0){
                    propertyController.removeShape();
                    propertyController.removePropertyPanel();

                    // Tells UOW property panel to remove all property values of the selected outside world
                    uowDiagramPanel.notDisplayOSW();

                    removeOSW(oswSelectIndex);
                    oswSelectIndex = -1;

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // 4. user wants to delete a relationship name
                else if (uowRelNameIndex >= 0){
                    arrayOfUOWRelNames.remove(uowRelNameIndex);
                    uowRelNameIndex = -1;

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_LEFT:
                // use moves an existing uow to the left
                if (uowSelectIndex >=0){
                    int newUOWXCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWXCoordinate() - 1;
                    if (newUOWXCoordinate < 0)
                        newUOWXCoordinate = 0;
                    arrayOfUOWs.get(uowSelectIndex).setUOWXCoordinate(newUOWXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing outside world symbol to the left
                else if (oswSelectIndex >=0){
                    int newOSWXCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getXCoordinate() - 1;
                    if (newOSWXCoordinate < 0)
                        newOSWXCoordinate = 0;
                    arrayOfOSWSymbols.get(oswSelectIndex).setXCoordinate(newOSWXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing uow relationship name to the left
                else if (uowRelNameIndex >=0){
                    int newUOWRelNameXCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelXCoordinate() - 1;
                    if (newUOWRelNameXCoordinate < 0)
                        newUOWRelNameXCoordinate = 0;
                    arrayOfUOWRelNames.get(uowRelNameIndex).setRPAGELabelXCoordinate(newUOWRelNameXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_RIGHT:
                // use moves an existing uow to the right
                if (uowSelectIndex >=0){
                    arrayOfUOWs.get(uowSelectIndex).setUOWXCoordinate(
                            (arrayOfUOWs.get(uowSelectIndex).getUOWXCoordinate() + 1));

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing outside world symbol to the right
                else if (oswSelectIndex >=0){
                    int newOSWXCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getXCoordinate() + 1;
                    if (newOSWXCoordinate < 0)
                        newOSWXCoordinate = 0;
                    arrayOfOSWSymbols.get(oswSelectIndex).setXCoordinate(newOSWXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing uow relationship name to the right
                else if (uowRelNameIndex >=0){
                    int newUOWRelNameXCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelXCoordinate() + 1;
                    if (newUOWRelNameXCoordinate < 0)
                        newUOWRelNameXCoordinate = 0;
                    arrayOfUOWRelNames.get(uowRelNameIndex).setRPAGELabelXCoordinate(newUOWRelNameXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_UP:
                // use moves up existing uow
                if (uowSelectIndex >=0){
                    int newUOWYCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWYCoordinate() - 1;
                    if (newUOWYCoordinate < 0)
                        newUOWYCoordinate = 0;
                    arrayOfUOWs.get(uowSelectIndex).setUOWYCoordinate(newUOWYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves up an existing outside world symbol
                else if (oswSelectIndex >=0){
                    int newOSWYCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getYCoordinate() - 1;
                    if (newOSWYCoordinate < 0)
                        newOSWYCoordinate = 0;
                    arrayOfOSWSymbols.get(oswSelectIndex).setXCoordinate(newOSWYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves up an existing uow relationship name
                else if (uowRelNameIndex >=0){
                    int newUOWRelNameYCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelYCoordinate() - 1;
                    if (newUOWRelNameYCoordinate < 0)
                        newUOWRelNameYCoordinate = 0;
                    arrayOfUOWRelNames.get(uowRelNameIndex).setRPAGELabelYCoordinate(newUOWRelNameYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_DOWN:
                // use moves down existing uow
                if (uowSelectIndex >=0){
                    int newUOWYCoordinate = arrayOfUOWs.get(uowSelectIndex).getUOWYCoordinate() + 1;
                    if (newUOWYCoordinate < 0)
                        newUOWYCoordinate = 0;
                    arrayOfUOWs.get(uowSelectIndex).setUOWYCoordinate(newUOWYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves down an existing outside world symbol
                else if (oswSelectIndex >=0){
                    int newOSWYCoordinate = arrayOfOSWSymbols.get(oswSelectIndex).getYCoordinate() + 1;
                    if (newOSWYCoordinate < 0)
                        newOSWYCoordinate = 0;
                    arrayOfOSWSymbols.get(oswSelectIndex).setXCoordinate(newOSWYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves down an existing uow relationship name
                else if (uowRelNameIndex >=0){
                    int newUOWRelNameYCoordinate = arrayOfUOWRelNames.get(uowRelNameIndex).getRPAGELabelYCoordinate() + 1;
                    if (newUOWRelNameYCoordinate < 0)
                        newUOWRelNameYCoordinate = 0;
                    arrayOfUOWRelNames.get(uowRelNameIndex).setRPAGELabelYCoordinate(newUOWRelNameYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;
        }
    }

/******************************************************************************/
    // Handle other events in UOW Drawing Panel
/******************************************************************************/

/******************************************************************************/
    // Handle events in UOW Property Panel
/******************************************************************************/
   /**
    * Handle events in UOW Property Panel
    * @param itemEvent
    */
    public void uowGridItemStateChanged(ItemEvent itemEvent){
        if (((JCheckBox)itemEvent.getSource()).isSelected()){
            turnOnUOWGridFlag = true;
        }
        else turnOnUOWGridFlag = false;
        uowDrawingArea.repaint();

    }// end method

    /********************************************************************/
    // OSW size
       /**
    * Handle events in UOW Property Panel by delegating to property controller
    * OSW Size - action performed
    * @param actionEvent
    */
    public void oswSizeTextFieldActionPerformed(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.oswSizeTextFieldActionPerformed(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Size - focus lost
    * @param focusEvent
    */
    public void oswSizeTextFieldFocusLost(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.oswSizeTextFieldFocusLost(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

    /********************************************************************/
    // UOW name, size, Coordinates, font name and font size
   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Name - action performed
    * @param actionEvent
    */
    public void uowNameTextFieldActionPerformed(ActionEvent actionEvent){
        String uowName = ((JTextField)actionEvent.getSource()).getText();
        uowName = StringUtilities.deleteSpace(uowName);
        if (uowSelectIndex >= 0 && !uowName.equalsIgnoreCase("")){
            String oldUOWName = arrayOfUOWs.get(uowSelectIndex).getUOWName();

            // user just wants to change from lower case to upper case
            if (oldUOWName.equalsIgnoreCase(uowName) && !oldUOWName.equals(uowName)){
                // change uow name
                // delegate the processing of this event to propertyController
                propertyController.uowNameTextFieldActionPerformed(actionEvent);

                // also change case process and case management process name
                int cpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_PROCESS){
                        cpIndex = i;
                    }
                }
                if (cpIndex >= 0){
                    arrayOfProcesses.get(cpIndex).setSuffixPName(uowName);
                }

                int cmpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                        cmpIndex = i;
                    }
                }
                if (cmpIndex >= 0){
                    arrayOfProcesses.get(cmpIndex).setSuffixPName(uowName);
                }

                uowDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }

            // the name exist so that user cannot change uow name
            else if (!oldUOWName.equalsIgnoreCase(uowName) && isExistingUOWName(uowName)){
                uowDiagramPanel.notifyDisplayMessage("\"" + uowName + "\"" + " has been used. Please choose another name");
            }

            else if (!uowName.equalsIgnoreCase("") && !oldUOWName.equals(uowName)){
                // change uow name
                // delegate the processing of this event to propertyController
                propertyController.uowNameTextFieldActionPerformed(actionEvent);

                // also change case process and case management process name
                int cpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_PROCESS){
                        cpIndex = i;
                    }
                }
                if (cpIndex >= 0){
                    arrayOfProcesses.get(cpIndex).setSuffixPName(uowName);
                }

                int cmpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                        cmpIndex = i;
                    }
                }
                if (cmpIndex >= 0){
                    arrayOfProcesses.get(cmpIndex).setSuffixPName(uowName);
                }

                uowDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }
            // the user has modified the diagram without saving
            isLastSaved = false;
        }// end if
        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Name - focus lost
    * @param itemEvent
    */
    public void uowNameTextFieldFocusLost(FocusEvent focusEvent){
       String uowName = ((JTextField)focusEvent.getSource()).getText();
       uowName = StringUtilities.deleteSpace(uowName);

       if (uowSelectIndex >= 0 && !uowName.equalsIgnoreCase("")){
            String oldUOWName = arrayOfUOWs.get(uowSelectIndex).getUOWName();

            // user just wants to change from lower case to upper case
            if (oldUOWName.equalsIgnoreCase(uowName) && !oldUOWName.equals(uowName)){
                // change uow name
                // delegate the processing of this event to propertyController
                propertyController.uowNameTextFieldFocusLost(focusEvent);

                // also change case process and case management process name
                int cpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_PROCESS){
                        cpIndex = i;
                    }
                }
                if (cpIndex >= 0){
                    arrayOfProcesses.get(cpIndex).setSuffixPName(uowName);
                }

                int cmpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                        cmpIndex = i;
                    }
                }
                if (cmpIndex >= 0){
                    arrayOfProcesses.get(cmpIndex).setSuffixPName(uowName);
                }

                uowDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }

            // the name exist so that user cannot change uow name
            else if (!oldUOWName.equalsIgnoreCase(uowName) && isExistingUOWName(uowName)){
                uowDiagramPanel.notifyDisplayMessage("\"" + uowName + "\"" + " has been used. Please choose another name");
            }

            else if (!uowName.equalsIgnoreCase("") && !oldUOWName.equals(uowName)){
                // change uow name
                // delegate the processing of this event to propertyController
                propertyController.uowNameTextFieldFocusLost(focusEvent);

                // also change case process and case management process name
                int cpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_PROCESS){
                        cpIndex = i;
                    }
                }
                if (cpIndex >= 0){
                    arrayOfProcesses.get(cpIndex).setSuffixPName(uowName);
                }

                int cmpIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(i).getSuffixPName().equalsIgnoreCase(oldUOWName)
                        && arrayOfProcesses.get(i).getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                        cmpIndex = i;
                    }
                }
                if (cmpIndex >= 0){
                    arrayOfProcesses.get(cmpIndex).setSuffixPName(uowName);
                }

                uowDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }
            // the user has modified the diagram without saving
            isLastSaved = false;
        }// end if
        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Size - action performed
    * @param itemEvent
    */
    public void uowSizeTextFieldActionPerformed(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowSizeTextFieldActionPerformed(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Size - focus lost
    * @param itemEvent
    */
    public void uowSizeTextFieldFocusLost(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowSizeTextFieldFocusLost(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW FONT NAME
    * @param itemEvent
    */
    public void uowFontNameComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowFontNameComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW FONT SIZE
    * @param itemEvent
    */
    public void uowFontSizeComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowFontSizeComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW X Coordinate - action performed
    * @param itemEvent
    */
    public void uowXCoordinateTextFieldActionPerformed(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowXCoordinateTextFieldActionPerformed(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW X Coordinate - focus lost
    * @param itemEvent
    */
    public void uowXCoordinateTextFieldFocusLost(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowXCoordinateTextFieldFocusLost(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Y Coordinate - action performed
    * @param itemEvent
    */
    public void uowYCoordinateTextFieldActionPerformed(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowYCoordinateTextFieldActionPerformed(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Y Coordinate - focus lost
    * @param itemEvent
    */
    public void uowYCoordinateTextFieldFocusLost(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowYCoordinateTextFieldFocusLost(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }// end method


   /********************************************************************/
   // UOW RELATIONSHIP
   /**
    * Handle events in UOW Relationship Property Panel
    * @param itemEvent
    */
    public void fromUOWRectComboBoxAP(ActionEvent actionEvent){
        if (selectedUOWRel != null){
            int fromRectIndex = (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString()));
            selectedUOWRel.setRectIndexFrom(fromRectIndex);
            for (UOW uow: arrayOfUOWs){
                uow.setRelFromRectIndex(selectedUOWRel, fromRectIndex);
            }

            for (OutSideWorld osw: arrayOfOSWSymbols){
                System.out.println("a");
                osw.setRelFromRectIndex(selectedUOWRel, fromRectIndex);
            }
        }

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }

   /**
    * Handle events in UOW Relationship Property Panel
    * @param itemEvent
    */
    public void toUOWRectComboBoxAP(ActionEvent actionEvent){
        if (selectedUOWRel != null){
            int toRectIndex = (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString()));
            for (UOW uow: arrayOfUOWs){
                uow.setRelToRectIndex(selectedUOWRel, toRectIndex);
            }
        }

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }

    /********************************************************************/
    // uow RELATIONSHIP NAME
   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * RPAGE Name - action performed
    * @param actionEvent
    */
    public void uowRPAGENameTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowRPAGENameTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * RPAGE Name - focus lost
    * @param focusEvent
    */
    public void uowRPAGENameTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.uowRPAGENameTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
    }

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Relationship name font name
    * @param itemEvent
    */
    public void rpageFontNameComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.rpageFontNameComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * UOW Relationship name font size
    * @param itemEvent
    */
    public void rpageFontSizeComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.rpageFontSizeComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        uowDrawingArea.repaint();
        pDrawingArea.repaint();
    }// end method

/******************************************************************************/
    // Handle mouse events in PROCESS Drawing Panel
/******************************************************************************/
    /**
     * Handle mouse events in process drawing Panel - MOUSE PRESSED
     * @param mouseEvent
     */
    public void pDrawingMousePressed(MouseEvent mouseEvent){

        // first cut drawing panel requests to be in focus so that it can handle keyboard events
        ((JPanel)mouseEvent.getSource()).requestFocus();

        // x,y coordinates of mouse when clicked
        mousePressedX = mouseEvent.getX();
        mousePressedY = mouseEvent.getY();

        // unbind current registered shape or panel
        propertyController.removeShape();
        propertyController.removePropertyPanel();

        // 1. user want to create a new case process.
        // There is no mouse released event after user has created a new case process
        // When user creates a new case process, a new UOW will be generated
        if (shapeMode == ShapeMode.CASE_PROCESS){

            // x, y coordinate of the new case process
            cpX = mousePressedX;
            cpY = mousePressedY;

            // input dialog for user to enter uow name that this new case process deals with
            String uowName = JOptionPane.showInputDialog(
                    ((JPanel)mouseEvent.getSource()),
                    "Please type in the UOW name that this case process will deal" +
                    " with: ",
                    "uow");
            
            // user has not enter any name
            if (uowName == null || uowName.equalsIgnoreCase("")
                    || StringUtilities.deleteSpace(uowName).equalsIgnoreCase("")){
                 // exit
                 shapeMode =ShapeMode.NONE;
                 initDefault();
             }

             // user has entered a new case process name and this name does not exist so
             // a new case process can be created
             else if (uowName != null
                      && !isExistingProcessName(StringUtilities.deleteSpace(uowName))
                      && !isExistingProcessName(StringUtilities.deleteSpace(uowName))
                      && !isExistingUOWName(StringUtilities.deleteSpace(uowName))
                      && !StringUtilities.deleteSpace(uowName).equalsIgnoreCase("")){

                 // restore to default state
                 shapeMode = ShapeMode.NONE;
                 initDefault();

                 // delete extra white spaces in this uow name
                 uowName = StringUtilities.deleteSpace(uowName);

                 // create a case process
                 // "Handle a <UOW>" or "Prepare a <UOW>"
                 Process newCP = new Process(cpX , cpY, 105, 45, "Handle a ", uowName, ProcessType.CASE_PROCESS);

                 // add this new case process into array of case processes
                 arrayOfProcesses.add(newCP);

                 // user not selects any shape in the drawing area
                 notSelectAnyShape();

                 // user selects this new shape
                 processSelectIndex = arrayOfProcesses.size() - 1;

                 // create a case management process
                 // "Manage the flow of <UOWs>
                 Process newCMP = new Process(cpX + 130, cpY, 105, 45, "Manage the flow of ", uowName, ProcessType.CASE_MANAGEMENT_PROCESS);

                 arrayOfProcesses.add(newCMP); // add this new case management process

                 // create a new uow that this case process deals with
                 newUOW = new UOW(cpX, cpY, uowName);
                 newUOW.initDefault();
                 arrayOfUOWs.add(newUOW); // add this new UOW into array of UOWs

                 // add this new case process into property controller
                 propertyController.addShape(arrayOfProcesses.get(processSelectIndex));

                 propertyController.regProcessPropertyPanel();

                 // tell first cut property panel to display the selected case process shape's properties
                 displayProcess(processSelectIndex);

                 // the new case process is red
                 arrayOfProcesses.get(processSelectIndex).setProcessColor(Color.red);

                 pDiagramPanel.notifyDisplayMessage("A new case process that deals with <UOW> " +
                    arrayOfProcesses.get(processSelectIndex).getSuffixPName() + " has been created");

                 // the user has modified the diagram without saving
                 isLastSaved = false;

             }

             // user has entered a new UOW name and this name has already been in use
             else {
                 int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)mouseEvent.getSource()),
                         "Would you like to reenter a new name?",
                         "This name has already been in use",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);
                 // user wants to reenter a new name
                 if (userInput == JOptionPane.YES_OPTION){
                     shapeMode = ShapeMode.CASE_PROCESS;
                 }

                 // user wants to cancel
                 else{
                     initDefault();
                     shapeMode = ShapeMode.NONE;

                      // user not selects any shape in the drawing area
                     notSelectAnyShape();
                 }
             }
        }

        // 2. user want to create a new case management process.
        // There is no mouse released event after user has created a new case process
        // When user creates a new case management process, if there is already an existing UOW and case process
        // only a case management process is created.

        // If there is no case process and uow. A new uow and case process will be created.
        else if (shapeMode == ShapeMode.CASE_MANAGEMENT_PROCESS){

            // x, y coordinate of the new case process
            cpX = mousePressedX;
            cpY = mousePressedY;

            // input dialog for user to enter uow name for this new case management process
            String uowName = JOptionPane.showInputDialog(
                    ((JPanel)mouseEvent.getSource()),
                    "Please type in the UOW name that this case management process will deal with: ",
                    "uow");

             // user has not enter any name
             if (uowName == null || uowName.equalsIgnoreCase("")
                 || StringUtilities.deleteSpace(uowName).equalsIgnoreCase("")){
                 // exit
                 shapeMode =ShapeMode.NONE;
                 initDefault();
             }

             // user has entered a new case mangement process name and this name does not exist so
             // a new case management process can be created
             else if (uowName != null
                      && !isExistingProcessName(StringUtilities.deleteSpace(uowName))
                      && !isExistingUOWName(StringUtilities.deleteSpace(uowName))
                      && !StringUtilities.deleteSpace(uowName).equalsIgnoreCase("")){

                 // restore to default state
                 shapeMode = ShapeMode.NONE;
                 initDefault();

                  // delete extra white spaces in this uow name
                 uowName = StringUtilities.deleteSpace(uowName);

                 // create a case management process
                 // "Manage the flow of <UOWs>"
                 Process newCMP = new Process(cpX, cpY, 105, 45, "Manage the flow of ", uowName, ProcessType.CASE_MANAGEMENT_PROCESS);

                 arrayOfProcesses.add(newCMP); // add this new case management process into array of case processes

                 // user not selects any shape in the drawing area
                 notSelectAnyShape();

                 // user selects this new shape
                 processSelectIndex = arrayOfProcesses.size() - 1;

                 // create a new uow for this case management process
                 newUOW = new UOW(cpX, cpY, uowName);
                 newUOW.initDefault();
                 arrayOfUOWs.add(newUOW); //add this new UOW into array of UOWs

                 // create a new case process
                 Process newCP = new Process(cpX +130, cpY, 105, 45, "Handle a ", uowName, ProcessType.CASE_PROCESS);
                 arrayOfProcesses.add(newCP);

                 // add this new case management process into property controller
                 propertyController.addShape(arrayOfProcesses.get(processSelectIndex));

                 // also add the new uow into property controller
                 propertyController.addShape(newUOW);


                 // do not need to add the new case process into property controller
                 // as case process and case management process will fire the same event.
                 // keep just one process in the property controller
                 // propertyController.addShape(newCP);

                 propertyController.regProcessPropertyPanel();

                 // tell first cut property panel to display the selected case process shape's properties
                 displayProcess(processSelectIndex);

                 // the new case management process is red
                 arrayOfProcesses.get(processSelectIndex).setProcessColor(Color.red);

                 pDiagramPanel.notifyDisplayMessage("A new case management process that deals with <UOW> " +
                    arrayOfProcesses.get(processSelectIndex).getSuffixPName() + " has been created");

                 // the user has modified the diagram without saving
                 isLastSaved = false;
             }

             // user has entered a new UOW name and this name has already been in use
             // This implies that there must be a uow and a case process. There can be a case management process or not
             // However, If there is no case management process a new case management process can be created.
             else {
                Boolean isExistingCMP = false;
                for (Process process: arrayOfProcesses){
                    if (process.getSuffixPName().equalsIgnoreCase(StringUtilities.deleteSpace(uowName))
                            && process.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS){
                        isExistingCMP = true;
                    }
                }

                // delete extra white spaces in this uow name
                uowName = StringUtilities.deleteSpace(uowName);

                // user cannot create a new cmp as there is an existing case management process
                if (isExistingCMP){
                    int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)mouseEvent.getSource()),
                         "Would you like to reenter a new name?",
                         "This name has already been in use",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

                    // user wants to reenter a new name
                    if (userInput == JOptionPane.YES_OPTION){
                        shapeMode = ShapeMode.CASE_MANAGEMENT_PROCESS;
                    }

                    // user wants to cancel
                    else{
                        initDefault();
                        shapeMode = ShapeMode.NONE;

                        // user not selects any shape in the drawing area
                        notSelectAnyShape();
                    }
                }

                // user can create a new case management process. There is no need to create a new uow and case process
                else{

                    // restore to default state
                    shapeMode = ShapeMode.NONE;
                    initDefault();

                    // create a case management process
                    // "Manage the flow of <UOWs>"
                    Process newCMP = new Process(cpX, cpY, 105, 45, "Manage the flow of ", uowName, ProcessType.CASE_MANAGEMENT_PROCESS);
                    arrayOfProcesses.add(newCMP); // add this new case management process into array of case processes

                    // user not selects any shape in the drawing area
                    notSelectAnyShape();

                    // user selects this new shape
                    processSelectIndex = arrayOfProcesses.size() - 1;

                    // add this new case management process into property controller
                    propertyController.addShape(arrayOfProcesses.get(processSelectIndex));

                    // also add the uow and case process into property controller
                    for (UOW uow: arrayOfUOWs){
                        if (uow.getUOWName().equalsIgnoreCase(uowName)){
                            propertyController.addShape(uow);
                        }
                    }

                    for (Process process: arrayOfProcesses){
                        if (process.getSuffixPName().equalsIgnoreCase(uowName)
                            && process.getProcessType() == ProcessType.CASE_PROCESS){
                            propertyController.addShape(process);
                        }
                    }

                    propertyController.regProcessPropertyPanel();

                    // tell process property panel to display the selected case management process shape's properties
                    displayProcess(processSelectIndex);

                    // the new case management process is red
                    arrayOfProcesses.get(processSelectIndex).setProcessColor(Color.red);
                    
                    pDiagramPanel.notifyDisplayMessage("A new case management process that deals with <UOW> " +
                        arrayOfProcesses.get(processSelectIndex).getSuffixPName() + " has been created");

                    // the user has modified the diagram without saving
                    isLastSaved = false;
                }
             }
        }

        // 3. user wants to create a new process relationship
        // user has to click twice

        // 3.1 This is first click
        else if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP && firstClickedFlag == true){
            processRelX1Coordinate = mouseEvent.getX();
            processRelY1Coordinate = mouseEvent.getY();

            // the index of the process contains the x,y coordinate
            // the new relationship will go from this process
            firstProcessRelIndex = getProcessRectSelectedIndex(processRelX1Coordinate, processRelY1Coordinate,
                    processStartIndex);

            if (firstProcessRelIndex >= 0){

                // the index of the rectangles within the process
                firstProcessRectIndex = arrayOfProcesses.get(firstProcessRelIndex).
                        getProcessRectIndex(processRelX1Coordinate, processRelY1Coordinate);

                if (firstProcessRectIndex >= 0){
                    arrayOfProcesses.get(firstProcessRelIndex).setRectColor(
                            firstProcessRectIndex, Color.red);

                    processRelX1Coordinate =
                            arrayOfProcesses.get(firstProcessRelIndex).getXCoordinateOfPoint(firstProcessRectIndex);

                    processRelY1Coordinate =
                            arrayOfProcesses.get(firstProcessRelIndex).getYCoordinateOfPoint(firstProcessRectIndex);

                    // user clicks within a rectangle of a process
                    allowCreatedProcessRelFlag = true;
                }

                // user not clicks within a rectangle of a process
                else allowCreatedProcessRelFlag = false;
            }

            // user has used one click
            firstClickedFlag = false;
        }

        //3.2 This is SECOND CLICK (create a new process relationship
        else if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP && (firstClickedFlag == false)
                && (secondClickedFlag == true)){

            processRelX2Coordinate = mouseEvent.getX();
            processRelY2Coordinate = mouseEvent.getY();

            secondProcessRelIndex = getProcessRectSelectedIndex(
                    processRelX2Coordinate,
                    processRelY2Coordinate, processStartIndex);

            if(secondProcessRelIndex >= 0){
                secondProcessRectIndex = arrayOfProcesses.get(secondProcessRelIndex).
                        getProcessRectIndex(processRelX2Coordinate, processRelY2Coordinate);

                if (secondProcessRectIndex >= 0 && allowCreatedProcessRelFlag == true){

                    arrayOfProcesses.get(secondProcessRelIndex).setRectColor(
                            secondProcessRectIndex, Color.red);

                    processRelX2Coordinate =
                            arrayOfProcesses.get(secondProcessRelIndex).getXCoordinateOfPoint(secondProcessRectIndex);

                    processRelY2Coordinate =
                            arrayOfProcesses.get(secondProcessRelIndex).getYCoordinateOfPoint(secondProcessRectIndex);

                    // a new relationship between two processes
                    if (firstProcessRelIndex >= 0){

                        String fromProcessName = arrayOfProcesses.get(firstProcessRelIndex).getProcessFullName();
                        String toProcessName = arrayOfProcesses.get(secondProcessRelIndex).getProcessFullName();

                        int rectIndexFrom = firstProcessRectIndex;
                        int rectIndexTo = secondProcessRectIndex;

                        newProcessRel = new RPAGERelationship(processRelX1Coordinate,
                                processRelY1Coordinate, processRelX2Coordinate, processRelY2Coordinate,
                                fromProcessName, toProcessName, rectIndexFrom, rectIndexTo, "PROCESS");

                        System.out.println("Process full name is: " + arrayOfProcesses.get(firstProcessRelIndex).getProcessFullName());
                        arrayOfProcesses.get(firstProcessRelIndex).addProcessRel(newProcessRel);
                        arrayOfProcesses.get(secondProcessRelIndex).addProcessRel(newProcessRel);

                        // the user has modified the diagram without saving
                       isLastSaved = false;
                    }

                    allowCreatedProcessRelFlag = false;
                }
            }

            // user has finished drawing relationship so shapeMode = NONE;
            initDefault();
            shapeMode = ShapeMode.NONE;

            // user not select any shape in the drawing area
            notSelectAnyShape();

            secondClickedFlag = false;
        }// end else if (user wants to create a new process relationship SECOND CLICK)


        // 4. user wants to create a new process relationship name
        else if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP_NAME){
             mousePressedX = mouseEvent.getX();
             mousePressedY = mouseEvent.getY();

             String processRelName = JOptionPane.showInputDialog(
                      ((JPanel)mouseEvent.getSource()),
                      "Please type in process relationship name: ");

             // user has not enter any name
             if (processRelName == null || StringUtilities.deleteSpace(processRelName).equalsIgnoreCase("")){
                 shapeMode =ShapeMode.NONE;
                 initDefault();
             }

             // user has entered the relationship name
             // There is no mouse released event after user has created a new process relationship name
             else{
                 newRPAGELabel = new RPAGELabel(mousePressedX, mousePressedY);
                 newRPAGELabel.setRPAGELabelName(processRelName);
                 newRPAGELabel.setZoomFactor(processZoomFactor);

                 arrayOfProcessRelNames.add(newRPAGELabel);

                 initDefault();
                 shapeMode = ShapeMode.NONE;

                 // user not selects any shape in the drawing area
                 notSelectAnyShape();

                 // user selects this new process relationship name
                 processRelNameIndex = arrayOfProcessRelNames.size() - 1;

                 propertyController.addShape(arrayOfProcessRelNames.get(processRelNameIndex));
                 propertyController.regProcessPropertyPanel();
                 displayProcessRelName(processRelNameIndex);

                 // change process relationship name to RED Color
                 arrayOfProcessRelNames.get(processRelNameIndex).setColor(Color.red);

                 pDiagramPanel.notifyDisplayMessage("A new relationship name has been created");

                 // the user has modified the diagram without saving
                 isLastSaved = false;
             }
        }

         // 5. user click on the drawing panel without having chosen the shape mode
         else if (this.shapeMode == ShapeMode.NONE){
             mousePressedX = mouseEvent.getX();
             mousePressedY = mouseEvent.getY();

             // find what shape the mouse is pointing at
             processSelectIndex = getProcessSelectedIndex(mousePressedX,mousePressedY,
                     processStartIndex); // 5.1 user wants to select an existing process

             processWithRelContainsXYIndex = getProcessRelSelected(mousePressedX,
                     mousePressedY); // 5.2 user wants to select an existing relationship

             processRelNameIndex = getProcessRelNameSelectedIndex(mousePressedX,
                     mousePressedY); // 5.3 user wants to select an existing process relationship name

             // can only handle each selected shape at a time. So if cpSelectIndex >= 0
            // processRelNameIndex = -1

            // 5.1 user selects an existing process
             if (processSelectIndex >= 0){

                 processStartIndex = (processSelectIndex + 1) % arrayOfProcesses.size();

                 processWithRelContainsXYIndex = -1;
                 processRelNameIndex = -1;

                 //x1Coordinate, y1Coordinate are used when user wants to drag this existing case process
                 x1Coordinate = mouseEvent.getX();
                 y1Coordinate = mouseEvent.getY();
             }

             // 5.2 user wants to select an existing process relationship
             if (processWithRelContainsXYIndex >= 0){

                // user is selecting a relationship.
                // User clicks the mouse to move the control point of this relationship
                // but as this control point is
                // near another relationship mouse clicked point to this nearby relationship.
                // Therefore, we should change processWithRelContainsXYIndex to -1
                if (selectedProcessRel != null){
                    processWithRelContainsXYIndex = -1;
                }

                else if (selectedProcessRel == null){
                    selectedProcessRel = arrayOfProcesses.get(
                        processWithRelContainsXYIndex).returnProcessRelContains(mousePressedX, mousePressedY);

                    initDefault();

                    selectedProcessRel.setIsDrawCircle(true);
                    selectedProcessRel.setRPAGERelColor(Color.red);

                    // default controller will handle changes to this existing process relationship
                    // therefore no need to register this uow relationship with property controller
                    // propertyController.addShape();

                    // tell process property panel to display the selected process relationship properties
                    pDiagramPanel.displayProcessRel(selectedProcessRel.getFromShapeName(),
                            selectedProcessRel.getToShapeName(),
                            selectedProcessRel.getRectIndexFrom(),
                            selectedProcessRel.getRectIndexTo());

                    if(selectedProcessRel.circleContains(mousePressedX, mousePressedY)){
                        isProcessRelControlPointModified = true;
                    }
                }

                processRelNameIndex = -1;
             }

             // user want to select the circle of the relationship
             // user has selected the relationship and then wants to select the circle
             if (processWithRelContainsXYIndex == -1 &&
                     selectedProcessRel != null){

                 if(selectedProcessRel.circleContains(mousePressedX, mousePressedY)){
                     isProcessRelControlPointModified = true;
                 }
                 // deselect the existing relationship
                 else {
                     selectedProcessRel.setIsDrawCircle(false);
                     selectedProcessRel.setRPAGERelColor(Color.black);
                     selectedProcessRel = null;
                 }
             }

             // user
             if ((processWithRelContainsXYIndex == -1) && (selectedProcessRel == null)){
                 isProcessRelControlPointModified = false;
                 initDefault();
             }

             // user wants to select an existing process relationship name
             if (processRelNameIndex >= 0){

                //x1Coordinate, y1Coordinate are used when user wants to drag this existing case process
                x1Coordinate = mouseEvent.getX();
                y1Coordinate = mouseEvent.getY();
             }

         }

         // repaint()
         ((JPanel)mouseEvent.getSource()).repaint();
    }

   /**
    * Handle mouse events in process drawing Panel - MOUSE CLICKED
    * @param mouseEvent
    */
    public void pDrawingMouseClicked(MouseEvent mouseEvent){

    }

    /**
    * Handle mouse events in process drawing Panel - MOUSE RELEASED
    * @param mouseEvent
    */
    public void pDrawingMouseReleased(MouseEvent mouseEvent){
        // 1. user clicks to select an existing process shape
        // When user creates a new case process, there is no mouse released event or mouse clicked event
        // as user pressed enter to type the case process name
        if (processSelectIndex >=0 && shapeMode != ShapeMode.PROCESS_RELATIONSHIP){

            // register this case process or case management process and property panel to display its properties
            propertyController.addShape(arrayOfProcesses.get(processSelectIndex));
            propertyController.regProcessPropertyPanel();

            // tell process property panel to display the selected case process's properties
            displayProcess(processSelectIndex);

            // exit
            shapeMode =ShapeMode.NONE;
            initDefault();

            // set the color of this selected case process to red
            arrayOfProcesses.get(processSelectIndex).setProcessColor(Color.red);

            pDiagramPanel.notifyDisplayMessage("Process that deals with <UOW> " +
                    arrayOfProcesses.get(processSelectIndex).getSuffixPName() + " is being selected");
        }

        // 2. user clicks to space
        // when user has finished creating a new uow relationship
        //
        else if (processSelectIndex == -1 && processRelNameIndex == -1 &&
                shapeMode == ShapeMode.NONE && selectedProcessRel == null) {
            initDefault();

            // user not selects any shape in the drawing area
            notSelectAnyShape();

            propertyController.removeShape();
            propertyController.removePropertyPanel();

            pDiagramPanel.notDisplayCProcess();
            pDiagramPanel.notDisplayCMProcess();
            pDiagramPanel.notDisplayProcessRelName();
            pDiagramPanel.notDisplayProcessRel();
        }

        // 3. user wants to select an existing process relationship name
        else if (processRelNameIndex >= 0){
            propertyController.addShape(arrayOfProcessRelNames.get(processRelNameIndex));
            propertyController.regProcessPropertyPanel();
            displayProcessRelName(processRelNameIndex);

            // exit
            shapeMode = ShapeMode.NONE;
            initDefault();

            // change process relationship name to RED color
            arrayOfProcessRelNames.get(processRelNameIndex).setColor(Color.red);

            pDiagramPanel.notifyDisplayMessage("A relationship name is being selected");
        }

        // repaint()
        ((JPanel)mouseEvent.getSource()).repaint();
    }// end mouse released

   /**
    * Handle mouse events in process drawing Panel - MOUSE DRAGGED
    * @param mouseEvent
    */
    public void pDrawingMouseDragged(MouseEvent mouseEvent){
        mouseDraggedX = mouseEvent.getX();
        mouseDraggedY = mouseEvent.getY();

        if (mouseDraggedX < 0){
            mouseDraggedX = 0;
        }

        if (mouseDraggedY < 0){
            mouseDraggedY = 0;
        }

        // 1. user wants to drag an existing case process
        if (shapeMode == ShapeMode.NONE && (processSelectIndex >= 0)){

            int xCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessXCoordinate();
            int yCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessYCoordinate();
            xCoordinate = xCoordinate + mouseDraggedX - x1Coordinate;
            yCoordinate = yCoordinate + mouseDraggedY - y1Coordinate;
            x1Coordinate = mouseDraggedX;
            y1Coordinate = mouseDraggedY;
            arrayOfProcesses.get(processSelectIndex).setProcessXCoordinate(xCoordinate);
            arrayOfProcesses.get(processSelectIndex).setProcessYCoordinate(yCoordinate);

            // the user has modified the diagram without saving
            isLastSaved = false;
        }

        // 2. user wants to drag the control point of process relationship
        else if (isProcessRelControlPointModified == true
                && selectedProcessRel != null){
             selectedProcessRel.setControlPoint(mouseEvent.getX(), mouseEvent.getY());
        }

        // 3. user wants to drag an existing process relationship name
        else if (shapeMode == ShapeMode.NONE && processRelNameIndex >= 0){

            int xCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).
                    getRPAGELabelXCoordinate();
            int yCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).
                    getRPAGELabelYCoordinate();

            xCoordinate = xCoordinate + mouseDraggedX - x1Coordinate;
            yCoordinate = yCoordinate + mouseDraggedY - y1Coordinate;
            x1Coordinate = mouseDraggedX;
            y1Coordinate = mouseDraggedY;
            //When change both x and y cannot move the UOW shape
            //defaultController.setUOWXYCoordinate(selectIndex,x,y)
            // it's ok to change x then y.
            arrayOfProcessRelNames.get(processRelNameIndex).
                    setRPAGELabelXCoordinate(xCoordinate);

            arrayOfProcessRelNames.get(processRelNameIndex).
                    setRPAGELabelYCoordinate(yCoordinate);

            // the user has modified the diagram without saving
            isLastSaved = false;
        }
         // repaint()
         ((JPanel)mouseEvent.getSource()).repaint();
    }

   /**
    * Handle mouse events in process drawing Panel - MouseMoved
    * @param mouseEvent
    */
    public void pDrawingMouseMoved(MouseEvent mouseEvent){
         // request focus so that key events can be handled
        ((JPanel)mouseEvent.getSource()).requestFocus();

        // create a temporary process relationship name when user moves the mouse
        if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP_NAME){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            String processRelName = "Process Relationship name (i.e. generates...)";
            pRelNameCreatedByMouseMove = new RPAGELabel(mouseMovedX,mouseMovedY,
                    processRelName);
            pRelNameCreatedByMouseMove.setColor(Color.red);
            pRelNameCreatedByMouseMove.setZoomFactor(processZoomFactor);

            // repaint()
            ((JPanel)mouseEvent.getSource()).repaint();
        }// end if

        // create a temporary case process when user moves the mouse
        else if (shapeMode == ShapeMode.CASE_PROCESS){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            processCreatedByMouseMove = new Process(mouseMovedX,mouseMovedY, "Handle a ", "uow", ProcessType.CASE_PROCESS);
            processCreatedByMouseMove.setProcessColor(Color.red);

            // repaint()
            expandProcessDrawingArea(mouseEvent, processCreatedByMouseMove);
           ((JPanel)mouseEvent.getSource()).repaint();
        }// end else if

        // create a temporary case management process when user moves the mouse
        else if (shapeMode == ShapeMode.CASE_MANAGEMENT_PROCESS){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            processCreatedByMouseMove = new Process(mouseMovedX,mouseMovedY, "Manage the flow of ", "uow", ProcessType.CASE_MANAGEMENT_PROCESS);
            processCreatedByMouseMove.setProcessColor(Color.red);

            // repaint()
            expandProcessDrawingArea(mouseEvent, processCreatedByMouseMove);
           ((JPanel)mouseEvent.getSource()).repaint();
        }// end else if

        // create a new process relationship when user moves the mouse - First click
        else if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP && firstClickedFlag == true){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            // is the mouse within the rectangle. If so change the mouse cursor
            processMouseMovedIndex = getProcessRectSelectedIndex(mouseMovedX, mouseMovedY, processStartIndex);

            if (processMouseMovedIndex >= 0){
                //change mouse cursor
                Cursor crossHairCursor = new Cursor (Cursor.CROSSHAIR_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
            }

            // the mouse not within the rectangle - default cursor
            else{
                Cursor normalCursor = new Cursor (Cursor.DEFAULT_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
            }
        }

        // create a new process relationship when user moves the mouse - Second click
        else if (shapeMode == ShapeMode.PROCESS_RELATIONSHIP && secondClickedFlag == true){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            processMouseMovedIndex = getProcessRectSelectedIndex(mouseMovedX, mouseMovedY, processStartIndex);

            if (processMouseMovedIndex >= 0){
                //change mouse cursor
                Cursor crossHairCursor = new Cursor (Cursor.CROSSHAIR_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
            }

            // the mouse not within the rectangle - default cursor
            else{
                Cursor normalCursor = new Cursor (Cursor.DEFAULT_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
            }

            processRelCreatedByMouseMove = new RPAGERelationship(
                    processRelX1Coordinate, processRelY1Coordinate,
                    mouseMovedX, mouseMovedY, "PROCESS");
            processRelCreatedByMouseMove.setRPAGERelColor(Color.red);

            // repaint()
            ((JPanel)mouseEvent.getSource()).repaint();
        }

        else if (shapeMode == ShapeMode.NONE && isProcessRelControlPointModified == true){
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                  ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
        }

        else if (shapeMode == ShapeMode.NONE && isProcessRelControlPointModified == false){
            mouseMovedX = mouseEvent.getX();
            mouseMovedY = mouseEvent.getY();

            if (getProcessRelSelected(mouseMovedX, mouseMovedY) >= 0){
                 Cursor crossHairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
                ((JPanel)mouseEvent.getSource()).setCursor(crossHairCursor);
            }

             else {
              Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
              ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
            }
        }

        else {
              Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
              ((JPanel)mouseEvent.getSource()).setCursor(normalCursor);
        }
    }


/******************************************************************************/
    // Handle key pressed events in Process Drawing Panel
/******************************************************************************/
   /**
    * Handle key events in process drawing Panel
    * @param keyEvent
    */
    public void keyPressedInPDrawing(KeyEvent keyEvent){
        int keyCode = keyEvent.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
        switch (keyCode){
            case KeyEvent.VK_ESCAPE:
                shapeMode = ShapeMode.NONE;
                initDefault();
                ((JPanel)keyEvent.getSource()).repaint();
                break;

            case KeyEvent.VK_DELETE:
                // 1. user wants to delete an existing process
                if (processSelectIndex >= 0){
                    // if this is a case process -> remove the case management process and uow
                    if (arrayOfProcesses.get(processSelectIndex).getProcessType() == ProcessType.CASE_PROCESS){
                        int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)keyEvent.getSource()),
                         "Would you like to delete this case process?",
                         "Deleting Case Process",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

                        // user confirms deleting
                        if (userInput == JOptionPane.YES_OPTION){
                            propertyController.removeShape();
                            propertyController.removePropertyPanel();

                            // Tells process property panel to remove all property values of the selected case process
                            pDiagramPanel.notDisplayCProcess();

                            // remove uow first
                            removeUOW(arrayOfProcesses.get(processSelectIndex).getSuffixPName());

                            // this method remove the case process and case management process
                            removeProcess(arrayOfProcesses.get(processSelectIndex).getSuffixPName());
                            processSelectIndex = -1;

                            // the user has modified the diagram without saving
                            isLastSaved = false;
                        }

                        // user wants to cancel deleting the case process
                        else{
                            initDefault();
                            shapeMode = ShapeMode.NONE;
                        }
                    }// end deleting a case process

                    // this is a case managment process. There is no need to delete the uow and case process
                    else{
                        int userInput = JOptionPane.showConfirmDialog(
                         ((JPanel)keyEvent.getSource()),
                         "Would you like to delete this case management process?",
                         "Deleting Case Management Process",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

                         // user confirms deleting
                        if (userInput == JOptionPane.YES_OPTION){
                            propertyController.removeShape();
                            propertyController.removePropertyPanel();

                            // Tells process property panel to remove all property values of the selected process
                            pDiagramPanel.notDisplayCProcess();

                            // this method remove only the selected case managment process
                            removeProcess(processSelectIndex);
                            processSelectIndex = -1;

                            // the user has modified the diagram without saving
                            isLastSaved = false;
                        }

                        // user wants to cancel deleting the case process
                        else{
                            initDefault();
                            shapeMode = ShapeMode.NONE;
                        }
                    }

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                  // 2. use wants to delete an existing process relationship
                else if (processWithRelContainsXYIndex >= 0
                        && selectedProcessRel != null){

                    String fromProcessName = selectedProcessRel.getFromShapeName();
                    String toProcessName = selectedProcessRel.getToShapeName();

                    // delete relationship in from process
                    getProcess(fromProcessName).removeRel(selectedProcessRel);

                    // delete relationship in to process
                    getProcess(toProcessName).removeRel(selectedProcessRel);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // 3. user wants to delete a process relationship name
                else if (processRelNameIndex >= 0){
                    arrayOfProcessRelNames.remove(processRelNameIndex);
                    processRelNameIndex = -1;

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

                 case KeyEvent.VK_LEFT:
                // use moves an existing process to the left
                if (processSelectIndex >=0){
                    int newPXCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessXCoordinate() - 1;
                    if (newPXCoordinate < 0)
                        newPXCoordinate = 0;
                    arrayOfProcesses.get(processSelectIndex).setProcessXCoordinate(newPXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing process relationship name to the left
                else if (processRelNameIndex >=0){
                    int newPRelNameXCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelXCoordinate() - 1;
                    if (newPRelNameXCoordinate < 0)
                        newPRelNameXCoordinate = 0;
                    arrayOfUOWRelNames.get(uowRelNameIndex).setRPAGELabelXCoordinate(newPRelNameXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_RIGHT:
                // use moves an existing process to the right
                if (processSelectIndex >=0){
                    arrayOfProcesses.get(processSelectIndex).setProcessXCoordinate(
                            (arrayOfProcesses.get(processSelectIndex).getProcessXCoordinate() + 1));

                    // the user has modified the diagram without saving
                       isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                // user moves an existing process relationship name to the right
                else if (processRelNameIndex >=0){
                    int newPRelNameXCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelXCoordinate() + 1;
                    if (newPRelNameXCoordinate < 0)
                        newPRelNameXCoordinate = 0;
                    arrayOfProcessRelNames.get(processRelNameIndex).setRPAGELabelXCoordinate(newPRelNameXCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;

            case KeyEvent.VK_UP:
                // use moves up existing process
                if (processSelectIndex >=0){
                    int newPYCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessYCoordinate() - 1;
                    if (newPYCoordinate < 0)
                        newPYCoordinate = 0;
                    arrayOfProcesses.get(processSelectIndex).setProcessYCoordinate(newPYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

             // user moves up an existing process relationship name
                else if (processRelNameIndex >=0){
                    int newPRelNameYCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelYCoordinate() - 1;
                    if (newPRelNameYCoordinate < 0)
                        newPRelNameYCoordinate = 0;
                    arrayOfProcessRelNames.get(processRelNameIndex).setRPAGELabelYCoordinate(newPRelNameYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }
                break;

            case KeyEvent.VK_DOWN:
                // use moves down existing process
                if (processSelectIndex >=0){
                    int newPYCoordinate = arrayOfProcesses.get(processSelectIndex).getProcessYCoordinate() + 1;
                    if (newPYCoordinate < 0)
                        newPYCoordinate = 0;
                    arrayOfProcesses.get(processSelectIndex).setProcessYCoordinate(newPYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

             // user moves up an existing process relationship name
                else if (processRelNameIndex >=0){
                    int newPRelNameYCoordinate = arrayOfProcessRelNames.get(processRelNameIndex).getRPAGELabelYCoordinate() + 1;
                    if (newPRelNameYCoordinate < 0)
                        newPRelNameYCoordinate = 0;
                    arrayOfProcessRelNames.get(processRelNameIndex).setRPAGELabelYCoordinate(newPRelNameYCoordinate);

                    // the user has modified the diagram without saving
                    isLastSaved = false;

                    //repaint()
                    ((JPanel)keyEvent.getSource()).repaint();
                }

                break;
        }
    }



/******************************************************************************/
    // Handle events in Process Property Panel
/******************************************************************************/
   /**
    * Handle events in process property panel
    * @param itemEvent
    */
    public void pGridItemStateChanged(ItemEvent itemEvent){
        if (((JCheckBox)itemEvent.getSource()).isSelected()){
            turnOnProcessGridFlag = true;
        }
        else turnOnProcessGridFlag = false;

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();

    }// end method

    /************************************************************************/
    // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Prefix Name - Action Performed
    * @param actionEvent
    */
    public void pPrefixNameComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pPrefixNameComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end pPrefixNameComboBoxAP


   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Name - Action Performed
    * @param itemEvent
    */
    public void pNameTextFieldAP(ActionEvent actionEvent){
        String uowName = ((JTextField)actionEvent.getSource()).getText();
        // delete extra white spaces in this uow name
        if (uowName != null){
            uowName = StringUtilities.deleteSpace(uowName);
        }

        if (processSelectIndex >= 0 && !uowName.equalsIgnoreCase("")){
            String oldUOWName = arrayOfProcesses.get(processSelectIndex).getSuffixPName();

            System.out.println("true: " + oldUOWName.equals(uowName));

            // user just wants to change from lower case to upper case
            if (oldUOWName.equalsIgnoreCase(uowName) && !oldUOWName.equals(uowName)){
                // change process name
                // delegate the processing of this event to propertyController
                propertyController.pNameTextFieldAP(actionEvent);

                // also change case process (or case management process) and uow name
                int cpOrCMPIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(processSelectIndex).getSuffixPName().
                             equalsIgnoreCase(arrayOfProcesses.get(i).getSuffixPName())
                             && i != processSelectIndex){
                         cpOrCMPIndex = i;
                    }
                 }

                // also change the name of the case process or case management process
                if (cpOrCMPIndex >= 0){
                    arrayOfProcesses.get(cpOrCMPIndex).setSuffixPName(uowName);
                }

                int uowIndex = -1;
                for (int i=0; i<arrayOfUOWs.size(); i++){
                    if (arrayOfUOWs.get(i).getUOWName().equalsIgnoreCase(oldUOWName)){
                        uowIndex = i;
                    }
                }

                if (uowIndex >= 0){
                    arrayOfUOWs.get(uowIndex).setUOWName(uowName);
                }

                pDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }

            // the name exist so that user cannot change uow name
            else if (!oldUOWName.equalsIgnoreCase(uowName) && isExistingUOWName(uowName)){
                pDiagramPanel.notifyDisplayMessage("\"" + uowName + "\"" + " has been used. Please choose another name");
            }

            else if (!uowName.equalsIgnoreCase("") && !oldUOWName.equals(uowName)){
                // change process name
                // delegate the processing of this event to propertyController
                propertyController.pNameTextFieldAP(actionEvent);

                // also change case process and uow name
                int cpOrCMPIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (oldUOWName.
                             equalsIgnoreCase(arrayOfProcesses.get(i).getSuffixPName())
                             && i != processSelectIndex){

                        cpOrCMPIndex = i;
                    }
                 }

                // also change the name of the case process or case management process
                if (cpOrCMPIndex >= 0){
                    arrayOfProcesses.get(cpOrCMPIndex).setSuffixPName(uowName);
                }

                int uowIndex = -1;
                for (int i=0; i<arrayOfUOWs.size(); i++){
                    if (arrayOfUOWs.get(i).getUOWName().equalsIgnoreCase(oldUOWName)){
                        uowIndex = i;
                    }
                }

                if (uowIndex >= 0){
                    arrayOfUOWs.get(uowIndex).setUOWName(uowName);
                }

                pDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }
            // the user has modified the diagram without saving
            isLastSaved = false;
        }// end if
        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Name - Focus Lost
    * @param itemEvent
    */
    public void pNameTextFieldFL(FocusEvent focusEvent){
        String uowName = ((JTextField)focusEvent.getSource()).getText();
        // delete extra white spaces in this uow name
        uowName = StringUtilities.deleteSpace(uowName);

        if (processSelectIndex >= 0 && !uowName.equalsIgnoreCase("")){
            String oldUOWName = arrayOfProcesses.get(processSelectIndex).getSuffixPName();

            // user just wants to change from lower case to upper case
            if (oldUOWName.equalsIgnoreCase(uowName) && !oldUOWName.equals(uowName)){
                // change process name
                // delegate the processing of this event to propertyController
                propertyController.pNameTextFieldFL(focusEvent);

                // also change case process and uow name
                int cpOrCMPIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (oldUOWName.
                             equalsIgnoreCase(arrayOfProcesses.get(i).getSuffixPName())
                             && i != processSelectIndex){
                         cpOrCMPIndex = i;
                    }
                 }

                // also change the name of the case process or case management process
                if (cpOrCMPIndex >= 0){
                    arrayOfProcesses.get(cpOrCMPIndex).setSuffixPName(uowName);
                }

                int uowIndex = -1;
                for (int i=0; i<arrayOfUOWs.size(); i++){
                    if (arrayOfUOWs.get(i).getUOWName().equalsIgnoreCase(oldUOWName)){
                        uowIndex = i;
                    }
                }

                if (uowIndex >= 0){
                    arrayOfUOWs.get(uowIndex).setUOWName(uowName);
                }

                pDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }

            // the name exist so that user cannot change uow name
            else if (!oldUOWName.equalsIgnoreCase(uowName) && isExistingUOWName(uowName)){
                pDiagramPanel.notifyDisplayMessage("\"" + uowName + "\"" + " has been used. Please choose another name");
            }

            else if (!uowName.equalsIgnoreCase("") && !oldUOWName.equals(uowName)){
                // change process name
                // delegate the processing of this event to propertyController
                propertyController.pNameTextFieldFL(focusEvent);

                // also change case process and uow name
                int cpOrCMPIndex = -1;
                for (int i=0; i<arrayOfProcesses.size(); i++){
                    if (arrayOfProcesses.get(processSelectIndex).getSuffixPName().
                             equalsIgnoreCase(arrayOfProcesses.get(i).getSuffixPName())
                             && i != processSelectIndex){
                         cpOrCMPIndex = i;
                    }
                 }

                // also change the name of the case process or case management process
                if (cpOrCMPIndex >= 0){
                    arrayOfProcesses.get(cpOrCMPIndex).setSuffixPName(uowName);
                }

                int uowIndex = -1;
                for (int i=0; i<arrayOfUOWs.size(); i++){
                    if (arrayOfUOWs.get(i).getUOWName().equalsIgnoreCase(oldUOWName)){
                        uowIndex = i;
                    }
                }

                if (uowIndex >= 0){
                    arrayOfUOWs.get(uowIndex).setUOWName(uowName);
                }

                 pDiagramPanel.notifyDisplayMessage("old UOW name: " + "\"" + oldUOWName + "\"" + " --> " +
                        "new UOW name: " + "\"" + uowName + "\"");
            }
            // the user has modified the diagram without saving
            isLastSaved = false;
        }// end if
        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Management Process S or ES
    * @param actionEvent
    */
    public void pSOrEsComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pSOrEsComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Width - Action Performed
    * @param actionEvent
    */
    public void pWidthTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pWidthTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in First Cut Property Panel by delegating to property controller
    * Case Process Width - Focus Lost
    * @param focusEvent
    */
    public void pWidthTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pWidthTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Height - Action Performed
    * @param actionEvent
    */
    public void pHeightTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pHeightTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Height - Focus Lost
    * @param focusEvent
    */
    public void pHeightTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pHeightTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Arc Width - Action Performed
    * @param itemEvent
    */
    public void pArcWidthTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pArcWidthTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Arc Width - Focus Lost
    * @param focusEvent
    */
    public void pArcWidthTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pArcWidthTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Arc Height- A ction Performed
    * @param actionEvent
    */
    public void pArcHeightTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pArcHeightTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process Height - Focus Lost
    * @param focusEvent
    */
    public void pArcHeightTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pArcHeightTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Process Font Name
    * @param actionEvent
    */
    public void pFontNameComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pFontNameComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Process FontSize
    * @param actionEvent
    */
    public void pFontSizeComboBoxAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.pFontSizeComboBoxAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process XCoordinate - Action Performed
    * @param actionEvent
    */
    public void pXCoordinateTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
       propertyController.pXCoordinateTextFieldAP(actionEvent);

       // the user has modified the diagram without saving
       isLastSaved = false;

       //}
       pDrawingArea.repaint();
    }// end method

    /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process XCoordinate - Focus Lost
    * @param itemEvent
    */
    public void pXCoordinateTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pXCoordinateTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process YCoordinate - Action Performed
    * @param itemEvent
    */
    public void pYCoordinateTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
       propertyController.pYCoordinateTextFieldAP(actionEvent);

       // the user has modified the diagram without saving
       isLastSaved = false;

       //
       pDrawingArea.repaint();
    }// end method

   /**
    * Handle events in Process Property Panel by delegating to property controller
    * Case Process YCoordinate - Focus Lost
    * @param focusEvent
    */
    public void pYCoordinateTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.pYCoordinateTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }// end method


    /************************************************************************/
    // PROCESS RELATIONSHIP
   /**
    * Handle events in Process Relationship Property Panel
    * @param actionEvent
    */
    public void fromPRectComboBoxAP(ActionEvent actionEvent){
        if (selectedProcessRel != null){
            int fromRectIndex = (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString()));
            for (Process process: arrayOfProcesses){
                process.setRelFromRectIndex(selectedProcessRel, fromRectIndex);
            }
        }

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in Process Relationship Property Panel
    * @param actionEvent
    */
    public void toPRectComboBoxAP(ActionEvent actionEvent){
        if (selectedProcessRel != null){
            int toRectIndex = (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString()));
            for (Process process: arrayOfProcesses){
                process.setRelToRectIndex(selectedProcessRel, toRectIndex);
            }
        }

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }


    /************************************************************************/
    // PROCESS RELATIONSHIP NAME
   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * RPAGE Name - action performed
    * @param actionEvent
    */
    public void processRPAGENameTextFieldAP(ActionEvent actionEvent){
        // delegate the processing of this event to propertyController
        propertyController.processRPAGENameTextFieldAP(actionEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

   /**
    * Handle events in UOW Property Panel by delegating to property controller
    * RPAGE Name - focus lost
    * @param focusEvent
    */
    public void processRPAGENameTextFieldFL(FocusEvent focusEvent){
        // delegate the processing of this event to propertyController
        propertyController.processRPAGENameTextFieldFL(focusEvent);

        // the user has modified the diagram without saving
        isLastSaved = false;

        pDrawingArea.repaint();
    }

/******************************************************************************/
    // Handle events in User Interface
/******************************************************************************/
   /**
    * NEW event in UI
    * @param actionEvent
    */
    public void uiNewActionPerformed(ActionEvent actionEvent){
        int userInput = JOptionPane.showConfirmDialog(
                ui,
                "Would you like to save before creating new diagrams?",
                         "Creating new diagrams",
                         JOptionPane.YES_NO_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

        // user wants to save before creating new diagrams
        if (userInput == JOptionPane.YES_OPTION
            && uiSaveActionPerformed(actionEvent)){
            arrayOfUOWs.clear();
            arrayOfOSWSymbols.clear();
            arrayOfUOWRelNames.clear();
            arrayOfProcesses.clear();
            arrayOfProcessRelNames.clear();
            initDefault();
            notSelectAnyShape();
        }

        // user does not want to save
        else if (userInput == JOptionPane.NO_OPTION){
            arrayOfUOWs.clear();
            arrayOfOSWSymbols.clear();
            arrayOfUOWRelNames.clear();
            arrayOfProcesses.clear();
            arrayOfProcessRelNames.clear();
            initDefault();
            notSelectAnyShape();
        }
    }
    /**
     * EXIT event in UI
     * @param windowEvent
     */
    public boolean uiExitWindowClosing(WindowEvent windowEvent){
        if (arrayOfUOWs.size() == 0 && arrayOfUOWRelNames.size() == 0
                && arrayOfOSWSymbols.size() == 0 && arrayOfProcesses.size() == 0
                && arrayOfProcessRelNames.size() == 0){
            return true;
        }

        if (isLastSaved == true){
            return true;
        }

        int userInput = JOptionPane.showConfirmDialog(
                ui,
                "Would you like to save before closing R-PAGE?",
                         "Exiting",
                         JOptionPane.YES_NO_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

        // user wants to save before exiting
        if (userInput == JOptionPane.YES_OPTION &&
            uiSaveActionPerformed()){
            return true;
        }

        // user does not want to save
        else if (userInput == JOptionPane.NO_OPTION){
            return true;
        }

        return false;
    }

   /**
    * EXIT event in UI
    * @param actionEvent
    */
    public boolean uiExitWindowClosing(ActionEvent actionEvent){
        if (arrayOfUOWs.size() == 0 && arrayOfUOWRelNames.size() == 0
                && arrayOfOSWSymbols.size() == 0 && arrayOfProcesses.size() == 0
                && arrayOfProcessRelNames.size() == 0){
            return true;
        }

        if (isLastSaved == true){
            return true;
        }

        int userInput = JOptionPane.showConfirmDialog(
                ui,
                "Would you like to save before closing R-PAGE?",
                         "Exiting",
                         JOptionPane.YES_NO_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE);

        // user wants to save before exiting
        if (userInput == JOptionPane.YES_OPTION &&
            uiSaveActionPerformed()){
            return true;
        }

        // user does not want to save
        else if (userInput == JOptionPane.NO_OPTION){
            return true;
        }

        return false;
    }
    /**
     * PRINT event in UI
     * @param actionEvent
     */
    public void uiPrintActionPerformed(ActionEvent actionEvent){
        // print uow drawing area
        if(ui.isUOWDrawingSelected()){
            // reset to zoom factor = 1
            UOW.setZoomFactor(1);
            OutSideWorld.setZoomFactor(1);
            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                uowRelName.setZoomFactor(1);
            }

            // to make the position of the relationships in the uow changed
            for (UOW uow: arrayOfUOWs){
                uow.setUOWSize(uow.getUOWSize());
            }

            for (OutSideWorld osw: arrayOfOSWSymbols){
                osw.setOSWSize(osw.getOSWSize());
            }


            PrintUtilities.printComponent(uowDrawingArea);

            UOW.setZoomFactor(uowZoomFactor);
            OutSideWorld.setZoomFactor(uowZoomFactor);
            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                uowRelName.setZoomFactor(uowZoomFactor);
            }

            // to make the position of the relationships in the uow changed
            for (UOW uow: arrayOfUOWs){
                uow.setUOWSize(uow.getUOWSize());
            }

            for (OutSideWorld osw: arrayOfOSWSymbols){
                osw.setOSWSize(osw.getOSWSize());
            }

        }

        else if(ui.isPDrawingSelected()){
            // reset to zoom factor = 1
            Process.setZoomFactor(1);

            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                processRelName.setZoomFactor(1);
            }

            // to make the position of the relationships in the uow changed
            for (Process process: arrayOfProcesses){
                process.setProcessHeight(process.getProcessHeight());
                process.setProcessWidth(process.getProcessWidth());
            }

            PrintUtilities.printComponent(pDrawingArea);

            // reset to processZoomFactor
            Process.setZoomFactor(processZoomFactor);

            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                processRelName.setZoomFactor(processZoomFactor);
            }

            // to make the position of the relationships in the process changed
            for (Process process: arrayOfProcesses){
                process.setProcessHeight(process.getProcessHeight());
                process.setProcessWidth(process.getProcessWidth());
            }

        }
    }

    public void uiPrintPreviewActionPerformed(ActionEvent actionEvent){

    }

   /*
    * Save event in UI
    * @param actionEvent
    */
    public boolean uiSaveActionPerformed(ActionEvent actionEvent){
        ObjectOutputStream objectOutputStream = null;

        // this is the first time user click save button
        if (filePath == null){

            // display the file chooser
            int result = ui.showSaveDialog(null);

            // If a file has been selected, save RPAGE diagrams to it
            if (result == JFileChooser.APPROVE_OPTION){
                File selectedFile = ui.getSelectedFile();

                // user selects to save to an existing file
                if (selectedFile.exists()){
                    // needs user's confirmation to override or cancel
                    int confirm = JOptionPane.showConfirmDialog(ui, "File " + selectedFile.getName() +
                                                " already exists. Overwrite?",
                                                "Saving...",
                                                JOptionPane.YES_NO_OPTION);

                    // user choose to override
                    if (confirm == JOptionPane.YES_OPTION){
                        try {
                            objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                            // reset to zoom factor = 1
                             for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                uowRelName.setZoomFactor(1);
                            }

                            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                processRelName.setZoomFactor(1);
                            }

                            // write uow model to the selected file
                            objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                            objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                            objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                            // write process model to the selected file
                            objectOutputStream.writeObject(arrayOfProcesses); // Process
                            objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                uowRelName.setZoomFactor(uowZoomFactor);
                            }

                            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                processRelName.setZoomFactor(processZoomFactor);
                            }

                            filePath = selectedFile.toString();

                            JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                            isLastSaved = true;
                            return true;

                        } catch (FileNotFoundException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } finally {
                            // close the ObjectOutput Stream
                            try{
                                if (objectOutputStream != null){
                                    objectOutputStream.flush();
                                    objectOutputStream.close();

                                    for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                        uowRelName.setZoomFactor(uowZoomFactor);
                                    }

                                    for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                        processRelName.setZoomFactor(processZoomFactor);
                                    }
                                }
                            } catch (IOException ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                                   "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }

                // user chooses to save to a new file.
                else{
                     try{
                         objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                        // reset to zoom factor = 1
                         for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                             uowRelName.setZoomFactor(1);
                         }

                         for (RPAGELabel processRelName: arrayOfProcessRelNames){
                             processRelName.setZoomFactor(1);
                         }

                         // write uow model to the selected file
                         objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                         objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                         objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                         // write process model to the selected file
                         objectOutputStream.writeObject(arrayOfProcesses); // Process
                         objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                         for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                             uowRelName.setZoomFactor(uowZoomFactor);
                         }

                         for (RPAGELabel processRelName: arrayOfProcessRelNames){
                             processRelName.setZoomFactor(processZoomFactor);
                         }

                         filePath = selectedFile.toString();
                         JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                         isLastSaved = true;
                         return true;

                        }catch (FileNotFoundException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        }catch (IOException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        }finally {
                            // close the ObjectOutput Stream
                            try{
                                if (objectOutputStream != null){
                                    objectOutputStream.flush();
                                    objectOutputStream.close();
                                }
                            }catch (IOException ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                                     "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
            }
        }

        // filePath != null. Just save to the existing RPAGE file
        else{
            try {
                File selectedFile = new File(filePath);

                objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                // reset to zoom factor = 1
                for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                    uowRelName.setZoomFactor(1);
                }

                for (RPAGELabel processRelName: arrayOfProcessRelNames){
                    processRelName.setZoomFactor(1);
                }

                // write uow model to the selected file
                objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                // write process model to the selected file
                objectOutputStream.writeObject(arrayOfProcesses); // Process
                objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                    uowRelName.setZoomFactor(uowZoomFactor);
                }

                for (RPAGELabel processRelName: arrayOfProcessRelNames){
                    processRelName.setZoomFactor(processZoomFactor);
                }

                JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                isLastSaved = true;
                return true;
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                           "File Save Error", JOptionPane.INFORMATION_MESSAGE);
            }catch (IOException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                           "File Save Error", JOptionPane.INFORMATION_MESSAGE);
            }finally {
                // close the ObjectOutput Stream
                try{
                    if (objectOutputStream != null){
                        objectOutputStream.flush();
                        objectOutputStream.close();
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ui, ex.getMessage(),
                           "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        return false;
    }

   /*
    * Save
    */
    public boolean uiSaveActionPerformed(){
        ObjectOutputStream objectOutputStream = null;

        // this is the first time user click save button
        if (filePath == null){

            // display the file chooser
            int result = ui.showSaveDialog(null);

            // If a file has been selected, save RPAGE diagrams to it
            if (result == JFileChooser.APPROVE_OPTION){
                File selectedFile = ui.getSelectedFile();

                // user selects to save to an existing file
                if (selectedFile.exists()){
                    // needs user's confirmation to override or cancel
                    int confirm = JOptionPane.showConfirmDialog(ui, "File " + selectedFile.getName() +
                                                " already exists. Overwrite?",
                                                "Saving...",
                                                JOptionPane.YES_NO_OPTION);

                    // user choose to override
                    if (confirm == JOptionPane.YES_OPTION){
                        try {
                            objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                            // reset to zoom factor = 1
                             for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                uowRelName.setZoomFactor(1);
                            }

                            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                processRelName.setZoomFactor(1);
                            }

                            // write uow model to the selected file
                            objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                            objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                            objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                            // write process model to the selected file
                            objectOutputStream.writeObject(arrayOfProcesses); // Process
                            objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                uowRelName.setZoomFactor(uowZoomFactor);
                            }

                            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                processRelName.setZoomFactor(processZoomFactor);
                            }

                            filePath = selectedFile.toString();

                            JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                            isLastSaved = true;
                            return true;

                        } catch (FileNotFoundException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } finally {
                            // close the ObjectOutput Stream
                            try{
                                if (objectOutputStream != null){
                                    objectOutputStream.flush();
                                    objectOutputStream.close();

                                    for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                                        uowRelName.setZoomFactor(uowZoomFactor);
                                    }

                                    for (RPAGELabel processRelName: arrayOfProcessRelNames){
                                        processRelName.setZoomFactor(processZoomFactor);
                                    }
                                }
                            } catch (IOException ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                                    "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }

                // user chooses to save to a new file.
                else{
                     try{
                         objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                        // reset to zoom factor = 1
                         for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                             uowRelName.setZoomFactor(1);
                         }

                         for (RPAGELabel processRelName: arrayOfProcessRelNames){
                             processRelName.setZoomFactor(1);
                         }

                         // write uow model to the selected file
                         objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                         objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                         objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                         // write process model to the selected file
                         objectOutputStream.writeObject(arrayOfProcesses); // Process
                         objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                         for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                             uowRelName.setZoomFactor(uowZoomFactor);
                         }

                         for (RPAGELabel processRelName: arrayOfProcessRelNames){
                             processRelName.setZoomFactor(processZoomFactor);
                         }

                         filePath = selectedFile.toString();
                         JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                         isLastSaved = true;
                         return true;

                        }catch (FileNotFoundException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);

                        }catch (IOException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);

                        }finally {
                            // close the ObjectOutput Stream
                            try{
                                if (objectOutputStream != null){
                                    objectOutputStream.flush();
                                    objectOutputStream.close();
                                }
                            }catch (IOException ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                                   "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
            }
        }

        // filePath != null. Just save to the existing RPAGE file
        else{
            try {
                File selectedFile = new File(filePath);

                objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                // reset to zoom factor = 1
                for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                    uowRelName.setZoomFactor(1);
                }

                for (RPAGELabel processRelName: arrayOfProcessRelNames){
                    processRelName.setZoomFactor(1);
                }

                // write uow model to the selected file
                objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                // write process model to the selected file
                objectOutputStream.writeObject(arrayOfProcesses); // Process
                objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                    uowRelName.setZoomFactor(uowZoomFactor);
                }

                for (RPAGELabel processRelName: arrayOfProcessRelNames){
                    processRelName.setZoomFactor(processZoomFactor);
                }

                JOptionPane.showMessageDialog(ui,
                                                "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                isLastSaved = true;
                return true;
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
            }catch (IOException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
            }finally {
                // close the ObjectOutput Stream
                try{
                    if (objectOutputStream != null){
                        objectOutputStream.flush();
                        objectOutputStream.close();
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        return false;
    }

   /*
    * Save as event in UI
    */
    public void uiSaveAsActionPerformed(ActionEvent actionEvent){
        // display the file chooser
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Save As");
        int result = jFileChooser.showSaveDialog(null);
        ObjectOutputStream objectOutputStream = null;

        // A file has been selected
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = jFileChooser.getSelectedFile();
            System.out.println("Selected File is " + selectedFile.getName());

            // the file is already exist
            if (selectedFile.exists()){
                // create a confirm dialog
                int confirm = JOptionPane.showConfirmDialog(ui, "File " + selectedFile.getName() +
                                                " already exists. Overwrite?",
                                                "Saving...",
                                                JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION){
                    try {
                        objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                        // reset to zoom factor = 1
                        for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                            uowRelName.setZoomFactor(1);
                        }

                        for (RPAGELabel processRelName: arrayOfProcessRelNames){
                            processRelName.setZoomFactor(1);
                        }

                        // write uow model to the selected file
                        objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                        objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                        objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                        // write process model to the selected file
                        objectOutputStream.writeObject(arrayOfProcesses); // Process
                        objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                        for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                            uowRelName.setZoomFactor(uowZoomFactor);
                        }

                        for (RPAGELabel processRelName: arrayOfProcessRelNames){
                            processRelName.setZoomFactor(processZoomFactor);
                        }

                        filePath = selectedFile.toString();
                        JOptionPane.showMessageDialog(ui,
                                  "File " + selectedFile.getName() +
                                                " saved successfully!",
                                                "Saving...",
                                                JOptionPane.INFORMATION_MESSAGE);
                        isLastSaved = true;

                        } catch (FileNotFoundException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                        } finally {
                            // close the ObjectOutput Stream
                            try{
                                if (objectOutputStream != null){
                                    objectOutputStream.flush();
                                    objectOutputStream.close();
                                }
                            } catch (IOException ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ui, ex.getMessage(),
                                   "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                }
            }

           // user choose a file that does not exist. So just save to this file
           else{
                try{
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(selectedFile));

                    // reset to zoom factor = 1
                    for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                        uowRelName.setZoomFactor(1);
                    }

                    for (RPAGELabel processRelName: arrayOfProcessRelNames){
                        processRelName.setZoomFactor(1);
                    }

                    // write uow model to the selected file
                    objectOutputStream.writeObject(arrayOfUOWs); // UOWs
                    objectOutputStream.writeObject(arrayOfOSWSymbols); // Outside world symbols
                    objectOutputStream.writeObject(arrayOfUOWRelNames); // uow relationship names

                    // write process model to the selected file
                    objectOutputStream.writeObject(arrayOfProcesses); // Process
                    objectOutputStream.writeObject(arrayOfProcessRelNames); // process relationship names

                    for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                        uowRelName.setZoomFactor(uowZoomFactor);
                    }

                    for (RPAGELabel processRelName: arrayOfProcessRelNames){
                        processRelName.setZoomFactor(processZoomFactor);
                    }

                    filePath = selectedFile.toString();
                    isLastSaved = true;
                    JOptionPane.showMessageDialog(ui,
                           "File " + selectedFile.getName() +
                           " saved successfully!",
                           "Saving...",
                           JOptionPane.INFORMATION_MESSAGE);

                }catch (FileNotFoundException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);

                }catch (IOException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                }finally {
                    // close the ObjectOutput Stream
                    try{
                        if (objectOutputStream != null){
                            objectOutputStream.flush();
                            objectOutputStream.close();
                        }
                    }catch (IOException ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Save Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
           }
        }
        else System.out.println("No file selected");
    }

   /*
    * Open event in UI
    */
    public void uiOpenActionPerformed(ActionEvent actionEvent){

        // display the file chooser
        int result = ui.showOpenDialog(null);
        ObjectInputStream objectInputStream = null;

        // a file has been selected
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = ui.getSelectedFile();

            // the file is already exist
            if (selectedFile.exists()){
                 try {
                      objectInputStream = new ObjectInputStream(new FileInputStream(selectedFile));
                      ArrayList<UOW> temArrayOfUOWs = new ArrayList<UOW>();
                      temArrayOfUOWs = (ArrayList<UOW>)objectInputStream.readObject();

                      ArrayList<OutSideWorld> temArrayOfOSWSymbols = new ArrayList<OutSideWorld>();
                      temArrayOfOSWSymbols = (ArrayList<OutSideWorld>)objectInputStream.readObject();

                      ArrayList<RPAGELabel> temArrayOfUOWRelNames = new ArrayList<RPAGELabel>();
                      temArrayOfUOWRelNames = (ArrayList<RPAGELabel>)objectInputStream.readObject();

                      ArrayList<Process> temArrayOfProcesses = new ArrayList<Process>();
                      temArrayOfProcesses = (ArrayList<Process>)objectInputStream.readObject();

                      ArrayList<RPAGELabel> temArrayOfProcessRelNames = new ArrayList<RPAGELabel>();
                      temArrayOfProcessRelNames = (ArrayList<RPAGELabel>)objectInputStream.readObject();

                      filePath = selectedFile.toString();
                      JOptionPane.showMessageDialog(ui,
                                            "File " + selectedFile.getName() +
                                            " has been loaded successfully!",
                                            "Opening...",
                                            JOptionPane.INFORMATION_MESSAGE);
                      isLastSaved = true;

                      arrayOfUOWs = temArrayOfUOWs;
                      arrayOfOSWSymbols = temArrayOfOSWSymbols;
                      arrayOfUOWRelNames = temArrayOfUOWRelNames;

                      arrayOfProcesses = temArrayOfProcesses;
                      arrayOfProcessRelNames = temArrayOfProcessRelNames;

                      // to make the position of the relationships in the uow change
                      for (UOW uow: arrayOfUOWs){
                          uow.setUOWSize(uow.getUOWSize());
                      }

                      for (OutSideWorld osw: arrayOfOSWSymbols){
                          osw.setOSWSize(osw.getOSWSize());
                      }

                      // to make the position of the relationships in the process changed
                      for (Process process: arrayOfProcesses){
                          process.setProcessHeight(process.getProcessHeight());
                          process.setProcessWidth(process.getProcessWidth());
                      }

                      uowDrawingArea.repaint();
                      pDrawingArea.repaint();

                      initDefault();

                      // user not selects any shape in the drawing area
                      notSelectAnyShape();

                  }catch (FileNotFoundException ex){
                      ex.printStackTrace();
                      JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Open Error", JOptionPane.INFORMATION_MESSAGE);
                  }catch (IOException ex){
                      ex.printStackTrace();
                      JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Open Error", JOptionPane.INFORMATION_MESSAGE);
                  }catch (ClassNotFoundException ex){
                      ex.printStackTrace();
                      JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Open Error", JOptionPane.INFORMATION_MESSAGE);
                  }finally{
                      // close the ObjectOutput Stream
                      try{
                        if (objectInputStream != null){
                            objectInputStream.close();
                        }
                    }catch (IOException ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ui, ex.getMessage(),
                              "File Open Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                  }
            }

            else {
                System.out.println("not exist");
                JOptionPane.showMessageDialog(ui,
                        "File " + selectedFile.getName() +
                                            " does not exist.",
                                            "File Open Error",
                                            JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

   /**
    * Zoom in event in UI
    */
    public void uiZoomInButtonAP(ActionEvent actionEvent){
        // user is choosing the uow drawing area
        if (ui.isUOWDrawingSelected()){
            // maximum zoom factor is 2
            uowZoomFactor = (float) (uowZoomFactor + 0.25);
            if (uowZoomFactor >= 2){
                uowZoomFactor = 2;
            }

            String zoomPercent = ((Float)(uowZoomFactor * 100)).toString();
            ui.setZoomInToolTip("Click here to zoom in (" + zoomPercent +"%" +")");
            ui.setZoomOutToolTip("Click here to zoom out (" + zoomPercent +"%" +")");

            UOW.setZoomFactor(uowZoomFactor);
            OutSideWorld.setZoomFactor(uowZoomFactor);
            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                uowRelName.setZoomFactor(uowZoomFactor);
            }

            // to make the position of the relationships in the uow changed
            for (UOW uow: arrayOfUOWs){
                uow.setUOWSize(uow.getUOWSize());
            }

            for (OutSideWorld osw: arrayOfOSWSymbols){
                osw.setOSWSize(osw.getOSWSize());
            }

            uowDrawingArea.repaint();
        }

         // user is choosing the process drawing area
        else if (ui.isPDrawingSelected()){
            // maximum zoom factor is 2
            processZoomFactor = (float) (processZoomFactor + 0.25);
            if (processZoomFactor >= 2){
                processZoomFactor = 2;
            }

            String zoomPercent = ((Float)(processZoomFactor * 100)).toString();
            ui.setZoomInToolTip("Click here to zoom in (" + zoomPercent +"%" +")");
            ui.setZoomOutToolTip("Click here to zoom out (" + zoomPercent +"%" +")");

            Process.setZoomFactor(processZoomFactor);

            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                processRelName.setZoomFactor(processZoomFactor);
            }

            // to make the position of the relationships in the uow changed
            for (Process process: arrayOfProcesses){
                process.setProcessHeight(process.getProcessHeight());
                process.setProcessWidth(process.getProcessWidth());
            }

            // to make the position of the relationships in the process changed
            for (Process process: arrayOfProcesses){
                process.setProcessHeight(process.getProcessHeight());
                process.setProcessWidth(process.getProcessWidth());
            }

            pDrawingArea.repaint();
        }
    }

   /**
    * Zoom out event in UI
    */
    public void uiZoomOutButtonAP(ActionEvent actionEvent){
        // user is choosing the uow drawing area
        if (ui.isUOWDrawingSelected()){
            // minimum zoom factor is 0.25
            uowZoomFactor = (float) (uowZoomFactor - 0.25);
            if (uowZoomFactor <= 0.25){
                uowZoomFactor = (float)0.25;
            }

            String zoomPercent = ((Float)(uowZoomFactor * 100)).toString();
            ui.setZoomInToolTip("Click here to zoom in (" + zoomPercent +"%" +")");
            ui.setZoomOutToolTip("Click here to zoom out (" + zoomPercent +"%" +")");

            UOW.setZoomFactor(uowZoomFactor);
            OutSideWorld.setZoomFactor(uowZoomFactor);
            for (RPAGELabel uowRelName: arrayOfUOWRelNames){
                uowRelName.setZoomFactor(uowZoomFactor);
            }

            // to make the position of relationships in the uow changed
            for (UOW uow: arrayOfUOWs){
                uow.setUOWSize(uow.getUOWSize());
            }

            for (OutSideWorld osw: arrayOfOSWSymbols){
                osw.setOSWSize(osw.getOSWSize());
            }
            uowDrawingArea.repaint();
        }

        // user is choosing the process drawing area
        if (ui.isPDrawingSelected()){
            // minimum zoom factor is 0.25
            processZoomFactor = (float) (processZoomFactor - 0.25);
            if (processZoomFactor <= 0.25){
                processZoomFactor = (float)0.25;
            }

            String zoomPercent = ((Float)(processZoomFactor * 100)).toString();
            ui.setZoomInToolTip("Click here to zoom in (" + zoomPercent +"%" +")");
            ui.setZoomOutToolTip("Click here to zoom out (" + zoomPercent +"%" +")");

            Process.setZoomFactor(processZoomFactor);

            for (RPAGELabel processRelName: arrayOfProcessRelNames){
                processRelName.setZoomFactor(processZoomFactor);
            }

            // to make the position of the relationships in the uow changed
            for (Process process: arrayOfProcesses){
                process.setProcessHeight(process.getProcessHeight());
                process.setProcessWidth(process.getProcessWidth());
            }

            pDrawingArea.repaint();
        }
    }
    /**
     * User selects main working are help in UI
     */
    public void uiHelpMainWorkingAreaAP(){
        URL imageURL = getClass().getResource("/images/RPAGE_UI4Help.jpg");
        ImageIcon helpImage = new ImageIcon(imageURL);
        JOptionPane.showMessageDialog(null, helpImage, "Main Working Area in R-PAGE",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * User selects rpage tool bar help in UI
     */
    public void uiHelpRPAGEToolBarAP(){
        URL imageURL = getClass().getResource("/images/RPAGE_ToolBar4Help.jpg");
        ImageIcon helpImage = new ImageIcon(imageURL);
        JOptionPane.showMessageDialog(null, helpImage, "RPAGE Tool Bar Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

   /**
    * User selects rpage palette tool help in UI
    */
    public void uiHelpRPAGEPaletteToolAP(){
        URL imageURL = getClass().getResource("/images/RPAGE_Palette_Tool4Help.jpg");
        ImageIcon helpImage = new ImageIcon(imageURL);
        JOptionPane.showMessageDialog(null, helpImage, "RPAGE Palette Tool Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

   /**
    * User selects rpage diagrams help in UI
    */
    public void uiHelpRPAGEDiagramsAP(){
        URL imageURL = getClass().getResource("/images/RPAGE_Diagrams4Help.jpg");
        ImageIcon helpImage = new ImageIcon(imageURL);
        JOptionPane.showMessageDialog(null, helpImage, "RPAGE Diagrams Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

   /**
    * User selects rpage property panel help in UI
    */
    public void uiHelpRPAGEPropertyAP(){
        URL imageURL = getClass().getResource("/images/RPAGE_Property4Help.jpg");
        ImageIcon helpImage = new ImageIcon(imageURL);
        JOptionPane.showMessageDialog(null, helpImage, "RPAGE Property Panel Help",
                JOptionPane.INFORMATION_MESSAGE);
    }


   /**
    * User selects rpage about in UI
    */
    public void uiAboutAP(){
        JOptionPane.showMessageDialog(null, "Name: Riva Process Architecture Graphical Editor\n" +
                "Version: 1.0 \n" +
                "Date: November 2009\n" +
                "\n" +
                "Submitted in part fulfilment of the requirements\n" +
                "for the MSc degree in Software Engineering of UWE, Bristol",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }



   /**
    * User selects uow diagram in the UI
    */
    public void userChoosingUOWDiagram(){
        initDefault();
        shapeMode = ShapeMode.NONE;

        notSelectAnyShape();
        isUOWDrawingSelected = true;
        isProcessDrawingSelected = false;
    }

   /**
    * User selects process diagram in the UI
    */
    public void userChoosingPDiagram(){
        initDefault();
        shapeMode = ShapeMode.NONE;

        notSelectAnyShape();
        isUOWDrawingSelected = false;
        isProcessDrawingSelected = true;
    }
/******************************************************************************/

    public int getUOWSelectIndex(){
        return uowSelectIndex;
    }// end method

    // construct UOW grid
    public void constructUOWGrid(Graphics2D g2D, int width, int height){
        if (turnOnUOWGridFlag){
            System.out.println("construct grid uow diagram");
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(10, 10);
            g2D.setPaint(Color.lightGray);
            GeneralPath path = new GeneralPath();

            for (int i = 0; i <=height/10; i++){
                path.moveTo(0, i);
                path.lineTo(width/10, i);
            }

            for (int i = 0; i <=width/10; i++){
                path.moveTo(i, 0);
                path.lineTo(i, height/10);
            }
            g2D.draw(affineTransform.createTransformedShape(path));
        }
    }

    // construct Process grid
    public void constructProcessGrid(Graphics2D g2D, int width, int height){
        if (turnOnProcessGridFlag){
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(10, 10);
            g2D.setPaint(Color.lightGray);
            GeneralPath path = new GeneralPath();

            for (int i = 0; i <=height/10; i++){
                path.moveTo(0, i);
                path.lineTo(width/10, i);
            }

            for (int i = 0; i <=width/10; i++){
                path.moveTo(i, 0);
                path.lineTo(i, height/10);
            }
            g2D.draw(affineTransform.createTransformedShape(path));
        }
    }


    //
    public void expandUOWDrawingArea(MouseEvent mouseEvent, UOW uow){
        Boolean changed = false;

        Dimension area = ((JPanel)mouseEvent.getSource()).getSize();

        if (uow != null){
            if (uow.getUOWXCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2 > area.getWidth()){
                area.width = (int)(uow.getUOWXCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2);
                changed=true;
            }

            if (uow.getUOWYCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2 > area.getHeight()){
                area.height = (int)(uow.getUOWYCoordinate() + uow.getUOWSize()* UOW.getZoomFactor() + 2);
                changed=true;
            }

            if (changed){
                //update uow drawing are because
                //the area taken up by the graphics has
                //gotten larger or smaller
                ((JPanel)mouseEvent.getSource()).setPreferredSize(area);

                //let the scroll pane know to update itself
                //and its scrollbars.
                ((JPanel)mouseEvent.getSource()).revalidate();
            }
        }
    }

    //
    public void expandUOWDrawingArea(MouseEvent mouseEvent, OutSideWorld oswSymbol){
        Boolean changed = false;

        Dimension area = ((JPanel)mouseEvent.getSource()).getSize();

        if (oswSymbol != null){
            if (oswSymbol.getXCoordinate() + oswSymbol.getBounds().getWidth() * uowZoomFactor > area.getWidth()){
                area.width = oswSymbol.getXCoordinate() +
                        (int)(oswSymbol.getBounds().getWidth() * uowZoomFactor) + 2;
                changed=true;
            }

            if (oswSymbol.getYCoordinate() + oswSymbol.getBounds().getHeight() * uowZoomFactor > area.getHeight()){
                area.height = oswSymbol.getYCoordinate() +
                        (int)(oswSymbol.getBounds().getHeight() * uowZoomFactor) + 2;
                changed=true;
            }

            if (changed){
                //update uow drawing are because
                //the area taken up by the graphics has
                //gotten larger or smaller
                ((JPanel)mouseEvent.getSource()).setPreferredSize(area);

                //let the scroll pane know to update itself
                //and its scrollbars.
                ((JPanel)mouseEvent.getSource()).revalidate();
            }
        }
    }

    //
    public Dimension calculateCurrentUOWDrawingArea (){
        Dimension temUOWArea = new Dimension();
        for (UOW uow: arrayOfUOWs) {
            if (temUOWArea.width < uow.getUOWXCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2){
                temUOWArea.width = (int)(uow.getUOWXCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2);
            }

            if (temUOWArea.height < uow.getUOWYCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2){
                temUOWArea.height = (int)(uow.getUOWYCoordinate() + uow.getUOWSize() * UOW.getZoomFactor() + 2);
            }
        }

        if (uowCreatedByMouseMove != null){
             if (temUOWArea.width < uowCreatedByMouseMove.getUOWXCoordinate() +
                uowCreatedByMouseMove.getUOWSize() * uowZoomFactor + 2){
                temUOWArea.width = (int)(uowCreatedByMouseMove.getUOWXCoordinate() +
                        uowCreatedByMouseMove.getUOWSize() * UOW.getZoomFactor() + 2);
            }

             if (temUOWArea.height < uowCreatedByMouseMove.getUOWYCoordinate() +
                uowCreatedByMouseMove.getUOWSize() * uowZoomFactor + 2){
                temUOWArea.height = (int)(uowCreatedByMouseMove.getUOWYCoordinate() +
                        uowCreatedByMouseMove.getUOWSize() * UOW.getZoomFactor() + 2);
            }
        }

        for (OutSideWorld oswSymbol: arrayOfOSWSymbols) {
            if (temUOWArea.width < oswSymbol.getXCoordinate() +
                    oswSymbol.getBounds().getWidth() * uowZoomFactor + 2){
                temUOWArea.width = oswSymbol.getXCoordinate() +
                        (int)(oswSymbol.getBounds().getWidth() * uowZoomFactor);
            }

            if (temUOWArea.height < oswSymbol.getYCoordinate() +
                    oswSymbol.getBounds().getHeight() * uowZoomFactor + 2){
                temUOWArea.height = oswSymbol.getYCoordinate() +
                        (int)(oswSymbol.getBounds().getHeight() * uowZoomFactor);
            }
        }

        if (oswCreatedByMouseMove != null){
             if (temUOWArea.width < oswCreatedByMouseMove.getXCoordinate() +
                oswCreatedByMouseMove.getBounds().getWidth() * uowZoomFactor + 2){
                temUOWArea.width = oswCreatedByMouseMove.getXCoordinate() +
                        (int)(oswCreatedByMouseMove.getBounds().getWidth() * uowZoomFactor + 2);
            }

             if (temUOWArea.height < oswCreatedByMouseMove.getYCoordinate() +
                oswCreatedByMouseMove.getBounds().getHeight() * uowZoomFactor + 2){
                temUOWArea.height = oswCreatedByMouseMove.getYCoordinate() +
                        (int)(oswCreatedByMouseMove.getBounds().getHeight() * uowZoomFactor + 2);
            }
        }

        for (RPAGELabel rpageLabel: arrayOfUOWRelNames) {
            if (temUOWArea.width < rpageLabel.getRPAGELabelXCoordinate() +
                    rpageLabel.getRectWidth() + 2){
                temUOWArea.width = rpageLabel.getRPAGELabelXCoordinate() +
                    rpageLabel.getRectWidth() + 2;
            }

            if (temUOWArea.height < rpageLabel.getRPAGELabelYCoordinate() +
                    rpageLabel.getRectHeight() + 2){
                temUOWArea.height = rpageLabel.getRPAGELabelYCoordinate() +
                    rpageLabel.getRectHeight() + 2;
            }
        }

        return temUOWArea;
    }

    public void expandProcessDrawingArea(MouseEvent mouseEvent, Process process){

        Boolean changed = false;

        Dimension area = ((JPanel)mouseEvent.getSource()).getSize();

        if (process != null){
            if (process.getProcessXCoordinate() + process.getProcessWidth() + 2 > area.getWidth()){
                area.width = process.getProcessXCoordinate() + process.getProcessWidth() + 2;
                changed=true;
            }

            if (process.getProcessYCoordinate() + process.getProcessHeight() + 2 > area.getHeight()){
                area.height = process.getProcessYCoordinate() + process.getProcessHeight() + 2;
                changed=true;
            }

            if (changed){
                //update uow drawing are because
                //the area taken up by the graphics has
                //gotten larger or smaller
                ((JPanel)mouseEvent.getSource()).setPreferredSize(area);

                //let the scroll pane know to update itself
                //and its scrollbars.
                ((JPanel)mouseEvent.getSource()).revalidate();
            }
        }
    }

      //
    public Dimension calculateCurrentProcessDrawingArea (){
        Dimension temProcessArea = new Dimension();
        for (Process process: arrayOfProcesses) {
            if (temProcessArea.width < process.getProcessXCoordinate() + process.getProcessWidth() + 2){
                temProcessArea.width = process.getProcessXCoordinate() + process.getProcessWidth() + 2;
            }

            if (temProcessArea.height < process.getProcessYCoordinate() + process.getProcessHeight() + 2){
                temProcessArea.height = process.getProcessYCoordinate() + process.getProcessHeight() + 2;
            }
        }

        if (processCreatedByMouseMove != null){
             if (temProcessArea.width < processCreatedByMouseMove.getProcessXCoordinate() +
                processCreatedByMouseMove.getProcessWidth() + 2){
                temProcessArea.width = processCreatedByMouseMove.getProcessXCoordinate() +
                        processCreatedByMouseMove.getProcessWidth() + 2;
            }

             if (temProcessArea.height < processCreatedByMouseMove.getProcessYCoordinate() +
                processCreatedByMouseMove.getProcessHeight() + 2){
                temProcessArea.height = processCreatedByMouseMove.getProcessYCoordinate() +
                        processCreatedByMouseMove.getProcessHeight() + 2;
            }
        }

        for (RPAGELabel rpageLabel: arrayOfProcessRelNames) {
            if (temProcessArea.width < rpageLabel.getRPAGELabelXCoordinate() +
                    rpageLabel.getRectWidth() + 2){
                temProcessArea.width = rpageLabel.getRPAGELabelXCoordinate() +
                    rpageLabel.getRectWidth() + 2;
            }

            if (temProcessArea.height < rpageLabel.getRPAGELabelYCoordinate() +
                    rpageLabel.getRectHeight() + 2){
                temProcessArea.height = rpageLabel.getRPAGELabelYCoordinate() +
                    rpageLabel.getRectHeight() + 2;
            }
        }

        return temProcessArea;
    }

    public float getUOWZoomFactor(){
        return uowZoomFactor;
    }

    public float getPZoomFactor(){
        return processZoomFactor;
    }
}

