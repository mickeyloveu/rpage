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
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpage_ver1.controller.DefaultController;

/**
 *
 * @author ducluu84
 */

/** The component inside the scroll pane. */

public class ProcessDrawingArea extends JPanel {
    private DefaultController defaultController;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public ProcessDrawingArea (DefaultController defaultController){
            this.defaultController = defaultController;
            defaultController.regPDrawingArea(this);
            initComponents();
    }// end constructor

    public void initComponents(){
        addKeyListener( new KeyListener(){
            public void keyPressed(KeyEvent keyEvent){
            defaultController.keyPressedInPDrawing(keyEvent);
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
                    defaultController.pDrawingMousePressed(mouseEvent);
                }// end mousePressed

                public void mouseReleased(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.pDrawingMouseReleased(mouseEvent);
                }// end mouseReleased

                public void mouseClicked(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.pDrawingMouseClicked(mouseEvent);
                }
            }// end MouseAdapter
            );// end addMouseListener

            addMouseMotionListener( new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.pDrawingMouseDragged(mouseEvent);
                }

                public void mouseMoved(MouseEvent mouseEvent){
                    // delegate the processing of this event to defaultController
                    defaultController.pDrawingMouseMoved(mouseEvent);
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

        setPreferredSize(defaultController.calculateCurrentProcessDrawingArea());
        revalidate();

        Graphics2D g2D = (Graphics2D)g;
        super.paintComponent(g);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        defaultController.drawProcesses(g2D);
        defaultController.drawProcessRelNames(g2D);

        defaultController.constructProcessGrid(g2D, getWidth(), getHeight());
    }

}

