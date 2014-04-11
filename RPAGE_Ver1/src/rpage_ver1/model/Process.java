/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;

import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import rpage_ver1.RPAGEUtilities.StringUtilities;
import rpage_ver1.controller.PropertyController;

/**
 *
 * @author ducluu84
 */

/**
 * Process class
 */
public class Process extends AbstractShape implements Serializable {

    // a process has 6 small rectangles
    private final int RECT_MAX_NUM = 10;
    private final int RECT_SIZE = 6;

    // The values have the type Object, so if the value is actually a primitive type,
    // it must be contained within a wrapper.
    // For example, an integer needs to be wrapped in an Integer object.
    private String prefixPName;

    // this the uow name
    // uows in case of case management process
    private String suffixPName;
    private String sOrEs;

    private Integer processXCoordinate;
    private Integer processYCoordinate;
    private Integer processWidth;
    private Integer processHeight;
    private Integer processArcWidth;
    private Integer processArcHeight;

    // font of case process name
    private String processFontName;
    private Integer processFontSize;

    // color of the case process
    private Color color = Color.black;

    //
    private Color[] arrayOfRectColors;
    private Rectangle[] arrayOfRects;
    private ArrayList <RPAGERelationship> arrayOfProcessRels = new ArrayList<RPAGERelationship>();

    private Boolean isRectDisplayed;

    private ProcessType processType;

    private static float zoomFactor = 1;
    private static int maxWidth = 300;
    private static int maxHeight = 300;

/******************************************************************************/
     //Constructor and initDefault
/******************************************************************************/
    /**
     * Default constructor
     */
    public Process (int processXCoordinate, int processYCoordinate, String prefixPName,
            String suffixPName, ProcessType processType){
        setProcessXCoordinate(processXCoordinate);
        setProcessYCoordinate(processYCoordinate);
        setPrefixPName(prefixPName);
        setSuffixPName(suffixPName);
        setProcessWidth(105);
        setProcessHeight(45);
        this.processType = processType;
        initDefault();
    }// end constructor

    public Process (int processXCoordinate, int processYCoordinate, int processWidth,
            int processHeight, String prefixPName, String suffixPName,ProcessType processType){
        setProcessXCoordinate(processXCoordinate);
        setProcessYCoordinate(processYCoordinate);
        setProcessWidth(processWidth);
        setProcessHeight(processHeight);
        setPrefixPName(prefixPName);
        setSuffixPName(suffixPName);
        this.processType = processType;
        initDefault();
    }

   /**
    *
    */
    public void initDefault() {
        // black is the default color of this case process
        sOrEs = "s";
        color = Color.black;
        processFontName = "Serif";
        processFontSize = 12;
        processArcWidth = 10;
        processArcHeight = 10;

        isRectDisplayed = false;

        arrayOfRects = new Rectangle[RECT_MAX_NUM];
        arrayOfRectColors = new Color[RECT_MAX_NUM];

        for (int i=0; i<RECT_MAX_NUM; i++){
            arrayOfRectColors[i]= Color.DARK_GRAY;
        }
    }

/******************************************************************************/
    //Accessor and mutator methods
/******************************************************************************/
    public static float getZoomFactor(){
        return zoomFactor;
    }

    public static void setZoomFactor(float newZoomFactor){
        zoomFactor = newZoomFactor;
    }

    public ProcessType getProcessType(){
        return processType;
    }

   /**
    *  Process Full Name
    */
    public String getProcessFullName(){
        if (this.processType == ProcessType.CASE_PROCESS){
            return (this.prefixPName + this.suffixPName);
        }

        return this.prefixPName + this.suffixPName + this.sOrEs;
    }

   /**
    * Process Name - PREFIX NAME
    */
    public String getPrefixPName() {
        return prefixPName;
    }

    public void setPrefixPName(String prefixPName) {
        String oldName = this.prefixPName;
        this.prefixPName = prefixPName;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (this.getProcessType() == ProcessType.CASE_PROCESS
               && processRel.getFromShapeName().equalsIgnoreCase(oldName + this.getSuffixPName())){
                processRel.setFromShapeName(this.getPrefixPName() + this.getSuffixPName());
            }

            else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getFromShapeName().equalsIgnoreCase(oldName + this.getSuffixPName()
               + this.getSOrEs())){
                processRel.setFromShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (this.getProcessType() == ProcessType.CASE_PROCESS
               && processRel.getToShapeName().equalsIgnoreCase(oldName + this.getSuffixPName())){
                processRel.setToShapeName(this.getPrefixPName() + this.getSuffixPName());
            }

            else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getToShapeName().equalsIgnoreCase(oldName + this.getSuffixPName()
               + this.getSOrEs())){
                processRel.setToShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.PREFIX_PROCESS_NAME_PROPERTY, oldName, prefixPName);
    }

   /**
    * Process Name - SUFFIX NAME
    */
    public String getSuffixPName() {
        return suffixPName;
    }

    public void setSuffixPName(String suffixPName) {
        String oldName = this.suffixPName;
        this.suffixPName = suffixPName;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (this.getProcessType() == ProcessType.CASE_PROCESS
               && processRel.getFromShapeName().equalsIgnoreCase(this.getPrefixPName() + oldName)){
                processRel.setFromShapeName(this.getPrefixPName() + this.getSuffixPName());
            }

            else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getFromShapeName().equalsIgnoreCase(this.getPrefixPName() + oldName
               + this.getSOrEs())){
                processRel.setFromShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (this.getProcessType() == ProcessType.CASE_PROCESS
               && processRel.getToShapeName().equalsIgnoreCase(this.getPrefixPName() + oldName)){
                processRel.setToShapeName(this.getPrefixPName() + this.getSuffixPName());
            }

            else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getToShapeName().equalsIgnoreCase(this.getPrefixPName() + oldName
               + this.getSOrEs())){
                processRel.setToShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.SUFFIX_PROCESS_NAME_PROPERTY, oldName, suffixPName);
    }

   /**
    * Process NAME - S or ES
    */
    public String getSOrEs(){
        return sOrEs;
    }

    public void setSOrEs(String sOrEs){
        String oldName = this.sOrEs;
        this.sOrEs = sOrEs;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getFromShapeName().equalsIgnoreCase(this.getPrefixPName() + this.getSuffixPName()
               + oldName)){
                processRel.setFromShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
               && processRel.getToShapeName().equalsIgnoreCase(this.getPrefixPName() + this.getSuffixPName()
               + oldName)){
                processRel.setToShapeName(this.getPrefixPName() + this.getSuffixPName() + this.getSOrEs());
            }
       }

        firePropertyChange(PropertyController.PROCESS_SORES_PROPERTY, oldName, sOrEs);
    }

   /**
    * Process WIDTH
    */
    public Integer getProcessWidth() {
        return processWidth;
    }

    public void setProcessWidth(Integer processWidth) {
        if (processWidth >= maxWidth){
            processWidth = maxWidth;
        }
        Integer oldWidth = this.processWidth;
        this.processWidth = processWidth;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelX1Coordinate(getXCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

             if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelX2Coordinate(getXCoordinateOfPoint(processRel.getRectIndexTo()));
            }

            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelY1Coordinate(getYCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

             if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelY2Coordinate(getYCoordinateOfPoint(processRel.getRectIndexTo()));
            }

        }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.PROCESS_WIDTH_PROPERTY, oldWidth, processWidth);
    }

   /**
    * Process HEIGHT
    */
    public Integer getProcessHeight() {
        return processHeight;
    }

    public void setProcessHeight(Integer processHeight) {
        if (processHeight >= maxHeight){
            processHeight = maxHeight;
        }
        Integer oldHeight = this.processHeight;
        this.processHeight = processHeight;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelX1Coordinate(getXCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

            if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelX2Coordinate(getXCoordinateOfPoint(processRel.getRectIndexTo()));
            }

            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelY1Coordinate(getYCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

             if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelY2Coordinate(getYCoordinateOfPoint(processRel.getRectIndexTo()));
            }
        }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.PROCESS_HEIGHT_PROPERTY, oldHeight, processHeight);
    }

   /**
    * Process ARC WIDTH
    */
    public Integer getProcessArcWidth() {
        return processArcWidth;
    }

    public void setProcessArcWidth(Integer processArcWidth) {

        Integer oldArcWidth = this.processArcWidth;
        this.processArcWidth = processArcWidth;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.PROCESS_ARC_WIDTH_PROPERTY, oldArcWidth, processArcWidth);
    }

   /**
    * Process ARC HEIGHT
    */
    public Integer getProcessArcHeight() {
        return processArcHeight;
    }

     public void setProcessArcHeight(Integer processArcHeight) {

        Integer oldArcHeight = this.processArcHeight;
        this.processArcHeight = processArcHeight;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.PROCESS_ARC_HEIGHT_PROPERTY, oldArcHeight, processArcHeight);
    }

   /**
    * Process FONT NAME
    */
    public String getProcessFontName(){
        return processFontName;
    }

    public void setProcessFontName(String processFontName){
        String oldFontName = this.processFontName;
        this.processFontName = processFontName;

        firePropertyChange(PropertyController.PROCESS_FONT_NAME_PROPERTY, oldFontName, processFontName);
    }

   /**
    * Process FONT SIZE
    */
    public Integer getProcessFontSize(){
        return processFontSize;
    }

    public void setProcessFontSize(Integer processFontSize){
        Integer oldFontSize = this.processFontSize;
        this.processFontSize = processFontSize;

        firePropertyChange(PropertyController.PROCESS_FONT_SIZE_PROPERTY, oldFontSize, processFontSize);
    }

   /**
    * Process XCoordinate
	*/
    public Integer getProcessXCoordinate() {
        return processXCoordinate;
    }// end getProcessXCoordinate method

    public void setProcessXCoordinate(Integer processXCoordinate) {
        if (processXCoordinate <= 0){
            processXCoordinate = 0;
        }
        Integer oldXCoordinate = this.processXCoordinate;
        this.processXCoordinate = processXCoordinate;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelX1Coordinate(getXCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

             if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelX2Coordinate(getXCoordinateOfPoint(processRel.getRectIndexTo()));
            }

        }

        firePropertyChange(PropertyController.PROCESS_XCOORDINATE_PROPERTY, oldXCoordinate, processXCoordinate);
    }// end setCPXCoordinate

   /**
    * Process YCoordinate
	*/
    public Integer getProcessYCoordinate() {
        return processYCoordinate;
    }// end getCPXCoordinate method

    public void setProcessYCoordinate(Integer processYCoordinate) {
        if (processYCoordinate <= 0){
            processYCoordinate = 0;
        }
        Integer oldYCoordinate = this.processYCoordinate;
        this.processYCoordinate = processYCoordinate;

        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (processRel.getRectIndexFrom() >=0 &&
                    processRel.getRectIndexFrom() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getFromShapeName())){
                processRel.setRPAGERelY1Coordinate(getYCoordinateOfPoint(processRel.getRectIndexFrom()));
            }

             if (processRel.getRectIndexTo() >=0 &&
                    processRel.getRectIndexTo() <=9 &&
                    this.getProcessFullName().equalsIgnoreCase(processRel.getToShapeName())){
                processRel.setRPAGERelY2Coordinate(getYCoordinateOfPoint(processRel.getRectIndexTo()));
            }

        }

        firePropertyChange(PropertyController.PROCESS_YCOORDINATE_PROPERTY, oldYCoordinate, processYCoordinate);
    }// end setCPXCoordinate


  /**
   * Return the x coordinate of point 0
   */
   public int getXCoordinateOfPoint0(){
       // point o
       return (int)(processXCoordinate + processWidth * zoomFactor);
   }// end method

  /**
   * Return the y coordinate of point 0
   */
   public int getYCoordinateOfPoint0(){
      // point 0
       return (int)(processYCoordinate + 3 * processHeight/4 * zoomFactor);
   }// end getYCoordinateOfPoint0 method

  /**
   * Return the x coordinate of point 1
   */
   public int getXCoordinateOfPoint1(){
       // point 1
       return (int)(processXCoordinate + processWidth * zoomFactor);
   }// end method

  /**
   * Return the y coordinate of point 1
   */
   public int getYCoordinateOfPoint1(){
      // point 1
       return (int)(processYCoordinate + processHeight/2 * zoomFactor);
   }// end getYCoordinateOfPoint1 method

     /**
   * Return the x coordinate of point 2
   */
   public int getXCoordinateOfPoint2(){
       // point 2
       return (int)(processXCoordinate + processWidth * zoomFactor);
   }// end method

  /**
   * Return the y coordinate of point 2
   */
   public int getYCoordinateOfPoint2(){
      // point 2
       return (int)(processYCoordinate + processHeight/4 * zoomFactor);
   }// end getYCoordinateOfPoint2 method


  /**
   * Return the x coordinate of point 3
   */
   public int getXCoordinateOfPoint3(){
      // point 3
      return (int)(processXCoordinate + processWidth*2/3 * zoomFactor);
   }// end getXCoordinateOfPoint3 method

  /**
   * Return the y coordinate of point 3
   */
   public int getYCoordinateOfPoint3(){
      // point 3
      return (int)(processYCoordinate);
   }// end getYCoordinateOfPoint3 method

   /**
   * Return the x coordinate of point 4
   */
   public int getXCoordinateOfPoint4(){
      // point 4
       return (int)(processXCoordinate + processWidth * 1/3 * zoomFactor);
   }// end getXCoordinateOfPoint4 method

  /**
   * Return the y coordinate of point 4
   */
   public int getYCoordinateOfPoint4(){
      // point 4
       return processYCoordinate;
   }// end getYCoordinateOfPoint3 method

  /**
   * Return the x coordinate of point 5
   */
   public int getXCoordinateOfPoint5(){
      // point 5
       return processXCoordinate;
   }// end getXCoordinateOfPoint5 method

  /**
   * Return the y coordinate of point 4
   */
   public int getYCoordinateOfPoint5(){
      // point 5
       return (int)(processYCoordinate + processHeight/4 * zoomFactor);
   }// end getYCoordinateOfPoint5 method

    /**
   * Return the x coordinate of point 6
   */
   public int getXCoordinateOfPoint6(){
      // point 6
       return processXCoordinate;
   }// end getXCoordinateOfPoint5 method

  /**
   * Return the y coordinate of point 6
   */
   public int getYCoordinateOfPoint6(){
      // point 6
       return (int)(processYCoordinate + processHeight/2 * zoomFactor);
   }// end getYCoordinateOfPoint5 method

  /**
   * Return the x coordinate of point 7
   */
   public int getXCoordinateOfPoint7(){
      // point 5
       return processXCoordinate;
   }// end getXCoordinateOfPoint5 method

  /**
   * Return the y coordinate of point 7
   */
   public int getYCoordinateOfPoint7(){
      // point 7
       return (int)(processYCoordinate + 3 * processHeight/4 * zoomFactor);
   }// end getYCoordinateOfPoint5 method


  /**
   * Return the x coordinate of point 8
   */
   public int getXCoordinateOfPoint8(){
      // point 8
      return (int)(processXCoordinate + processWidth * 1/3 * zoomFactor);
   }// end getXCoordinateOfPoint8 method

  /**
   * Return the y coordinate of point 8
   */
   public int getYCoordinateOfPoint8(){
      // point 8
      return (int)(processYCoordinate + processHeight * zoomFactor);
   }// end getYCoordinateOfPoint6 method

  /**
   * Return the x coordinate of point 9
   */
   public int getXCoordinateOfPoint9(){
      // point 9
      return (int)(processXCoordinate + processWidth*2/3 * zoomFactor);
   }// end method

  /**
   * Return the y coordinate of point 9
   */
   public int getYCoordinateOfPoint9(){
      // point 9
      return (int)(processYCoordinate + processHeight * zoomFactor);
   }// end method

  /**
   * Return the x coordinate of any point
   */
   public int getXCoordinateOfPoint(int pointIndex){
       // point
       if (pointIndex == 0){
           return getXCoordinateOfPoint0();
       }
       else if (pointIndex == 1){
           return getXCoordinateOfPoint1();
       }
       else if (pointIndex == 2){
           return getXCoordinateOfPoint2();
       }
       else if (pointIndex == 3){
           return getXCoordinateOfPoint3();
       }
       else if (pointIndex == 4){
           return getXCoordinateOfPoint4();
       }
       else if (pointIndex == 5){
           return getXCoordinateOfPoint5();
       }

       else if (pointIndex == 6){
           return getXCoordinateOfPoint6();
       }
       else if (pointIndex == 7){
           return getXCoordinateOfPoint7();
       }

       else if (pointIndex == 8){
           return getXCoordinateOfPoint8();
       }

       else if (pointIndex == 9){
           return getXCoordinateOfPoint9();
       }

       return 0;
   }// end method

  /**
   * Return the y coordinate of any point
   */
   public int getYCoordinateOfPoint(int pointIndex){
       if (pointIndex == 0){
           return getYCoordinateOfPoint0();
       }
       else if (pointIndex == 1){
           return getYCoordinateOfPoint1();
       }
       else if (pointIndex == 2){
           return getYCoordinateOfPoint2();
       }
       else if (pointIndex == 3){
           return getYCoordinateOfPoint3();
       }
       else if (pointIndex == 4){
           return getYCoordinateOfPoint4();
       }
       else if (pointIndex == 5){
           return getYCoordinateOfPoint5();
       }
       else if (pointIndex == 6){
           return getYCoordinateOfPoint6();
       }
       else if (pointIndex == 7){
           return getYCoordinateOfPoint7();
       }
       else if (pointIndex == 8){
           return getYCoordinateOfPoint8();
       }
       else if (pointIndex == 9){
           return getYCoordinateOfPoint9();
       }

       return 0;
   }// end method

   /**
    * Process Color
    */
    public void setProcessColor(Color color){
        this.color = color;
    }

  /**
   * set color for the rectangle
   */
   public void setRectColor(int rectangleIndex, Color color){
           arrayOfRectColors[rectangleIndex] = color;
   }

   public void addProcessRel(RPAGERelationship processRel){
       arrayOfProcessRels.add(processRel);
   }

  /**
   * When user decides to delete an existing relationship, this method will be called
   * @param processRel
   */
   public void removeRel(RPAGERelationship processRel){
       if (processRel.getFromShapeName().equalsIgnoreCase(this.getProcessFullName())){
           processRel.setFromShapeName("DELETED");
       }

       else if (processRel.getToShapeName().equalsIgnoreCase(this.getProcessFullName())){
           processRel.setToShapeName("DELETED");
       }
       arrayOfProcessRels.remove(processRel);
   }

  /**
   * before a process is deleted, all of its relationship will be deleted
   */
   public void removeAllProcessRel(){
       
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (processRel.getFromShapeName().equalsIgnoreCase(this.getProcessFullName())){
               processRel.setFromShapeName("DELETED");
           }
       }

       for (RPAGERelationship processRel: arrayOfProcessRels){
             if (processRel.getToShapeName().equalsIgnoreCase(this.getProcessFullName())){
                 processRel.setToShapeName("DELETED");
             }
       }
       arrayOfProcessRels.clear();
   }

  /**
   * Removes all process relationships that fromUOWName is deleted or toUOWName is deleted
   */
   public void removeRel(){
       ArrayList<RPAGERelationship> temArrayOfProcessRels = new ArrayList<RPAGERelationship>();
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (!processRel.getFromShapeName().equals("DELETED") &&
               !processRel.getToShapeName().equals("DELETED")){
               temArrayOfProcessRels.add(processRel);
           }
        }
       arrayOfProcessRels.clear();
       arrayOfProcessRels = temArrayOfProcessRels;
   }

  /**
   * Return the index of rectangle that x, y belong to
   */
   public int getProcessRectIndex(int x, int y){
       for (int i = 0; i < arrayOfRects.length; i++) {
            if (arrayOfRects[i].contains(x, y)){
                return i;
            }
        }
        return -1;
   }// end method

   public void setDisplayRect(Boolean isRectDisplayed){
       this.isRectDisplayed = isRectDisplayed;
   }// end method
/******************************************************************************/
    //Other methods
/******************************************************************************/
    public void draw(Graphics g){
        g.setColor(color);
        g.drawRoundRect(processXCoordinate, processYCoordinate, (int)(processWidth * zoomFactor),
                (int)(processHeight * zoomFactor),
                (int)(processArcWidth * zoomFactor), (int)(processArcHeight * zoomFactor));

        createRects();
        if (isRectDisplayed){
            drawRects(g);
        }

        // draw process relationship
        RPAGERelationship.setZoomFactor(zoomFactor);
        for (RPAGERelationship processRel: arrayOfProcessRels){
            if (processRel.getFromShapeName().equalsIgnoreCase(this.getProcessFullName())){
                processRel.drawUOWRel(g);
            }
        }

        // create font for this process
        Font font = new Font(processFontName, Font.PLAIN, (int)(processFontSize * zoomFactor));

        if (StringUtilities.countWord(suffixPName) <= 3){
            // Get measures needed to draw the process name
            FontMetrics fontMetrics = g.getFontMetrics (font);

            // ascent of the font
            int ascent = fontMetrics.getAscent();

            // yCoordinate to draw the case management process or case process name in the round rectangle
            int stringY1Coordinate = (int)(processYCoordinate + processHeight/2 * zoomFactor - ascent/2);
            int stringY2Coordinate = (int)(processYCoordinate + processHeight/2 * zoomFactor + ascent/2);

            // this is a case process
            if (processType == ProcessType.CASE_PROCESS){
                g.setFont(font);
                g.drawString(prefixPName, processXCoordinate, stringY1Coordinate);
                g.drawString(suffixPName, processXCoordinate, stringY2Coordinate);
            }

            // this is a case management process
            else {
                g.setFont(font);
                g.drawString(prefixPName, processXCoordinate, stringY1Coordinate);

                int length = suffixPName.length();
                char last = suffixPName.charAt(length-1);

                // this case management process ends with yes
                if (last == 'y' && sOrEs.equals("es")){
                    // reverse the string
                    String newStringSuffixPName = new StringBuffer(suffixPName).reverse().toString();

                    // replace yes -> ies
                    newStringSuffixPName = newStringSuffixPName.replaceFirst("y", "i");
                    newStringSuffixPName = new StringBuffer(newStringSuffixPName).reverse().toString();
                    g.drawString(newStringSuffixPName + sOrEs, processXCoordinate, stringY2Coordinate);
                }

                else{
                    g.drawString(suffixPName + sOrEs, processXCoordinate, stringY2Coordinate);
                }
            }
        }

        else if (StringUtilities.countWord(suffixPName) > 3){
            String [] stringSuffixPName = new String[2];

            // string for line 1, 2
            stringSuffixPName[0] = "";
            stringSuffixPName[1] = "";

            StringTokenizer st = new StringTokenizer(suffixPName);

            // if we have 5 words: 1st line (3) 2nd line (2)
            // arrayNum[0] = 3; arrayNum[1] = 2
            int[] arrayNum = new int[2];

            for (int i = 0; i < StringUtilities.countWord(suffixPName); i++){
                arrayNum[i%2] = arrayNum[i%2] + 1;
            }

            // arrayNum[0] = 3; arrayNum[1] = 5; arrayNum[2] = 5
            arrayNum[1] = arrayNum[0] + arrayNum[1];

            int i = 1;
            while(st.hasMoreElements()){
                //System.out.println("next element outside if: " + st.nextElement());
                // these words are displayed on line 1
                if (i <= arrayNum[0]){
                      stringSuffixPName[0]  = stringSuffixPName[0] + st.nextElement() + " ";
                    //System.out.println("next element in line 1 is: " + st.nextElement());
                }

                // these words are displayed on line 2
                else if (i > arrayNum[0] && i <= arrayNum[1]){

                    stringSuffixPName[1] = stringSuffixPName[1] + st.nextElement() + " ";
                }

                i++;
            }

            // Get measures needed to draw the process name
            FontMetrics fontMetrics = g.getFontMetrics (font);

            // ascent of the font
            int ascent = fontMetrics.getAscent();

            // yCoordinate to draw the case management process or case process name in the round rectangle
            int stringY1Coordinate = (int)(processYCoordinate + processHeight/2 * zoomFactor - ascent/2);
            int stringY2Coordinate = (int)(processYCoordinate + processHeight/2 * zoomFactor + ascent/2);
            int stringY3Coordinate = (int)(processYCoordinate + processHeight/2 * zoomFactor + 1.5* ascent);

            // this is a case process
            if (processType == ProcessType.CASE_PROCESS){
                g.setFont(font);
                g.drawString(prefixPName, processXCoordinate, stringY1Coordinate);
                g.drawString(stringSuffixPName[0], processXCoordinate, stringY2Coordinate);
                g.drawString(stringSuffixPName[1], processXCoordinate, stringY3Coordinate);
            }

            // this is a case management process
            else {
                g.setFont(font);
                g.drawString(prefixPName, processXCoordinate, stringY1Coordinate);
                g.drawString(stringSuffixPName[0], processXCoordinate, stringY2Coordinate);
                stringSuffixPName[1] = stringSuffixPName[1].trim();

                int length = suffixPName.length();
                char last = suffixPName.charAt(length-1);

                // this case management process ends with yes
                if (last == 'y' && sOrEs.equals("es")){
                    // reverse the string
                    String newStringSuffixPName_1 = new StringBuffer(stringSuffixPName[1]).reverse().toString();

                    // replace yes -> ies
                    newStringSuffixPName_1 = newStringSuffixPName_1.replaceFirst("y", "i");
                    newStringSuffixPName_1 = new StringBuffer(newStringSuffixPName_1).reverse().toString();
                    g.drawString(newStringSuffixPName_1 + sOrEs, processXCoordinate, stringY3Coordinate);
                }

                else{
                    g.drawString(stringSuffixPName[1] + sOrEs, processXCoordinate, stringY3Coordinate);
                }
            }
        }
    }

    private void createRects(){
       Rectangle temRect;
       int x, y;

       temRect = new Rectangle(getXCoordinateOfPoint(0), getYCoordinateOfPoint(0),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[0] = temRect;
       temRect = new Rectangle(getXCoordinateOfPoint(1), getYCoordinateOfPoint(1),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[1] = temRect;
       temRect = new Rectangle(getXCoordinateOfPoint(2), getYCoordinateOfPoint(2),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[2] = temRect;


       temRect = new Rectangle(getXCoordinateOfPoint(3) , (int)(getYCoordinateOfPoint(3) - RECT_SIZE * zoomFactor),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[3] = temRect;
       temRect = new Rectangle(getXCoordinateOfPoint(4) , (int)(getYCoordinateOfPoint(4) - RECT_SIZE * zoomFactor),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[4] = temRect;


       temRect = new Rectangle((int)(getXCoordinateOfPoint(5) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(5),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[5] = temRect;
       temRect = new Rectangle((int)(getXCoordinateOfPoint(6) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(6),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[6] = temRect;
       temRect = new Rectangle((int)(getXCoordinateOfPoint(7) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(7),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[7] = temRect;

       temRect = new Rectangle(getXCoordinateOfPoint(8), getYCoordinateOfPoint(8),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[8] = temRect;
       temRect = new Rectangle(getXCoordinateOfPoint(9), getYCoordinateOfPoint(9),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[9] = temRect;
    }

   /**
    * Draw six small rectangle
    */
    private void drawRects(Graphics g){

        g.setColor(arrayOfRectColors[0]);
        g.drawRect(getXCoordinateOfPoint(0), getYCoordinateOfPoint(0),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[1]);
        g.drawRect(getXCoordinateOfPoint(1), getYCoordinateOfPoint(1),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[2]);
        g.drawRect(getXCoordinateOfPoint(2), getYCoordinateOfPoint(2),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));

        g.setColor(arrayOfRectColors[3]);
        g.drawRect(getXCoordinateOfPoint(3) , (int)(getYCoordinateOfPoint(3) - RECT_SIZE * zoomFactor) ,
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[4]);
        g.drawRect(getXCoordinateOfPoint(4) , (int) (getYCoordinateOfPoint(4) - RECT_SIZE * zoomFactor),
               (int)(RECT_SIZE * zoomFactor), (int) (RECT_SIZE * zoomFactor));

        g.setColor(arrayOfRectColors[5]);
        g.drawRect((int)(getXCoordinateOfPoint(5) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(5),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[6]);
        g.drawRect((int)(getXCoordinateOfPoint(6) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(6),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[7]);
        g.drawRect((int)(getXCoordinateOfPoint(7) - RECT_SIZE * zoomFactor), getYCoordinateOfPoint(7),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));

        g.setColor(arrayOfRectColors[8]);
        g.drawRect(getXCoordinateOfPoint(8), getYCoordinateOfPoint(8),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
        g.setColor(arrayOfRectColors[9]);
        g.drawRect(getXCoordinateOfPoint(9), getYCoordinateOfPoint(9),
               (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
    }// end method

   /**
    * Check if x,y belongs to any rectangle and make this rectangle red
    */
    public boolean isWithinRects(int x, int y){
        for (int i=0; i < RECT_MAX_NUM; i++) {
            if (arrayOfRects[i].contains(x,y)){
                //arrayOfRectColors[i] = Color.red;
                return arrayOfRects[i].contains(x,y);
            }
        }
        return false;
    }// end method

    public boolean isRelContains(int x, int y){
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (processRel.contains(x,y)){
               return true;
           }
       }
       return false;
   }

   public RPAGERelationship returnProcessRelContains(int x, int y){
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (processRel.contains(x,y)){
               return processRel;
           }
       }
       return null;
   }

   public RPAGERelationship findProcessRel(String fromUOWName, String toUOWName){
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (processRel.getFromShapeName().equals(fromUOWName) &&
               processRel.getToShapeName().equals(toUOWName)){
               return processRel;
           }
       }
       return null;
   }

   /**
    * Change the rectangle index
    */
   public void setRelFromRectIndex(RPAGERelationship selectedProcessRel, int fromRectIndex){
       for (RPAGERelationship processRel: arrayOfProcessRels){
           if (this.getProcessType() == ProcessType.CASE_PROCESS
                   && processRel.getFromShapeName().equals(selectedProcessRel.getFromShapeName())
                   && processRel.getToShapeName().equals(selectedProcessRel.getToShapeName())
                   && (this.getPrefixPName()+this.getSuffixPName()).equalsIgnoreCase(selectedProcessRel.getFromShapeName())
                   && processRel.equals(selectedProcessRel)){
               processRel.setRectIndexFrom(fromRectIndex);
               processRel.setRPAGERelX1Coordinate(getXCoordinateOfPoint(processRel.getRectIndexFrom()));
               processRel.setRPAGERelY1Coordinate(getYCoordinateOfPoint(processRel.getRectIndexFrom()));
           }

           else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
                   && processRel.getFromShapeName().equals(selectedProcessRel.getFromShapeName())
                   && processRel.getToShapeName().equals(selectedProcessRel.getToShapeName())
                   && (this.getPrefixPName()+this.getSuffixPName() + this.getSOrEs()).equalsIgnoreCase(selectedProcessRel.getFromShapeName())
                   && processRel.equals(selectedProcessRel)){
               processRel.setRectIndexFrom(fromRectIndex);
               processRel.setRPAGERelX1Coordinate(getXCoordinateOfPoint(processRel.getRectIndexFrom()));
               processRel.setRPAGERelY1Coordinate(getYCoordinateOfPoint(processRel.getRectIndexFrom()));
           }
       }
   }

   public void setRelToRectIndex(RPAGERelationship selectedProcessRel, int toRectIndex){
         for (RPAGERelationship processRel: arrayOfProcessRels){
             if (this.getProcessType() == ProcessType.CASE_PROCESS
                   && processRel.getFromShapeName().equals(selectedProcessRel.getFromShapeName())
                   && processRel.getToShapeName().equals(selectedProcessRel.getToShapeName())
                   && (this.getPrefixPName()+this.getSuffixPName()).equalsIgnoreCase(selectedProcessRel.getToShapeName())
                   && processRel.equals(selectedProcessRel)){
                 processRel.setRectIndexTo(toRectIndex);
                 processRel.setRPAGERelX2Coordinate(getXCoordinateOfPoint(processRel.getRectIndexTo()));
                 processRel.setRPAGERelY2Coordinate(getYCoordinateOfPoint(processRel.getRectIndexTo()));
             }

             else if (this.getProcessType() == ProcessType.CASE_MANAGEMENT_PROCESS
                   && processRel.getFromShapeName().equals(selectedProcessRel.getFromShapeName())
                   && processRel.getToShapeName().equals(selectedProcessRel.getToShapeName())
                   && (this.getPrefixPName()+this.getSuffixPName() + this.getSOrEs()).equalsIgnoreCase(selectedProcessRel.getToShapeName())
                   && processRel.equals(selectedProcessRel)){
                 processRel.setRectIndexTo(toRectIndex);
                 processRel.setRPAGERelX2Coordinate(getXCoordinateOfPoint(processRel.getRectIndexTo()));
                 processRel.setRPAGERelY2Coordinate(getYCoordinateOfPoint(processRel.getRectIndexTo()));
             }
         }
   }

    public boolean contains(int xCoordinate, int yCoordinate){
        RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(
                processXCoordinate, processYCoordinate, processWidth * zoomFactor, processHeight * zoomFactor,
                processArcWidth * zoomFactor, processArcHeight * zoomFactor);
        if (roundRect.contains(xCoordinate, yCoordinate)){
            return true;
        }
        return false;
    }

   /**
    * Checks if there is an existing relationship between fromProcessName and toProcessName
    */
    public boolean isExistingRelationship(String fromProcessName, String toProcessName){
        for (RPAGERelationship processRel: arrayOfProcessRels){
             if (processRel.getFromShapeName().equalsIgnoreCase(fromProcessName)
                   && processRel.getToShapeName().equalsIgnoreCase(toProcessName)){
                 return true;
             }
        }
        return false;
    }
}