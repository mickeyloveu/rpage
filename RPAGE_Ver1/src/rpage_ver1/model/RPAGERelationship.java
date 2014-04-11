/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import rpage_ver1.controller.PropertyController;

/**
 *
 * @author ducluu84
 */
public class RPAGERelationship implements Serializable{
    private String rpageRelName;
    private Integer rpageRelWidth;
    private Integer rpageRelHeight;

    // X, Y Coordinate of rpage relationship
    private Integer rpageRelX1Coordinate;
    private Integer rpageRelY1Coordinate;

    private Integer rpageRelX2Coordinate;
    private Integer rpageRelY2Coordinate;

    private Integer rpageRelControlXCoordinate;
    private Integer rpageRelControlYCoordinate;

    //
    private Integer rectIndexFrom;
    private Integer rectIndexTo;

    //
    private String fromShapeName;
    private String toShapeName;

    // calculate the length of the arrow
    private double length;

    private double barb = 4;


    // create new QuadCurve2D.Float
    private QuadCurve2D quadCurve2D = new QuadCurve2D.Float();

    // a circle has been used to dynamically change the control point of the curve
    private Ellipse2D.Float circle = new Ellipse2D.Float();
    private float radius = 4;
    boolean isDrawCircle = false;
    private Color circleColor = Color.red;
    private Color rpageRelColor = Color.black;

    private String fromShapeType;

    private static float zoomFactor = 1;

/******************************************************************************/
    //Constructor
/******************************************************************************/
    public RPAGERelationship(Integer rpageRelX1Coordinate, Integer rpageRelY1Coordinate,
            Integer rpageRelX2Coordinate, Integer rpageRelY2Coordinate,
            String fromShapeName, String toShapeName, int rectIndexFrom,
            int rectIndexTo, String fromShapeType){
        this.rpageRelX1Coordinate = rpageRelX1Coordinate;
        this.rpageRelY1Coordinate = rpageRelY1Coordinate;

        this.rpageRelX2Coordinate = rpageRelX2Coordinate;
        this.rpageRelY2Coordinate = rpageRelY2Coordinate;

        rpageRelControlXCoordinate = (rpageRelX2Coordinate -
                rpageRelX1Coordinate)/2 + rpageRelX1Coordinate;

        rpageRelControlYCoordinate =(rpageRelY2Coordinate -
                rpageRelY1Coordinate)/2 + rpageRelY1Coordinate;

        circle = new Ellipse2D.Float(rpageRelControlXCoordinate - radius,
                rpageRelControlYCoordinate - radius, 2 * radius, 2 * radius);

        this.fromShapeName = fromShapeName;
        this.toShapeName = toShapeName;

        this.rectIndexFrom = rectIndexFrom;
        this.rectIndexTo = rectIndexTo;

        this.fromShapeType = fromShapeType;
    }

     public RPAGERelationship(Integer rpageRelX1Coordinate, Integer rpageRelY1Coordinate,
            Integer rpageRelX2Coordinate, Integer rpageRelY2Coordinate, String fromShapeType){
        this.rpageRelX1Coordinate = rpageRelX1Coordinate;
        this.rpageRelY1Coordinate = rpageRelY1Coordinate;

        this.rpageRelX2Coordinate = rpageRelX2Coordinate;
        this.rpageRelY2Coordinate = rpageRelY2Coordinate;

        rpageRelControlXCoordinate = (rpageRelX2Coordinate -
                rpageRelX1Coordinate)/2 + rpageRelX1Coordinate;

        rpageRelControlYCoordinate =(rpageRelY2Coordinate -
                rpageRelY1Coordinate)/2 + rpageRelY1Coordinate;

        circle = new Ellipse2D.Float(rpageRelControlXCoordinate - radius,
                rpageRelControlYCoordinate - radius, 2 * radius, 2 * radius);

        this.fromShapeType = fromShapeType;
     }

     public RPAGERelationship(){

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

    public String getFromShapeType(){
        return fromShapeType;
    }

    public void setFromShapeName(String fromShapeName){
        this.fromShapeName = fromShapeName;
    }

    public String getFromShapeName(){
        return fromShapeName;
    }

    public void setToShapeName(String toShapeName){
        this.toShapeName = toShapeName;
    }

    public String getToShapeName(){
        return toShapeName;
    }

    public void setRPAGERelColor(Color rpageRelColor){
        this.rpageRelColor = rpageRelColor;
    }
    public Integer getRPAGERelX1Coordinate(){
        return rpageRelX1Coordinate;
    }

    public void setRPAGERelX1Coordinate(Integer rpageRelX1Coordinate){
        this.rpageRelX1Coordinate = rpageRelX1Coordinate;
    }

    public Integer getRPAGERelY1Coordinate(){
        return rpageRelY1Coordinate;
    }

    public void setRPAGERelY1Coordinate(Integer rpageRelY1Coordinate){
        this.rpageRelY1Coordinate = rpageRelY1Coordinate;
    }

    public Integer getRPAGERelX2Coordinate(){
        return rpageRelX2Coordinate;
    }

    public void setRPAGERelX2Coordinate(Integer rpageRelX2Coordinate){
        this.rpageRelX2Coordinate = rpageRelX2Coordinate;
    }

    public Integer getRPAGERelY2Coordinate(){
        return rpageRelY2Coordinate;
    }

    public void setRPAGERelY2Coordinate(Integer rpageRelY2Coordinate){
        this.rpageRelY2Coordinate = rpageRelY2Coordinate;
    }

    public Integer getRectIndexFrom(){
        return rectIndexFrom;
    }// end getRectIndexFrom method

    public void setRectIndexFrom(Integer rectIndexFrom){
        this.rectIndexFrom = rectIndexFrom;
    }

    public Integer getRectIndexTo(){
        return rectIndexTo;
    }// end getRectIndexTo method

    public void setRectIndexTo(Integer rectIndexTo){
        this.rectIndexTo = rectIndexTo;
    }

    public void setControlPoint(int rpageRelControlXCoordinate,
            int rpageRelControlYCoordinate){
        this.rpageRelControlXCoordinate = rpageRelControlXCoordinate;
        this.rpageRelControlYCoordinate = rpageRelControlYCoordinate;

        circle = new Ellipse2D.Float(rpageRelControlXCoordinate - radius,
                rpageRelControlYCoordinate - radius, 2 * radius, 2 * radius);
    }

/******************************************************************************/
    //Other methods
/******************************************************************************/
    public void drawUOWRel(Graphics g){
        int deltaX = rpageRelX2Coordinate - rpageRelControlXCoordinate;
        int deltaY = rpageRelY2Coordinate - rpageRelControlYCoordinate;

        length = Math.sqrt(deltaX*deltaX + deltaY * deltaY);

        g.setColor(rpageRelColor);

        //computes the phase theta by computing an arc tangent of y/x
        double theta = Math.atan2(deltaY, deltaX);

        // an arrow consist of three line ->
        // draw the first line -

        //g.drawLine(uowRelationshipX1Coordinate,uowRelationshipY1Coordinate,
        //        uowRelationshipX2Coordinate,uowRelationshipY2Coordinate);

        // rotate two points to make arrow and then translate
        // (length - 10, -10)
        // (length - 10, +10)

        // X’ = cos(A)X – sin(A)Y
        // Y’ = sin(A)X + cos(A)Y

        // (length - 10, -10)
        // create two second lines >

        int x3 = rpageRelControlXCoordinate + Math.round( (float)
                (Math.cos(theta) * (length - barb * zoomFactor) -
                Math.sin(theta) * (-barb * zoomFactor))
                );

        int y3 = rpageRelControlYCoordinate + Math.round( (float)
                (Math.sin(theta) * (length - barb * zoomFactor) +
                Math.cos(theta) * (-barb * zoomFactor))
                );

        // (length - 10, +10)
          int x4 = rpageRelControlXCoordinate + Math.round( (float)
                (Math.cos(theta) * (length - barb * zoomFactor) -
                Math.sin(theta) * (barb * zoomFactor))
                );

        int y4 = rpageRelControlYCoordinate + Math.round( (float)
                (Math.sin(theta) * (length - barb * zoomFactor) +
                Math.cos(theta) * (barb * zoomFactor))
                );

        g.drawLine(x3, y3, rpageRelX2Coordinate, rpageRelY2Coordinate
                );

        g.drawLine(x4, y4, rpageRelX2Coordinate, rpageRelY2Coordinate
                );

        Graphics2D g2D = (Graphics2D)g;

        // draw QuadCurve2D.Float with set coordinates
        quadCurve2D.setCurve(rpageRelX1Coordinate, rpageRelY1Coordinate,
                rpageRelControlXCoordinate, rpageRelControlYCoordinate,
                rpageRelX2Coordinate, rpageRelY2Coordinate);
        g2D.draw(quadCurve2D);

        circle = new Ellipse2D.Float(rpageRelControlXCoordinate - radius,
                rpageRelControlYCoordinate - radius, 2 * radius, 2 * radius);

        if (isDrawCircle == true){
            g2D.setColor(circleColor);
            g2D.draw(circle);
            g2D.setColor(Color.black);
        }

    }// end drawUOWRelationship method

    public boolean contains(int xCoordinate, int yCoordinate){

        if (Math.abs(yCoordinate - rpageRelY1Coordinate) <= 5 ){
            if (rpageRelX2Coordinate > rpageRelX1Coordinate &&
                    xCoordinate > rpageRelX1Coordinate && xCoordinate < rpageRelX2Coordinate){
                return true;
            }

            else if (rpageRelX2Coordinate < rpageRelX1Coordinate &&
                    xCoordinate > rpageRelX2Coordinate && xCoordinate < rpageRelX1Coordinate){
                return true;
            }

        }

        else {
            for (int i = -10; i<=10; i++){
                for (int j = -10; j<=10; j++){
                    if (quadCurve2D.contains(xCoordinate + i, yCoordinate +j)){
                        return true;
                    }
                }
            }
         }

        return false;
    }

    public boolean circleContains(int x, int y){
        if (circle.contains(x,y)){
            return true;
        }
        else return false;
    }

    public void setIsDrawCircle(Boolean isDrawCircle){
        this.isDrawCircle = isDrawCircle;
    }
}
