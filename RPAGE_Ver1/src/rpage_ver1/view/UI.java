/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import javax.swing.*;

import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpage_ver1.controller.DefaultController;
import rpage_ver1.view.UOWDiagramPanel;
import rpage_ver1.view.ProcessDiagramPanel;

/**
 *
 * @author ducluu84
 */
public class UI extends JFrame {

    // controller used by UI
    DefaultController defaultController;

    // UOW diagram
    private UOWDiagramPanel uowDiagramPanel;
    private ProcessDiagramPanel pDiagramPanel;

    //
    private RPAGEPallettePanel rpagePalPanel;

    //
    private JFileChooser fileChooser;

     // JMenuBar
    private JMenuBar jmb;

    // Tabbed Pane
    private JTabbedPane jTabbedPane;

    // File, View, Add, Edit, Help
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;

    /**************************************************************************/
    // New, Open, Save, Print, Exit in File
    JMenuItem newMenuItem;
    JMenuItem openMenuItem;
    JMenuItem saveMenuItem;
    JMenuItem saveAsMenuItem;
    JMenuItem printMenuItem;
    JMenuItem exitMenuItem;
    /**************************************************************************/

    /**************************************************************************/
    // Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram in View menu
    JMenuItem zoomInMenuItem;
    JMenuItem zoomOutMenuItem;
    JMenuItem uowMenuItem;
    JMenuItem processMenuItem;
    /**************************************************************************/

    /**************************************************************************/
    // Help Contents, About in Help menu
    JMenu helpContentSubMenu;
    JMenuItem mainWorkingAreaMenuItem;
    JMenuItem rpageToolBarMenuItem;
    JMenuItem rpagePaletteToolMenuItem;
    JMenuItem rpageDiagramsMenuItem;
    JMenuItem rpagePropertyPanelMenuItem;

    JMenuItem aboutMenuItem;
    /**************************************************************************/

    /**************************************************************************/
    // button: New, Open, Save, Print
    JButton newButton;
    JButton openButton;
    JButton saveButton;
    JButton printButton;
    /**************************************************************************/

    /**************************************************************************/
    // button: Zoom in, Zoom out, UOW diagram, 1st cut diagram, 2nd cut diagram
    JButton zoomInButton;
    JButton zoomOutButton;
    JButton uowButton;
    JButton processButton;
    /**************************************************************************/

    /**************************************************************************/
    // toolbar for File, View, Add, Edit
    JToolBar fileJToolBar;
    JToolBar viewJToolBar;
    /**************************************************************************/

    // panel containing toolbars
    JPanel allToolBarJPanel;

    // define constants
    public static final int UOW_DIAGRAM = 0;
    public static final int PROCESS_DIAGRAM = 1;

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public UI(UOWDiagramPanel uowDiagramPanel, ProcessDiagramPanel pDiagramPanel, RPAGEPallettePanel rpagePalPanel,
            DefaultController defaultController){
        // key events
        setFocusable(true);

        //
        this.uowDiagramPanel = uowDiagramPanel;
        this.pDiagramPanel = pDiagramPanel;
        this.rpagePalPanel = rpagePalPanel;
        this.defaultController = defaultController;
        defaultController.regUI(this);

       //
        initComponents();
    }// end constructor

     public void initComponents() {
        // create the JTabbed Pane
        jTabbedPane = new JTabbedPane();

        // create the file chooser
        fileChooser = new JFileChooser();

        // create a jmenu bar
        jmb  = new JMenuBar();

        // Attach a menu bar to a frame
        setJMenuBar(jmb);

        /*********************************************************************/
        // create File, View, Add, Edit, Help
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        // add File, View, Add, Edit, Help in jmenu bar
        jmb.add(fileMenu);
        jmb.add(viewMenu);
        jmb.add(helpMenu);

        // set keyboard mnemonics for New, Open, Save, Print, Exit
        // Alt + mnemonics
        fileMenu.setMnemonic('F');
        viewMenu.setMnemonic('V');
        helpMenu.setMnemonic('H');

        /**********************************************************************/

        /**********************************************************************/
        // create New, Open, Save, Print, Exit in File menu
        newMenuItem = new JMenuItem("New");
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        saveAsMenuItem = new JMenuItem("Save as...");
        printMenuItem = new JMenuItem("Print");
        exitMenuItem = new JMenuItem("Exit");

        // add New, Open, Save, Print, Exit in File menu
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

       // set icons for New, Open, Save, Save as, Print, Exit

        // ICON COPYRIGHT
        // the author use icons from Java(TM) Look and Feel Graphics Repository 1.0
        // https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewLicense-Start
        URL imageURL = getClass().getResource("/images/new_24.gif");
        newMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/open_24.gif");
        openMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/save_24.gif");
        saveMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/saveAs_24.gif");
        saveAsMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/print_24.gif");
        printMenuItem.setIcon(new ImageIcon(imageURL));

        // set keyboard mnemonics for New, Open, Save, Print, Exit
        // Alt + mnemonics
        newMenuItem.setMnemonic('N');
        openMenuItem.setMnemonic('O');
        saveMenuItem.setMnemonic('S');

        // set key accelerators for New, Open, Save, Print, Exit
        // Ctrl + accelerator keys
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        /**********************************************************************/

        /**********************************************************************/
         // create Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram in View menu
        zoomInMenuItem = new JMenuItem("Zoom in");
        zoomOutMenuItem = new JMenuItem("Zoom out");
        uowMenuItem = new JMenuItem("UOW diagram");
        processMenuItem = new JMenuItem("Prcoess diagram");

        // add Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram in View menu
        viewMenu.add(zoomInMenuItem);
        viewMenu.add(zoomOutMenuItem);
        viewMenu.add(uowMenuItem);
        viewMenu.add(processMenuItem);

        // set icons for Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram

        // ICON COPYRIGHT
        // the author use icons from Java(TM) Look and Feel Graphics Repository 1.0
        // https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewLicense-Start
        imageURL = getClass().getResource("/images/zoomIn_24.gif");
        zoomInMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/zoomOut_24.gif");
        zoomOutMenuItem.setIcon(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/stop_24.gif");
        uowMenuItem.setIcon(new ImageIcon(imageURL));

        // set keyboard mnemonics for Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram
        // Alt + mnemonics

        // set key accelerators for Zoom in, Zoom out, UoW diagram, 1st cut diagram, 2nd cut diagram
        // Ctrl + accelerator keys

        /**********************************************************************/

        /**********************************************************************/
        // create Help Contents, About in Help menu
        helpContentSubMenu = new JMenu("Help Contents");
        mainWorkingAreaMenuItem = new JMenuItem ("Main Working Area");
        rpageToolBarMenuItem = new JMenuItem ("R-PAGE Tool Bar");
        rpagePaletteToolMenuItem = new JMenuItem ("R-PAGE Palette Tool");
        rpageDiagramsMenuItem = new JMenuItem ("R-PAGE Diagrams");
        rpagePropertyPanelMenuItem = new JMenuItem ("R-PAGE Property Panel");

        helpContentSubMenu.add(mainWorkingAreaMenuItem);
        helpContentSubMenu.add(rpageToolBarMenuItem);
        helpContentSubMenu.add(rpagePaletteToolMenuItem);
        helpContentSubMenu.add(rpageDiagramsMenuItem);
        helpContentSubMenu.add(rpagePropertyPanelMenuItem);

        aboutMenuItem = new JMenuItem("About R-PAGE");

        // add Help Contents, About in HELP menu
        helpMenu.add(helpContentSubMenu);
        helpMenu.add(aboutMenuItem);

        // REMEMBER TO ATTACH THE LINK OF ICON COPY RIGHT IN ABOUT SECTION

        /**********************************************************************/
        // create button for FILE Menu
        // New, open, save, print

        imageURL = getClass().getResource("/images/new_24.gif");
        newButton = new JButton(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/open_24.gif");
        openButton = new JButton(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/save_24.gif");
        saveButton = new JButton(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/print_24.gif");
        printButton = new JButton(new ImageIcon(imageURL));

        newButton.setToolTipText("New");
        openButton.setToolTipText("Open");
        saveButton.setToolTipText("Save");
        printButton.setToolTipText("Print");

        // create tool bar for File menu
        fileJToolBar = new JToolBar("File Tool Bar");
        fileJToolBar.setFloatable(true);
        fileJToolBar.add(newButton);
        fileJToolBar.add(openButton);
        fileJToolBar.add(saveButton);
        fileJToolBar.add(printButton);
        /**********************************************************************/

        /**********************************************************************/
        // create button for VIEW Menu
        // zoom in, zoom out, uow, first cut, second cut
        imageURL = getClass().getResource("/images/zoomIn_24.gif");
        zoomInButton = new JButton(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/zoomOut_24.gif");
        zoomOutButton = new JButton(new ImageIcon(imageURL));

        imageURL = getClass().getResource("/images/stop_24.gif");
        uowButton = new JButton(new ImageIcon(imageURL));

        processButton = new JButton("Process diagram");

        zoomInButton.setToolTipText("Click here to zoom in (100%)" );
        zoomOutButton.setToolTipText("Click here to zoom out (100%)");
        uowButton.setToolTipText("Click here to view UOW diagram");
        processButton.setToolTipText("Click here to view process diagram");

        // create tool bar for VIEW menu
        viewJToolBar = new JToolBar("View Tool Bar");
        viewJToolBar.setFloatable(true);
        viewJToolBar.add(zoomInButton);
        viewJToolBar.add(zoomOutButton);
        viewJToolBar.add(uowButton);
        viewJToolBar.add(processButton);
        /**********************************************************************/

        /**********************************************************************/

        /**********************************************************************/
         // create panel containing all toolbars
        allToolBarJPanel = new JPanel();
        allToolBarJPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        allToolBarJPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        allToolBarJPanel.add(fileJToolBar);
        allToolBarJPanel.add(viewJToolBar);

       /***********************************************************************/

        /**********************************************************************/
        // BorderLayout(hgap: int, vgap: int)
        // creates a BorderLayout manageer with a specified number of horizontal gap
        // and vertical gap
        setLayout(new BorderLayout(0,0));

        // add the toolbar to the north position of the content pane
        getContentPane().add(allToolBarJPanel, BorderLayout.NORTH);
        //getContentPane().add(fileJToolBar, BorderLayout.NORTH);

        // add UOW Diagram and process diagram to this tabbed pane
        jTabbedPane.addTab("UOW", null, uowDiagramPanel, "UOW diagram");
        jTabbedPane.addTab("Process diagram", null, pDiagramPanel, "Process diagram");

        // add tabbed pane to the UI
        add(jTabbedPane, BorderLayout.CENTER);
        add(rpagePalPanel, BorderLayout.WEST);

        // handle EXIT event
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowEvent) {
                if (defaultController.uiExitWindowClosing(windowEvent)){
                    System.exit(0);
                }
            }
        });

        // handle EXIT event
        exitMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                if (defaultController.uiExitWindowClosing(actionEvent)){
                    System.exit(0);
                }
            }
        });

        // handle NEW event
        newMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiNewActionPerformed(actionEvent);
            }
        });

        // handle NEW event
        newButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiNewActionPerformed(actionEvent);
            }
        });



        // handle SAVE event
        saveMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiSaveActionPerformed(actionEvent);
            }
        });

        // handle SAVE event
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiSaveActionPerformed(actionEvent);

            }
        });

         // handle SAVE AS event
        saveAsMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiSaveAsActionPerformed(actionEvent);

            }
        });

        // handle OPEN event
        openMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiOpenActionPerformed(actionEvent);

            }
        });

        // handle OPEN event
        openButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiOpenActionPerformed(actionEvent);

            }
        });

        // handle PRINT event
        printMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiPrintActionPerformed(actionEvent);
            }
        });

        // handle PRINT event
        printButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiPrintActionPerformed(actionEvent);
            }
        });

        // handle Zoom in event
        zoomInButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent actionEvent){
               defaultController.uiZoomInButtonAP(actionEvent);
           }
        });

        // handle Zoom in event
        zoomInMenuItem.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent actionEvent){
               defaultController.uiZoomInButtonAP(actionEvent);
           }
        });

        // handle Zoom out
        zoomOutButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent actionEvent){
               System.out.println("zoom out");
               defaultController.uiZoomOutButtonAP(actionEvent);
           }
        });

        // handle Zoom out
        zoomOutMenuItem.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent actionEvent){
               defaultController.uiZoomOutButtonAP(actionEvent);
           }
        });

        // handle events in uow button
        uowButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.userChoosingUOWDiagram();
                jTabbedPane.setSelectedIndex(0);
            }
        });

        uowMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.userChoosingUOWDiagram();
                jTabbedPane.setSelectedIndex(0);
            }
        });

        // handle events in process cut button
        processButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.userChoosingPDiagram();
                jTabbedPane.setSelectedIndex(1);
            }
        });

        processMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.userChoosingPDiagram();
                jTabbedPane.setSelectedIndex(1);
            }
        });

        // Register a change listener
        jTabbedPane.addChangeListener(new ChangeListener(){
        // This method is called whenever the selected tab changes
            public void stateChanged(ChangeEvent evt){
                int tabSelectedIndex = jTabbedPane.getSelectedIndex();
                switch (tabSelectedIndex){
                    case UOW_DIAGRAM:
                        rpagePalPanel.addUOWPalPanel();
                        float uowZoomFactor = defaultController.getUOWZoomFactor();
                        String uowZoomPercent = ((Float)(uowZoomFactor * 100)).toString();
                        setZoomInToolTip("Click here to zoom in (" + uowZoomPercent +"%" +")");
                        setZoomOutToolTip("Click here to zoom out (" + uowZoomPercent +"%" +")");
                        defaultController.userChoosingUOWDiagram();
                        break;

                    case PROCESS_DIAGRAM:
                        rpagePalPanel.addPPalPanel();
                        float processZoomFactor = defaultController.getPZoomFactor();
                        String processZoomPercent = ((Float)(processZoomFactor * 100)).toString();
                        setZoomInToolTip("Click here to zoom in (" + processZoomPercent +"%" +")");
                        setZoomOutToolTip("Click here to zoom out (" + processZoomPercent +"%" +")");
                        defaultController.userChoosingPDiagram();
                        break;
                }
        }});

        // handle view main working area help
        mainWorkingAreaMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiHelpMainWorkingAreaAP();
            }
        });

        // handle view R-PAGE tool bar help
        rpageToolBarMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiHelpRPAGEToolBarAP();
            }
        });

        // handle view R-PAGE Palette tool help
        rpagePaletteToolMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiHelpRPAGEPaletteToolAP();
            }
        });

        // handle view R-PAGE Diagrams help
        rpageDiagramsMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiHelpRPAGEDiagramsAP();
            }
        });

       // handle view R-PAGE Property Help
        rpagePropertyPanelMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiHelpRPAGEPropertyAP();
            }
        });

        // handle view R-PAGE about
        aboutMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                defaultController.uiAboutAP();
            }
        });



    }// end initComponenents method

/******************************************************************************/
     public int showSaveDialog(Component parent){
         return fileChooser.showSaveDialog(parent);
     }

      public int showOpenDialog(Component parent){
         return fileChooser.showOpenDialog(parent);
     }

     public File getSelectedFile(){
         return fileChooser.getSelectedFile();
     }

     /**
      * Determine if user is viewing a uow drawing diagram
      * @return
      */
     public boolean isUOWDrawingSelected(){
         if (jTabbedPane.getSelectedIndex() == UOW_DIAGRAM){
             return true;
         }
         return false;
     }

    /**
     * Determine if user is viewing a process drawing diagram
     * @return
     */
     public boolean isPDrawingSelected(){
         if (jTabbedPane.getSelectedIndex() == PROCESS_DIAGRAM){
             return true;
         }
         return false;
     }

     public void setZoomInToolTip(String text){
        zoomInButton.setToolTipText(text);
     }

     public void setZoomOutToolTip (String text){
         zoomOutButton.setToolTipText(text);
     }
}
