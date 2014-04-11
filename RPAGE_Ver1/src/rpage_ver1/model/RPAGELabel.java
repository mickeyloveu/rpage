/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import rpage_ver1.controller.PropertyController;

/**
 *
 * @author ducluu84
 */
public class RPAGELabel  extends AbstractShape implements Serializable{
    private int xCoordinate;
    private int yCoordinate;
    private int rectWidth;
    private int rectHeight;
    private String rpageLabelName;

    // the rectangle that covers this relationship name
    private Rectangle rectangle;

    private Color color;

    // font of rpage label
    private String rpageLabelFontName;
    private Integer rpageLabelFontSize;

    private float zoomFactor = 1;

/******************************************************************************/
    //Constructor
/******************************************************************************/
    public RPAGELabel(int xCoordinate, int yCoordinate, String rpageLabelName){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.rpageLabelName = rpageLabelName;
        initDefault();
    }

    public RPAGELabel(int xCoordinate, int yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.rpageLabelName = "sample UOWRelationship Name";
        initDefault();
    }

    public void initDefault(){
        rpageLabelFontName = "Serif";
        rpageLabelFontSize = 12;
        rectWidth = 0;
        rectHeight = 0;
        this.color = Color.black;
    }

/******************************************************************************/
    //Accessor and mutator methods
/******************************************************************************/
    public float getZoomFactor(){
        return zoomFactor;
    }

    public void setZoomFactor(float newZoomFactor){
        zoomFactor = newZoomFactor;
    }

   /**
    * Return the name of this label
    */
    public String getRPAGELabelFontName(){
        return rpageLabelFontName;
    }

    public void setRPAGELabelFontName(String rpageLabelFontName){
        String oldFontName = this.rpageLabelFontName;
        this.rpageLabelFontName = rpageLabelFontName;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.RPAGE_LABEL_FONT_NAME_PROPERTY, oldFontName, rpageLabelFontName);
    }

    public Integer getRPAGELabelFontSize(){
        return rpageLabelFontSize;
    }

    public void setRPAGELabelFontSize(Integer rpageLabelFontSize){
        Integer oldFontSize = this.rpageLabelFontSize;
        this.rpageLabelFontSize = rpageLabelFontSize;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        firePropertyChange(PropertyController.RPAGE_LABEL_FONT_SIZE_PROPERTY, oldFontSize, rpageLabelFontSize);
    }

    public String getRPAGELabelName (){
        return rpageLabelName;
    }

   /**
    * Change the name of this label
    */
    public void setRPAGELabelName(String rpageLabelName){
        String oldName = this.rpageLabelName;
        this.rpageLabelName = rpageLabelName;

        // firePropertyChange(String propertyName, Object oldvalue, Object newvalue
        // This method first check if oldValue and newValue are equal. If they are, the method call is ignored
        // ATTENTION this method is IGNORED if oldValue and newValue are equal
        // When first create a UOW how to fire this method?
        System.out.println("rpage label name entered is: " + rpageLabelName);
        firePropertyChange(PropertyController.RPAGE_LABEL__NAME_PROPERTY, oldName, rpageLabelName);
    }

    public int getRPAGELabelXCoordinate(){
        return xCoordinate;
    }

    public void setRPAGELabelXCoordinate(int xCoordinate){
        if (xCoordinate <= 0){
            xCoordinate = 0;
        }
        this.xCoordinate = xCoordinate;
    }

    public int getRPAGELabelYCoordinate(){
        return yCoordinate;
    }

     public void setRPAGELabelYCoordinate(int yCoordinate){
        if (yCoordinate <= 0){
            yCoordinate = 0;
        }
        this.yCoordinate = yCoordinate;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public int getRectWidth(){
        return rectWidth;
    }

    public int getRectHeight(){
        return rectHeight;
    }


/******************************************************************************/
    //Other methods
/******************************************************************************/
    public boolean contains(int xCoordinate, int yCoordinate){
        rectangle = new Rectangle(this.xCoordinate, this.yCoordinate, rectWidth, rectHeight);
        return rectangle.contains(xCoordinate, yCoordinate);
    }

    public void drawRPAGELabel(Graphics g){
        // create font for this rpage label
        Font font = new Font(rpageLabelFontName, Font.PLAIN, (int)(rpageLabelFontSize * zoomFactor));

        FontMetrics fontMetrics = g.getFontMetrics (font);

        // width and height of the rectangle covering relationship name
        rectWidth = fontMetrics.stringWidth(rpageLabelName);
        rectHeight = fontMetrics.getAscent() + fontMetrics.getLeading() + 10;

        g.setFont(font);
        g.setColor(color);
        g.drawString(rpageLabelName, xCoordinate, yCoordinate + 15);
    }
}
