/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpage_ver1.controller.DefaultController;

/**
 *
 * @author ducluu84
 */

 /** The component inside the scroll pane. */
public class UOWDrawingArea extends JPanel {
    private DefaultController defaultController;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public UOWDrawingArea (DefaultController defaultController){
            this.defaultController = defaultController;
            defaultController.regUOWDrawingArea(this);
            initComponents();
    }// end constructor

    public void initComponents(){
        addKeyListener( new KeyListener(){
            public void keyPressed(KeyEvent keyEvent){
            defaultController.keyPressedInUOWDrawing(keyEvent);
        }

        public void keyReleased(KeyEvent e){
        }

        public void keyTyped(KeyEvent e){
        }
        }
        );// end addKeyListener

        addMouseListener( new MouseAdapter(){
                public void mousePressed(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.uowDrawingMousePressed(mouseEvent);
                }// end mousePressed

                public void mouseReleased(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.uowDrawingMouseReleased(mouseEvent);
                }// end mouseReleased

                public void mouseClicked(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.uowDrawingMouseClicked(mouseEvent);
                }

            }// end MouseAdapter
            );// end addMouseListener

            addMouseMotionListener( new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.uowDrawingMouseDragged(mouseEvent);
                }

                public void mouseMoved(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.uowDrawingMouseMoved(mouseEvent);
                }
            }
            );
    }

/******************************************************************************/
     //Other methods
/******************************************************************************/
    public void paintComponent(Graphics g){

        Dimension d = this.getSize(); //get size of document
        double panelWidth = d.width; //width in pixels
        double panelHeight = d.height; //height in pixels

        setPreferredSize(defaultController.calculateCurrentUOWDrawingArea());
        revalidate();

        Graphics2D g2D = (Graphics2D)g;
        super.paintComponent(g);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        defaultController.drawUOWs(g2D);
        defaultController.drawUOWRels(g2D);
        defaultController.drawUOWRelNames(g2D);
        defaultController.drawOSWSymbols(g2D);

        defaultController.constructUOWGrid(g2D, getWidth(), getHeight());
    }
}