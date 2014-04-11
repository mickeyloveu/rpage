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
public class UOW extends AbstractShape implements ReadOnlyUOW, Serializable{

    // a UOW has 6 small rectangles
    private final int RECT_MAX_NUM = 6;
    private final int RECT_SIZE = 6;

    private String uowName;
    private Integer uowSize = 100;

    // font of uow name
    private String uowFontName;
    private Integer uowFontSize;

    // color of the UOW
    private Color color;

    // The center of the hexagon
    private Integer uowXCoordinate;
    private Integer uowYCoordinate;

    //
    private Polygon polygon;

    //
    private Color[] arrayOfRectColors;
    private Rectangle[] arrayOfRects;
    private ArrayList <RPAGERelationship> arrayOfUOWRels = new ArrayList<RPAGERelationship>();;

    //
    private Boolean isRectDisplayed;

    private static float zoomFactor = 1;
    private static int maxSize = 400;

/******************************************************************************/
     //Constructor and initDefault
/******************************************************************************/
    /**
     * Default constructor
     */
    public UOW (int uowXCoordinate, int uowYCoordinate){
        setUOWXCoordinate(uowXCoordinate);
        setUOWYCoordinate(uowYCoordinate);
        setUOWName("");
        initDefault();
    }// end constructor


    public UOW (int uowXCoordinate, int uowYCoordinate, String uowName){
        setUOWXCoordinate(uowXCoordinate);
        setUOWYCoordinate(uowYCoordinate);
        setUOWName(uowName);
        initDefault();
    }// end constructor

       public UOW (int uowXCoordinate, int uowYCoordinate, int uowSize){
        setUOWXCoordinate(uowXCoordinate);
        setUOWYCoordinate(uowYCoordinate);
        setUOWSize(uowSize);
        initDefault();
    }

    /**
     *
     */
    public void initDefault() {
        uowFontName = "Serif";
        uowFontSize = 12;
        // black is the default color of this uow
        polygon = createPolygon();
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

    /**
     * UOW NAME
     */
    public String getUOWName() {
        return uowName;
    }
    /**
     * Change the name of this uow
     * @param uowName
     */
    public void setUOWName(String uowName) {
        String oldName = this.uowName;
        this.uowName = uowName;

        for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equals(oldName)){
               uowRel.setFromShapeName(this.uowName);
           }
        }
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getToShapeName().equals(oldName)){
               uowRel.setToShapeName(this.uowName);
           }
       }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.UOW_NAME_PROPERTY, oldName, uowName);
    }

   /**
	* UOW Size
    */
    public Integer getUOWSize() {
        return uowSize;
    }// end getUOWSize method

    public void setUOWSize(Integer uowSize) {

        if (uowSize >= maxSize){
            uowSize = maxSize;
        }
        Integer oldUOWSize = this.uowSize;
        this.uowSize = uowSize;

        for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getRectIndexFrom() >=0 &&
                        uowRel.getRectIndexFrom() <=5 &&
                        uowName.equals(uowRel.getFromShapeName())){
                            uowRel.setRPAGERelX1Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexFrom()));
            }

            if (uowRel.getRectIndexTo() >=0 &&
                        uowRel.getRectIndexTo() <=5 &&
                        uowName.equals(uowRel.getToShapeName())){
                            uowRel.setRPAGERelX2Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
        }

         for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getRectIndexFrom() >=0 &&
                        uowRel.getRectIndexFrom() <=5 &&
                        uowName.equals(uowRel.getFromShapeName())){

                uowRel.setRPAGERelY1Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexFrom()));

            }
            if (uowRel.getRectIndexTo() >=0 &&
                        uowRel.getRectIndexTo() <=5 &&
                        uowName.equals(uowRel.getToShapeName())){
                uowRel.setRPAGERelY2Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
        }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        firePropertyChange(PropertyController.UOW_SIZE_PROPERTY, oldUOWSize, uowSize);
    }// end setUOWSize method

   /**
    * UOW Font Name
    * @return
    */
    public String getUOWFontName(){
        return uowFontName;
    }

    public void setUOWFontName(String uowFontName){
        String oldFontName = this.uowFontName;
        this.uowFontName = uowFontName;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.UOW_FONT_NAME_PROPERTY, oldFontName, uowFontName);
    }

    /**
     * UOW Font Size
     */
    public Integer getUOWFontSize(){
        return uowFontSize;
    }

    public void setUOWFontSize(Integer uowFontSize){
        Integer oldFontSize = this.uowFontSize;
        this.uowFontSize = uowFontSize;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.UOW_FONT_SIZE_PROPERTY, oldFontSize, uowFontSize);
    }

   /**
    * UOW XCoordinate
	*/
    public Integer getUOWXCoordinate() {
        return uowXCoordinate;
    }// end getUOWXCoordinate method

    public void setUOWXCoordinate(Integer uowXCoordinate) {
        if (uowXCoordinate <= 0){
            uowXCoordinate = 0;
        }
        Integer oldXCoordinate = this.uowXCoordinate;
        this.uowXCoordinate = uowXCoordinate;
        for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getRectIndexFrom() >=0 &&
                        uowRel.getRectIndexFrom() <=5 &&
                        uowName.equals(uowRel.getFromShapeName())){
                            uowRel.setRPAGERelX1Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexFrom()));
            }

            if (uowRel.getRectIndexTo() >=0 &&
                        uowRel.getRectIndexTo() <=5 &&
                        uowName.equals(uowRel.getToShapeName())){
                            uowRel.setRPAGERelX2Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
        }
        firePropertyChange(PropertyController.UOW_XCOORDINATE_PROPERTY, oldXCoordinate, uowXCoordinate);
    }// end setUOWXCoordinate

   /**
    * UOW YCoordinate
	*/
    public Integer getUOWYCoordinate() {
        return uowYCoordinate;
    }// end getUOWYCoordinate

    public void setUOWYCoordinate(Integer uowYCoordinate) {
        if (uowYCoordinate <= 0){
            uowYCoordinate = 0;
        }

        Integer oldYCoordinate = this.uowYCoordinate;
        this.uowYCoordinate = uowYCoordinate;
         for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getRectIndexFrom() >=0 &&
                        uowRel.getRectIndexFrom() <=5 &&
                        uowName.equals(uowRel.getFromShapeName())){
                uowRel.setRPAGERelY1Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexFrom()));

            }
            if (uowRel.getRectIndexTo() >=0 &&
                        uowRel.getRectIndexTo() <=5 &&
                        uowName.equals(uowRel.getToShapeName())){
                uowRel.setRPAGERelY2Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
        }

        firePropertyChange(PropertyController.UOW_YCOORDINATE_PROPERTY, oldYCoordinate, uowYCoordinate);
    }// end  setUOWYCoordinate method

    /**
   * Return the x coordinate of point 0
   */
   public int getXCoordinateOfPoint0(){
      // point 0
       int radius = (int)(uowSize * 0.4 * zoomFactor);

       int x = uowXCoordinate + radius;
       return x;
   }// end getXCoordinateOfPoint0 method

  /**
   * Return the y coordinate of point 0
   */
   public int getYCoordinateOfPoint0(){
      // point 0
       int radius = (int)(uowSize * 0.4 * zoomFactor);

       // sclae uowYCoordinate to zoom factor
       int y = uowYCoordinate;
       return y;
   }// end getYCoordinateOfPoint0 method

  /**
   * Return the x coordinate of point 1
   */
   public int getXCoordinateOfPoint1(){
      // point 1
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int x = (int)(uowXCoordinate + radius * Math.cos(2 * Math.PI / 6));
       return x;
   }// end getXCoordinateOfPoint1 method

  /**
   * Return the y coordinate of point 1
   */
   public int getYCoordinateOfPoint1(){
      // point 2
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int y = (int)(uowYCoordinate - radius * Math.sin(2 * Math.PI / 6));
       return y;
   }// end getYCoordinateOfPoint1 method

   /**
   * Return the x coordinate of point 2
   */
   public int getXCoordinateOfPoint2(){
      // point 3
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int x = (int)(uowXCoordinate + radius *
                Math.cos(2 * 2 * Math.PI / 6));
       return x;
   }// end getXCoordinateOfPoint2 method

  /**
   * Return the y coordinate of point 2
   */
   public int getYCoordinateOfPoint2(){
      // point 2
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int y = (int)(uowYCoordinate - radius *
                Math.sin(2 * 2 * Math.PI / 6));
       return y;
   }// end getYCoordinateOfPoint2 method

  /**
   * Return the x coordinate of point 3
   */
   public int getXCoordinateOfPoint3(){
      // point 3
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int x = (int)(uowXCoordinate + radius *
         Math.cos(3 * 2 * Math.PI / 6));
       return x;
   }// end getXCoordinateOfPoint3 method

  /**
   * Return the y coordinate of point 3
   */
   public int getYCoordinateOfPoint3(){
      // point 3
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int y = (int)(uowYCoordinate - radius *
         Math.sin(3 * 2 * Math.PI / 6));
       return y;
   }// end getYCoordinateOfPoint3 method

  /**
   * Return the x coordinate of point 4
   */
   public int getXCoordinateOfPoint4(){
      // point 4
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int x = (int)(uowXCoordinate + radius *
         Math.cos(4 * 2 * Math.PI / 6));
       return (int)(x);
   }// end getXCoordinateOfPoint4 method

  /**
   * Return the y coordinate of point 4
   */
   public int getYCoordinateOfPoint4(){
      // point 4
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int y = (int)(uowYCoordinate - radius *
         Math.sin(4 * 2 * Math.PI / 6));
       return (int)(y);
   }// end getYCoordinateOfPoint4 method

  /**
   * Return the x coordinate of point 5
   */
   public int getXCoordinateOfPoint5(){
      // point 5
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int x = (int)(uowXCoordinate + radius *
         Math.cos(5 * 2 * Math.PI / 6));
       return x;
   }// end getXCoordinateOfPoint5 method

  /**
   * Return the y coordinate of point 5
   */
   public int getYCoordinateOfPoint5(){
      // point 5
       int radius = (int)(uowSize * 0.4 * zoomFactor);
       int y = (int)(uowYCoordinate - radius *
         Math.sin(5 * 2 * Math.PI / 6));
       return y;
   }// end getYCoordinateOfPoint5 method

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
       return 0;
   }// end method

  /**
   * UOW Color
   */
   public void setUOWColor(Color color){
        this.color = color;
   }

  /**
   * set color for the rectangle
   */
   public void setRectColor(int rectangleIndex, Color color){
           arrayOfRectColors[rectangleIndex] = color;
   }

   public void addUOWRel(RPAGERelationship uowRel){
       arrayOfUOWRels.add(uowRel);
   }

   public void removeRel(RPAGERelationship uowRel){
       if (uowRel.getFromShapeName().equals(this.uowName)){
               uowRel.setFromShapeName("DELETED");
           }

       else if (uowRel.getToShapeName().equals(this.uowName)){
               uowRel.setToShapeName("DELETED");
       }

       arrayOfUOWRels.remove(uowRel);
   }

   public void removeAllUOWRel(){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equals(this.uowName)){
               uowRel.setFromShapeName("DELETED");
           }
       }
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getToShapeName().equals(this.uowName)){
               uowRel.setToShapeName("DELETED");
           }
       }
       arrayOfUOWRels.clear();
   }

  /**
   * Removes all uow relationships that fromUOWName is deleted or toUOWName is deleted
   */
   public void removeRel(){
       ArrayList<RPAGERelationship> temArrayOfUOWRels = new ArrayList<RPAGERelationship>();
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (!uowRel.getFromShapeName().equals("DELETED") &&
               !uowRel.getToShapeName().equals("DELETED")){
               temArrayOfUOWRels.add(uowRel);
           }
        }
       arrayOfUOWRels.clear();
       arrayOfUOWRels = temArrayOfUOWRels;
   }

  /**
    * Return the index of rectangle that x, y belong to
   */
   public int getUOWRectIndex(int x, int y){
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
    public Polygon createPolygon(){
        // Create a Polygon object
       Polygon polygon = new Polygon();

       for (int i=0; i < RECT_MAX_NUM; i++){
           polygon.addPoint(getXCoordinateOfPoint(i), getYCoordinateOfPoint(i));
       }
       return polygon;
    }

    public void drawUOW(Graphics g){
        polygon = createPolygon();
        g.setColor(color);
        g.drawPolygon(polygon);
        createRects();
        if (isRectDisplayed){
            drawRects(g);
        }

        for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowName.equals(uowRel.getToShapeName())){
                uowRel.drawUOWRel(g);
            }
        }

        // create font for this UOW
        Font font = new Font(uowFontName, Font.PLAIN, (int)(uowFontSize * zoomFactor));

        // Get measures needed to center the message
        FontMetrics fontMetrics = g.getFontMetrics (font);


        if (StringUtilities.countWord(uowName) == 1){
             // How many pixels wide is the string
            int msg_width = fontMetrics.stringWidth (uowName);

            // x coordinate of uowName
            int stringXCoordinate = (int)(uowXCoordinate) - (msg_width/2);

            // int y coordinate of uowName
            int stringYCoordinate = (int)(uowYCoordinate) +3;

            // set this graphics context's font to the specified font
            g.setFont(font);

            // draw uow name
            g.drawString(uowName, stringXCoordinate, stringYCoordinate);
        }

        else if (StringUtilities.countWord(uowName) == 2){
            String [] stringUOWName = new String[2];
            StringTokenizer st = new StringTokenizer(uowName);
            int i = 0;
            while(st.hasMoreElements()){
                stringUOWName[i] = st.nextElement() + " ";
                i++;
            }

            // How many pixels wide is the string
            int msg_width1 = fontMetrics.stringWidth (stringUOWName[0]);
            int msg_width2 = fontMetrics.stringWidth (stringUOWName[1]);

            // ascent of the font
            int ascent = fontMetrics.getAscent();

            // x coordinate of uowName
            int stringX1Coordinate = (int)(uowXCoordinate) - (msg_width1/2);
            int stringX2Coordinate = (int)(uowXCoordinate) - (msg_width2/2);

            // int y coordinate of uowName
            int stringY1Coordinate = (int)(uowYCoordinate) - 3;
            int stringY2Coordinate = (int)(uowYCoordinate) - 3 + ascent;

            // set this graphics context's font to the specified font
            g.setFont(font);

            // draw uow name
            g.drawString(stringUOWName[0], stringX1Coordinate, stringY1Coordinate);
            g.drawString(stringUOWName[1], stringX2Coordinate, stringY2Coordinate);
        }

        else if (StringUtilities.countWord(uowName) == 3){
            String [] stringUOWName = new String[3];
            StringTokenizer st = new StringTokenizer(uowName);
            int i = 0;
            while(st.hasMoreElements()){
                stringUOWName[i] = st.nextElement() + " ";
                i++;
            }

            // How many pixels wide is the string
            int msg_width1 = fontMetrics.stringWidth (stringUOWName[0]);
            int msg_width2 = fontMetrics.stringWidth (stringUOWName[1]);
            int msg_width3 = fontMetrics.stringWidth (stringUOWName[2]);

            // ascent of the font
            int ascent = fontMetrics.getAscent();

            // x coordinate of uowName
            int stringX1Coordinate = (int)(uowXCoordinate) - (msg_width1/2);
            int stringX2Coordinate = (int)(uowXCoordinate) - (msg_width2/2);
            int stringX3Coordinate = (int)(uowXCoordinate) - (msg_width3/2);

            // int y coordinate of uowName
            int stringY1Coordinate = (int)(uowYCoordinate) - 4;
            int stringY2Coordinate = (int)(uowYCoordinate) - 4 + ascent;
            int stringY3Coordinate = (int)(uowYCoordinate) - 4 + 2*ascent;

            // set this graphics context's font to the specified font
            g.setFont(font);

            // draw uow name
            g.drawString(stringUOWName[0], stringX1Coordinate, stringY1Coordinate);
            g.drawString(stringUOWName[1], stringX2Coordinate, stringY2Coordinate);
            g.drawString(stringUOWName[2], stringX3Coordinate, stringY3Coordinate);
        }
        
        else if (StringUtilities.countWord(uowName) > 3){
            String [] stringUOWName = new String[3];
            
            // string for line 1, 2 and 3
            stringUOWName[0] = "";
            stringUOWName[1] = "";
            stringUOWName[2] = "";

            StringTokenizer st = new StringTokenizer(uowName);

            // if we have 5 words: 1st line (2) 2nd line (2) third line (1)
            // arrayNum[0] = 2; arrayNum[1] = 2; arrayNum[2] = 1
            int[] arrayNum = new int[3];

            for (int i = 0; i < StringUtilities.countWord(uowName); i++){
                arrayNum[i%3] = arrayNum[i%3] + 1;
            }
            
            // arrayNum[0] = 2; arrayNum[1] = 4; arrayNum[2] = 5
            arrayNum[1] = arrayNum[0] + arrayNum[1];
            arrayNum[2] = arrayNum[1] + arrayNum[2];

            int i = 1;
            while(st.hasMoreElements()){
                //System.out.println("next element outside if: " + st.nextElement());
                // these words are displayed on line 1
                if (i <= arrayNum[0]){
                      stringUOWName[0]  = stringUOWName[0] + st.nextElement() + " ";
                    //System.out.println("next element in line 1 is: " + st.nextElement());
                }

                // these words are displayed on line 2
                else if (i > arrayNum[0] && i <= arrayNum[1]){

                    stringUOWName[1] = stringUOWName[1] + st.nextElement() + " ";
                }

                // these words are displayed on line 3
                else if (i > arrayNum[1] && i <= arrayNum[2]){
                    stringUOWName[2] = stringUOWName[2] + st.nextElement() + " ";
                }
                i++;
            }

            // How many pixels wide is the string
            int msg_width1 = fontMetrics.stringWidth (stringUOWName[0]);
            int msg_width2 = fontMetrics.stringWidth (stringUOWName[1]);
            int msg_width3 = fontMetrics.stringWidth (stringUOWName[2]);

            // ascent of the font
            int ascent = fontMetrics.getAscent();

            // x coordinate of uowName
            int stringX1Coordinate = (int)(uowXCoordinate) - (msg_width1/2);
            int stringX2Coordinate = (int)(uowXCoordinate) - (msg_width2/2);
            int stringX3Coordinate = (int)(uowXCoordinate) - (msg_width3/2);

            // int y coordinate of uowName
            int stringY1Coordinate = (int)(uowYCoordinate) - 6;
            int stringY2Coordinate = (int)(uowYCoordinate) - 6 + ascent;
            int stringY3Coordinate = (int)(uowYCoordinate) - 6 + 2*ascent;

            // set this graphics context's font to the specified font
            g.setFont(font);

            // draw uow name
            g.drawString(stringUOWName[0], stringX1Coordinate, stringY1Coordinate);
            g.drawString(stringUOWName[1], stringX2Coordinate, stringY2Coordinate);
            g.drawString(stringUOWName[2], stringX3Coordinate, stringY3Coordinate);
        }
    }

    public void drawUOWRels(Graphics g){
         RPAGERelationship.setZoomFactor(zoomFactor);
         for (RPAGERelationship uowRel: arrayOfUOWRels){
            uowRel.drawUOWRel(g);
        }
    }

    private void createRects(){
       Rectangle temRect;
       int x, y;

       for (int i=0; i < RECT_MAX_NUM; i++){
           temRect = new Rectangle(getXCoordinateOfPoint(i), getYCoordinateOfPoint(i),
                   (int)(RECT_SIZE * zoomFactor),(int) (RECT_SIZE * zoomFactor));
           arrayOfRects[i] = temRect;
       }
    }

    /**
     * Draw six small rectangle
     */
    private void drawRects(Graphics g){
       for (int i=0; i < RECT_MAX_NUM; i++){
           g.setColor(arrayOfRectColors[i]);
           g.drawRect(getXCoordinateOfPoint(i), getYCoordinateOfPoint(i),
                   (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       }
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
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.contains(x,y)){
               return true;
           }
       }
       return false;
   }

   public RPAGERelationship returnUOWRelContains(int x, int y){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.contains(x,y)){
               return uowRel;
           }
       }
       return null;
   }

   public RPAGERelationship findUOWRel(String fromUOWName, String toUOWName){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equals(fromUOWName) &&
               uowRel.getToShapeName().equals(toUOWName)){
               return uowRel;
           }
       }
       return null;
   }

   /**
    * Change the rectangle index
    */
   public void setRelFromRectIndex(RPAGERelationship selectedUOWRel, int fromRectIndex){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equals(selectedUOWRel.getFromShapeName())
                   && uowRel.getToShapeName().equals(selectedUOWRel.getToShapeName())
                   && uowName.equals(selectedUOWRel.getFromShapeName())
                   && uowRel.equals(selectedUOWRel)){
               uowRel.setRectIndexFrom(fromRectIndex);
               uowRel.setRPAGERelX1Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexFrom()));

               uowRel.setRPAGERelY1Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexFrom()));
           }
       }
   }

   public void setRelToRectIndex(RPAGERelationship selectedUOWRel, int toRectIndex){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getFromShapeName().equals(selectedUOWRel.getFromShapeName())
                   && uowRel.getToShapeName().equals(selectedUOWRel.getToShapeName())
                   && uowName.equals(selectedUOWRel.getToShapeName())
                   && uowRel.equals(selectedUOWRel)){
               uowRel.setRectIndexTo(toRectIndex);
               uowRel.setRPAGERelX2Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexTo()));

               uowRel.setRPAGERelY2Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
       }
   }




}
