/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import rpage_ver1.controller.PropertyController;

/**
 *
 * @author ducluu84
 */
public class OutSideWorld extends AbstractShape implements Serializable{
    // a out side world symbol has 2 small rectangles
    private final int RECT_MAX_NUM = 2;
    private final int RECT_SIZE = 6;

    private GeneralPath cloud;

    private Integer oswSize;

    private int xCoordinate;
    private int yCoordinate;
    private Color color;
    private String name;

    private float controlX1, controlY1, controlX2, controlY2,
        controlX3, controlY3, controlX4, controlY4, controlX5, controlY5,
        controlX6, controlY6, controlX7, controlY7, controlX8, controlY8,
        controlX9, controlY9;

    private float x2Coordinate, y2Coordinate, x3Coordinate, y3Coordinate,
        x4Coordinate, y4Coordinate, x5Coordinate, y5Coordinate,
        x6Coordinate, y6Coordinate, x7Coordinate, y7Coordinate,
        x8Coordinate, y8Coordinate, x9Coordinate, y9Coordinate,
        x10Coordinate, y10Coordinate;

    private Color[] arrayOfRectColors;
    private Rectangle[] arrayOfRects;
    private ArrayList <RPAGERelationship> arrayOfUOWRels;

    //
    private Boolean isRectDisplayed;

    private static float zoomFactor = 1;
    private static int maxSize = 100;

/******************************************************************************/
     //Constructor and initDefault
/******************************************************************************/
    public OutSideWorld(){
        cloud = new GeneralPath();
        this.color = Color.black;
        initDefault();
    }// end constructor

    public OutSideWorld(int size, int xCoordinate, int yCoordinate, String oswSymbolName){
        this.oswSize = size;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.name = oswSymbolName;
        cloud = new GeneralPath();
        this.color = Color.black;
        initDefault();
    }// end constructor

   /**
    *
    */
    public void initDefault() {
        isRectDisplayed = false;
        arrayOfRects = new Rectangle[RECT_MAX_NUM];
        arrayOfRectColors = new Color[RECT_MAX_NUM];
        for (int i=0; i<RECT_MAX_NUM; i++){
            arrayOfRectColors[i]= Color.DARK_GRAY;
        }
        arrayOfUOWRels = new ArrayList<RPAGERelationship>();
        calculateCoordinate();
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

    public String getName(){
         return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getOSWSize(){
        return oswSize;
    }

    public void setOSWSize(Integer oswSize){
        if (oswSize >= maxSize){
            oswSize = maxSize;
        }
        Integer oldOSWSize = this.oswSize;
        this.oswSize = oswSize;

        calculateCoordinate();
        for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getRectIndexFrom() == 0 &&
                       uowRel.getFromShapeName().equalsIgnoreCase(name)){
                           uowRel.setRPAGERelX1Coordinate(
                               (int)(xCoordinate));
                           uowRel.setRPAGERelY1Coordinate(
                               (int)(yCoordinate));
                           System.out.println("rect index");
           }

           if (uowRel.getRectIndexFrom() == 1 &&
                       uowRel.getFromShapeName().equalsIgnoreCase(name)){
                           uowRel.setRPAGERelX1Coordinate(
                               (int)x4Coordinate);
                           uowRel.setRPAGERelY1Coordinate(
                                (int)y4Coordinate);
           }
        }

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        firePropertyChange(PropertyController.OSW_SIZE_PROPERTY, oldOSWSize, oswSize);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public int getXCoordinate(){
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate){
        if (xCoordinate <= 0){
            xCoordinate = 0;
        }
        this.xCoordinate = xCoordinate;
        calculateCoordinate();
        for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getRectIndexFrom() == 0 &&
                       uowRel.getFromShapeName().equalsIgnoreCase(name)){
                           uowRel.setRPAGERelX1Coordinate(
                               xCoordinate);
           }

           if (uowRel.getRectIndexFrom() == 1 &&
                       uowRel.getFromShapeName().equalsIgnoreCase(name)){
                           uowRel.setRPAGERelX1Coordinate(
                               (int)x4Coordinate);
           }
        }
     }

    public int getYCoordinate(){
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate){
        if (yCoordinate <= 0){
            yCoordinate = 0;
        }
        this.yCoordinate = yCoordinate;
         calculateCoordinate();
            for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getRectIndexFrom() == 0 &&
                        uowRel.getFromShapeName().equals(name)){
                            uowRel.setRPAGERelY1Coordinate(
                                yCoordinate);
            }

            if (uowRel.getRectIndexFrom() == 1 &&
                        uowRel.getFromShapeName().equals(name)){
                            uowRel.setRPAGERelY1Coordinate(
                                (int)y4Coordinate);
            }
        }
     }

    public Rectangle getBounds(){
        if (cloud != null){
            return cloud.getBounds();
        }
        return null;
    }

    /**
     * set color for the rectangle
     */
     public void setRectColor(int rectIndex, Color color){
          arrayOfRectColors[rectIndex] = color;
     }

     // return the point that relates to the rectangle
     // For example, the second rectangle have x4, y4 as it coordinates
     public final int getXCoordinateOfPoint(int rectangleIndex){
         if (rectangleIndex == 0){
             return xCoordinate;
         }
         if (rectangleIndex == 1){
             return (int)x4Coordinate;
         }
         return -1;
     }

     // return the point that relates to the rectangle
     // For example, the second rectangle have x4, y4 as it coordinates
     public final int getYCoordinateOfPoint(int rectangleIndex){
         if (rectangleIndex == 0){
             return yCoordinate;
         }
         if (rectangleIndex == 1){
             return (int)y4Coordinate;
         }
         return -1;
     }

     public void addUOWRel(RPAGERelationship uowRel){
         arrayOfUOWRels.add(uowRel);
     }

    public void removeRel(RPAGERelationship uowRel){
       if (uowRel.getFromShapeName().equals(name)){
               uowRel.setFromShapeName("DELETED");
           }
       arrayOfUOWRels.remove(uowRel);
   }

   // when a outside world symbol is deleted all its relationships should be
   // deleted as well
   public void removeAllUOWRel(){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equals(name)){
               uowRel.setFromShapeName("DELETED");
           }
       }
       arrayOfUOWRels.clear();
   }

  /**
   * Removes all uow relationships that toUOWName is deleted
   */
   public void removeRel(){
       ArrayList<RPAGERelationship> temArrayOfUOWRels = new ArrayList<RPAGERelationship>();
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (!uowRel.getToShapeName().equals("DELETED")){
               temArrayOfUOWRels.add(uowRel);
           }
        }
       arrayOfUOWRels.clear();
       arrayOfUOWRels = temArrayOfUOWRels;
   }

   public void setDisplayRect(Boolean isRectDisplayed){
       this.isRectDisplayed = isRectDisplayed;
   }// end method

      /**
    * Change the rectangle index
    */
   public void setRelFromRectIndex(RPAGERelationship selectedUOWRel, int fromRectIndex){
       for (RPAGERelationship uowRel: arrayOfUOWRels){
           if (uowRel.getFromShapeName().equalsIgnoreCase(selectedUOWRel.getFromShapeName())
                   && uowRel.getToShapeName().equalsIgnoreCase(selectedUOWRel.getToShapeName())
                   && name.equalsIgnoreCase(selectedUOWRel.getFromShapeName())
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
            if (uowRel.getFromShapeName().equalsIgnoreCase(selectedUOWRel.getFromShapeName())
                   && uowRel.getToShapeName().equalsIgnoreCase(selectedUOWRel.getToShapeName())
                   && name.equalsIgnoreCase(selectedUOWRel.getToShapeName())
                   && uowRel.equals(selectedUOWRel)){
               uowRel.setRectIndexTo(toRectIndex);
               uowRel.setRPAGERelX2Coordinate(
                            getXCoordinateOfPoint(uowRel.getRectIndexTo()));

               uowRel.setRPAGERelY2Coordinate(
                        getYCoordinateOfPoint(uowRel.getRectIndexTo()));
            }
       }
   }

/******************************************************************************/
    //Other methods
/******************************************************************************/
     public void draw(Graphics g){
        calculateCoordinate();
        cloud = new GeneralPath();
        cloud.moveTo(xCoordinate, yCoordinate);
        cloud.quadTo(controlX1, controlY1, x2Coordinate, yCoordinate);
        cloud.quadTo(controlX2, controlY2, x3Coordinate, y3Coordinate);
        cloud.quadTo(controlX3, controlY3, x4Coordinate, y4Coordinate);
        cloud.quadTo(controlX4, controlY4, x5Coordinate, y5Coordinate);
        cloud.quadTo(controlX5, controlY5, x6Coordinate, y6Coordinate);
        cloud.quadTo(controlX6, controlY6, x7Coordinate, y7Coordinate);
        cloud.quadTo(controlX7, controlY7, x8Coordinate, y8Coordinate);
        cloud.quadTo(controlX8, controlY8, x9Coordinate, y9Coordinate);
        cloud.quadTo(controlX9, controlY9, x10Coordinate, y10Coordinate);

        cloud.closePath();
        Graphics2D g2D = (Graphics2D)g;
        g2D.setColor(color);
        g2D.draw(cloud);
        createRects();
        if (isRectDisplayed){
            drawRects(g);
        }

        // draw uow relationship
        RPAGERelationship.setZoomFactor(zoomFactor);
        for (RPAGERelationship uowRel: arrayOfUOWRels){
            if (uowRel.getFromShapeName().equals(name)){
                uowRel.drawUOWRel(g);
            }
        }
   }

   public boolean contains(int xCoordinate, int yCoordinate){
       if (cloud != null && cloud.contains(xCoordinate, yCoordinate)){
           return true;
       }
       else return false;
   }

   private void createRects(){
       Rectangle temRect;

       temRect = new Rectangle((int) xCoordinate, (int)yCoordinate,
                    (int)(RECT_SIZE * zoomFactor), (int) (RECT_SIZE * zoomFactor));
       arrayOfRects[0] = temRect;

       temRect = new Rectangle((int) x4Coordinate, (int)y4Coordinate,
                   (int)(RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
       arrayOfRects[1] = temRect;
    }


    /**
     * Draw three small rectangle
     */
    private void drawRects(Graphics g){
       g.setColor(arrayOfRectColors[0]);
       g.drawRect((int)xCoordinate, (int)yCoordinate, (int)(RECT_SIZE * zoomFactor), (int) (RECT_SIZE * zoomFactor));
       g.setColor(arrayOfRectColors[1]);
       g.drawRect((int)x4Coordinate, (int)y4Coordinate, (int) (RECT_SIZE * zoomFactor), (int)(RECT_SIZE * zoomFactor));
    }// end method

    private void calculateCoordinate(){
        controlX1 = xCoordinate + oswSize/2 * zoomFactor;
        controlY1= yCoordinate - 0.75F*oswSize * zoomFactor;

        x2Coordinate = xCoordinate + 0.95F*oswSize * zoomFactor;
        y2Coordinate = yCoordinate;
        controlX2 = x2Coordinate + 0.75F*oswSize * zoomFactor;
        controlY2 = y2Coordinate + 0.25F*oswSize * zoomFactor;

        x3Coordinate = x2Coordinate;
        y3Coordinate = y2Coordinate + 0.5F*oswSize * zoomFactor;
        controlX3 = x3Coordinate + oswSize * zoomFactor;
        controlY3 = y3Coordinate + 0.25F*oswSize * zoomFactor;

        x4Coordinate = x3Coordinate;
        y4Coordinate = y3Coordinate + 0.5F*oswSize * zoomFactor;
        controlX4 = x4Coordinate - 0.5F*oswSize * zoomFactor;
        controlY4 = y4Coordinate + 0.5F*oswSize * zoomFactor;

        x5Coordinate = x4Coordinate - 0.75F*oswSize * zoomFactor;
        y5Coordinate = y4Coordinate;
        controlX5 = x5Coordinate - 0.5F*oswSize * zoomFactor;
        controlY5 = y5Coordinate + 0.5F*oswSize * zoomFactor;

        x6Coordinate = x5Coordinate - 0.75F*oswSize * zoomFactor;
        y6Coordinate = y5Coordinate;
        controlX6 = x6Coordinate - 0.5F*oswSize * zoomFactor;
        controlY6 = y6Coordinate + 0.5F*oswSize * zoomFactor;

        x7Coordinate = x6Coordinate - 0.75F*oswSize * zoomFactor;
        y7Coordinate = y6Coordinate;
        controlX7 = x7Coordinate - 0.25F*oswSize * zoomFactor;
        controlY7 = y7Coordinate - 0.45F*oswSize * zoomFactor;

        x8Coordinate = x7Coordinate + 0.45F*oswSize * zoomFactor;
        y8Coordinate = y7Coordinate - 0.45F*oswSize * zoomFactor;
        controlX8 = x8Coordinate - 0.5F*oswSize * zoomFactor;
        controlY8 = y8Coordinate - 0.45F*oswSize * zoomFactor;

        x9Coordinate = x8Coordinate + 0.45F*oswSize * zoomFactor;
        y9Coordinate = y8Coordinate - 0.45F*oswSize * zoomFactor;
        controlX9 = x9Coordinate + 0.125F*oswSize * zoomFactor;
        controlY9 = y9Coordinate - 0.5F*oswSize * zoomFactor;

        x10Coordinate = xCoordinate ;
        y10Coordinate = yCoordinate;
    }

   /**
    * Check if x,y belongs to any rectangle
    */
    public boolean isWithinRects(int x, int y){
        for (int i=0; i < RECT_MAX_NUM; i++) {
            if (arrayOfRects[i].contains(x,y)){
                return arrayOfRects[i].contains(x,y);
            }
        }
        return false;
    }// end method

  /**
   * Return the index of rectangle that x, y belong to
   */
   public int getOSWRectIndex(int x, int y){
       for (int i = 0; i < arrayOfRects.length; i++) {
            if (arrayOfRects[i].contains(x, y)){
                return i;
            }
        }
        return -1;
   }// end method
}
