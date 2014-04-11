/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Polygon;

import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.SpringLayout;
import java.lang.Math;

import rpage_ver1.controller.DefaultController;
import rpage_ver1.controller.PropertyController;
import rpage_ver1.model.AbstractShape;


/**
 *
 * @author ducluu84
 */
public class ProcessPropertyPanel extends JPanel implements PropertyPanelInterface{

    //  The controller used by this view
    private DefaultController defaultController;

    // display messages
    private JTextField displayMessage;

    // this check box enables user to turn on or turn off grid
    private JCheckBox pGridCheckBox;

    /************************************************************************/
    // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
    private JTextField pNameTextField;
    private JTextField pXCoordinateTextField;
    private JTextField pYCoordinateTextField;
    private JTextField pWidthTextField;
    private JTextField pHeightTextField;
    private JTextField pArcWidthTextField;
    private JTextField pArcHeightTextField;

    // case process name comboBox
    private JComboBox cpNameComboBox;
    private String[] cpPrefixNameStrings = {"Handle a ", "Prepare a ", "Handle an ", "Prepare an "};

    // case management process name label
    // that is "Manage the flow of"
    private JLabel cmpNameLabel;

    // case management process sOrEs comboBox
    private JComboBox pSOrEsComboBox;

    private JLabel pXCoordinateLabel;
    private JLabel pYCoordinateLabel;
    private JLabel pWidthLabel;
    private JLabel pHeightLabel;
    private JLabel pArcWidthLabel;
    private JLabel pArcHeightLabel;
    private JLabel pFontNameLabel;
    private JLabel pFontSizeLabel;

    private JComboBox pFontNameComboBox;
    private JComboBox pFontSizeComboBox;

    /************************************************************************/

    /************************************************************************/
    // PROCESS RELATIONSHIP
    private JTextField fromPRelNameTextField;
    private JTextField toPRelNameTextField;

    private JLabel fromPRelNameLabel;
    private JLabel toPRelNameLabel;

    private JLabel fromRectLabel;
    private JLabel toRectLabel;

    private JComboBox fromPRectComboBox;
    private JComboBox toPRectComboBox;

    /************************************************************************/

    /************************************************************************/
    // PROCESS RELATIONSHIP NAME
    private JTextField rpageNameTextField;
    private JLabel rpageLabelNameLabel;
    private JLabel rpageFontNameLabel;
    private JLabel rpageFontSizeLabel;

    // these properties are used for first cut relationship property
    private JTextField fromProcessTextField;
    private JTextField toProcessTextField;

    // uow label font name and size combo box
    private JComboBox rpageLabelFontNameComboBox;
    private JComboBox rpageLabelFontSizeComboBox;

    /************************************************************************/
    private String[] pRectTypeStrings ={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private String[] fontNameStrings = {"Serif", "SansSerif", "Monospaced"};
    private String[] fontSizeStrings = {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
    "18", "19", "20", "21", "22"};

    private String[] sOrEsStrings = {"s", "es"};
/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public ProcessPropertyPanel(PropertyController propertyController){
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        /************************************************************************/
        // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
        pNameTextField = new JTextField();
        pXCoordinateTextField = new JTextField();
        pYCoordinateTextField = new JTextField();
        pWidthTextField = new JTextField();
        pHeightTextField = new JTextField();
        pArcWidthTextField = new JTextField();
        pArcHeightTextField = new JTextField();

        // create labels for these text fields
        cmpNameLabel = new JLabel("Manage the flow of");
        pXCoordinateLabel = new JLabel("x", JLabel.RIGHT);
        pYCoordinateLabel = new JLabel("y", JLabel.RIGHT);
        pWidthLabel = new JLabel("Width", JLabel.RIGHT);
        pHeightLabel = new JLabel("Height", JLabel.RIGHT);
        pArcWidthLabel = new JLabel("Arc width", JLabel.RIGHT);
        pArcHeightLabel = new JLabel("Arc height", JLabel.RIGHT);
        pFontNameLabel = new JLabel("Font Name", JLabel.RIGHT); // font name of process
        pFontSizeLabel = new JLabel("Font Size", JLabel.RIGHT); // font size of process

        cpNameComboBox = new JComboBox(cpPrefixNameStrings); // that is Handle a or Prepare a
        pFontNameComboBox = new JComboBox(fontNameStrings); // font name of process
        pFontSizeComboBox = new JComboBox(fontSizeStrings); // font size of process

        pSOrEsComboBox = new JComboBox(sOrEsStrings);

        /************************************************************************/
        // PROCESS RELATIONSHIP
        fromPRelNameTextField = new JTextField();
        fromPRelNameTextField.setEditable(false);
        toPRelNameTextField = new JTextField();
        toPRelNameTextField.setEditable(false);

        fromPRelNameLabel = new JLabel("From Process", JLabel.RIGHT);
        toPRelNameLabel = new JLabel("To Process", JLabel.RIGHT);

        fromRectLabel = new JLabel("From rectangle", JLabel.RIGHT);
        toRectLabel = new JLabel("To rectangle", JLabel.RIGHT);

        fromPRectComboBox = new JComboBox(pRectTypeStrings);
        toPRectComboBox = new JComboBox(pRectTypeStrings);

        /************************************************************************/
        // PROCESS RELATIONSHIP NAME
        rpageNameTextField = new JTextField();

        // label for process relationship name
        rpageLabelNameLabel = new JLabel("Relationship name", JLabel.RIGHT);
        rpageFontNameLabel = new JLabel("Font Name", JLabel.RIGHT); // font name of uow label
        rpageFontSizeLabel = new JLabel("Font Size", JLabel.RIGHT); // font size of uow label

        rpageLabelFontNameComboBox = new JComboBox(fontNameStrings); //font name of uow label
        rpageLabelFontSizeComboBox = new JComboBox(fontSizeStrings); // font size of uow label

        /********************************************************************/
        // display message and grid check box
        displayMessage = new JTextField("To display some message");
        displayMessage.setEditable(false);

        // create grid check box
        pGridCheckBox = new JCheckBox("Enable Grid");
        pGridCheckBox.setSelected(false);

        addCPPropertyPanel();
    }// end init components

/******************************************************************************/
    // Model Changes Handling
/******************************************************************************/
   /************************************************************************/
   // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
   /*
    * Display case process property value in first cut property panel
    */
    public void displayCProcess(String prefixPName, String suffixPName, int processXCoordinate,
            int processYCoordinate, int processWidth, int processHeight, int processArcWidth,
            int processArcHeight, String processFontName, String processFontSize){

        removeCPPropertyPanel();
        removeCMPPropertyPanel();
        removeProcessRelPropertyPanel();
        removeProcessLabelPropertyPanel();

        addCPPropertyPanel();

        cpNameComboBox.setSelectedItem(prefixPName);
        pNameTextField.setText(suffixPName);
        pXCoordinateTextField.setText(((Integer)processXCoordinate).toString());
        pYCoordinateTextField.setText(((Integer)processYCoordinate).toString());
        pWidthTextField.setText(((Integer)processWidth).toString());
        pHeightTextField.setText(((Integer)processHeight).toString());
        pArcWidthTextField.setText(((Integer)processArcWidth).toString());
        pArcHeightTextField.setText(((Integer)processArcHeight).toString());
        pFontNameComboBox.setSelectedItem(processFontName);
        pFontSizeComboBox.setSelectedItem(processFontSize);
        validate();
    }

   /*
    * Display process property value in first cut property panel
    */
    public void displayCMProcess(String prefixPName, String suffixPName, String sOrEs, int processXCoordinate,
            int processYCoordinate, int processWidth, int processHeight, int processArcWidth,
            int processArcHeight, String processFontName, String processFontSize){

        removeCPPropertyPanel();
        removeCMPPropertyPanel();
        removeProcessRelPropertyPanel();
        removeProcessLabelPropertyPanel();

        addCMPPropertyPanel();

        cmpNameLabel.setText(prefixPName);
        pNameTextField.setText(suffixPName);
        pSOrEsComboBox.setSelectedItem(sOrEs);

        pXCoordinateTextField.setText(((Integer)processXCoordinate).toString());
        pYCoordinateTextField.setText(((Integer)processYCoordinate).toString());
        pWidthTextField.setText(((Integer)processWidth).toString());
        pHeightTextField.setText(((Integer)processHeight).toString());
        pArcWidthTextField.setText(((Integer)processArcWidth).toString());
        pArcHeightTextField.setText(((Integer)processArcHeight).toString());
        pFontNameComboBox.setSelectedItem(processFontName);
        pFontSizeComboBox.setSelectedItem(processFontSize);
        validate();
    }

    public void notDisplayProcess(){
        pNameTextField.setText("");
        pXCoordinateTextField.setText("");
        pYCoordinateTextField.setText("");
        pWidthTextField.setText("");
        pHeightTextField.setText("");
        pArcWidthTextField.setText("");
        pArcHeightTextField.setText("");

        // this will only make error if the shape (process) has not been removed from the property controller
        pFontNameComboBox.setSelectedItem(0);
        pFontSizeComboBox.setSelectedItem(0);
    }

    /************************************************************************/
    // PROCESS RELATIONSHIP

    /************************************************************************/
    // PROCESS RELATIONSHIP NAME
    /**
    * @param
    */
    public void displayProcessRel(String fromShapeName, String toShapeName, int rectIndexFrom,
            int rectIndexTo){

        removeCPPropertyPanel();
        removeCMPPropertyPanel();
        removeProcessRelPropertyPanel();
        removeProcessLabelPropertyPanel();

        addProcessRelPropertyPanel();

        fromPRelNameTextField.setText(fromShapeName);
        toPRelNameTextField.setText(toShapeName);
        fromPRectComboBox.setSelectedIndex(rectIndexFrom);
        toPRectComboBox.setSelectedIndex(rectIndexTo);
        validate();
    }// end displayUOWRelationshipProperty method

    public void notDisplayProcessRel(){
        fromPRelNameTextField.setText("");
        toPRelNameTextField.setText("");

        // this will make error as the defaultController will handle events
        // when fromUOWRectComboBox.setSelectedIndex(0) it will generate an event
        // and the defaultController will set the from rectangle to 0
        //fromUOWRectComboBox.setSelectedIndex(0);
        //toUOWRectComboBox.setSelectedIndex(0);
    }

    /********************************************************************/
    // PROCESS relationship name
    public void displayProcessRelName(String rpageLabelName, String rpageLabelFontName, String rpageLabelFontSize){
        removeCPPropertyPanel();
        removeCMPPropertyPanel();
        removeProcessRelPropertyPanel();
        removeProcessLabelPropertyPanel();

        addProcessLabelPropertyPanel();

        rpageNameTextField.setText(rpageLabelName);
        rpageLabelFontNameComboBox.setSelectedItem(rpageLabelFontName);
        rpageLabelFontSizeComboBox.setSelectedItem(rpageLabelFontSize);
        validate();
    }

    public void notDisplayProcessRelName(){
        rpageNameTextField.setText("");

        // this will only make error if the shape (uow relationship name) has not been removed from the
        // propertyController
        rpageLabelFontNameComboBox.setSelectedItem(0);
        rpageLabelFontSizeComboBox.setSelectedItem(0);
    }

    public void shapePropertyChange(final PropertyChangeEvent propertyChangeEvent){

        /************************************************************************/
        // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
        // PROCESS SUFFIX Name property
        if (propertyChangeEvent.getPropertyName().equals(PropertyController.PREFIX_PROCESS_NAME_PROPERTY)){
            pPrefixNamePropertyChange(propertyChangeEvent);
        }

        // PROCESS SUFFIX Name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.SUFFIX_PROCESS_NAME_PROPERTY)){
            pNamePropertyChange(propertyChangeEvent);
        }

        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_SORES_PROPERTY)){
            pSOrEsPropertyChange(propertyChangeEvent);
        }

        // PROCESS width property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_WIDTH_PROPERTY)){
           pWidthPropertyChange(propertyChangeEvent);
        }

        // PROCESS height property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_HEIGHT_PROPERTY)){
           pHeightPropertyChange(propertyChangeEvent);
        }

        // PROCESS arcwidth property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_ARC_WIDTH_PROPERTY)){
           pArcWidthPropertyChange(propertyChangeEvent);
        }

        // PROCESS arc height property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_ARC_HEIGHT_PROPERTY)){
           pArcHeightPropertyChange(propertyChangeEvent);
        }

        // PROCESS Font Name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_FONT_NAME_PROPERTY)){
            pFontNamePropertyChange(propertyChangeEvent);
        }

        // PROCESS Font Size property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_FONT_SIZE_PROPERTY)){
            pFontSizePropertyChange(propertyChangeEvent);
        }

        // PROCESS XCoordinate property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_XCOORDINATE_PROPERTY)){
            pXCoordinatePropertyChange(propertyChangeEvent);
        }

        // PROCESS YCoordinate property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.PROCESS_YCOORDINATE_PROPERTY)){
            pYCoordinatePropertyChange(propertyChangeEvent);
        }

        /************************************************************************/
        // PROCESS RELATIONSHIP - No Need as defaultController will handle uow relationship event

        /************************************************************************/
        // PROCESS RELATIONSHIP NAME
        // process relationship name name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL__NAME_PROPERTY)){
            processRelNamePropertyChange(propertyChangeEvent);
        }

        // process relationship name font name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL_FONT_NAME_PROPERTY)){
            processRelFontNamePropertyChange(propertyChangeEvent);
        }

        // process relationship name font size property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL_FONT_SIZE_PROPERTY)){
            processRelFontSizePropertyChange(propertyChangeEvent);
        }
    }// END shapePropertyChange method

    /************************************************************************/
    // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
    // Update widget as PROCESS Prefix Name property has changed
    private void pPrefixNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!((String)cpNameComboBox.getSelectedItem()).equalsIgnoreCase(newStringValue)){
                cpNameComboBox.setSelectedItem(newStringValue);
         }
    }

    // Update widget as PROCESS Name property has changed
    private void pNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!pNameTextField.getText().equalsIgnoreCase(newStringValue)){
                pNameTextField.setText(newStringValue);
         }
    }

    // Update widget as PROCESS sOrEs Name property has changed
    private void pSOrEsPropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!((String)pSOrEsComboBox.getSelectedItem()).equalsIgnoreCase(newStringValue)){
                pSOrEsComboBox.setSelectedItem(newStringValue);
         }
    }

    // Update widget as PROCESSs width property has changed
    private void pWidthPropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pWidthTextField.getText().equalsIgnoreCase(newStringValue)){
                pWidthTextField.setText(newStringValue);
        }
    }

    // Update widget as PRCOESS height property has changed
    private void pHeightPropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pHeightTextField.getText().equalsIgnoreCase(newStringValue)){
                pHeightTextField.setText(newStringValue);
        }
    }

    // Update widget as PROCESS arc width property has changed
    private void pArcWidthPropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pArcWidthTextField.getText().equalsIgnoreCase(newStringValue)){
                pArcWidthTextField.setText(newStringValue);
        }
    }

    // Update widget as PROCESS arc height property has changed
    private void pArcHeightPropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pArcHeightTextField.getText().equals(newStringValue)){
                pArcHeightTextField.setText(newStringValue);
        }
    }

    // Update widget as PROCESS Font name property has changed
    private void pFontNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pFontNameComboBox.getSelectedItem().equals(newStringValue)){
            pFontNameComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as PROCESS Font size property has changed
    private void pFontSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pFontSizeComboBox.getSelectedItem().equals(newStringValue)){
            pFontSizeComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as PROCESS XCoordinate property has changed
    private void pXCoordinatePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pXCoordinateTextField.getText().equals(newStringValue)){
                pXCoordinateTextField.setText(newStringValue);
        }
    }

    // Update widget as PROCESS YCoordinate property has changed
    private void pYCoordinatePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!pYCoordinateTextField.getText().equals(newStringValue)){
                pYCoordinateTextField.setText(newStringValue);
        }
    }

    /************************************************************************/
    // PROCESS RELATIONSHIP - No Need as defaultController will handle uow relationship event

    /************************************************************************/
    // PROCESS RELATIONSHIP NAME
    // Update widget as Process relationship name property has changed
    private void processRelNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!rpageNameTextField.getText().equals(newStringValue)){
                rpageNameTextField.setText(newStringValue);
         }
    }

    // Update widget as Process Relationship name Font name property has changed
    private void processRelFontNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!rpageLabelFontNameComboBox.getSelectedItem().equals(newStringValue)){
            rpageLabelFontNameComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as Process Relationship name Font size property has changed
    private void processRelFontSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!rpageLabelFontSizeComboBox.getSelectedItem().equals(newStringValue)){
            rpageLabelFontSizeComboBox.setSelectedItem(newStringValue);
        }
    }

/******************************************************************************/
    //    // Events fired by process property panel widgets
/******************************************************************************/
   /**
    * Default controller handles events in this panel
    */
    public void addDefaultController(final DefaultController defaultController){
        //
        pGridCheckBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent itemEvent){
                defaultController.pGridItemStateChanged(itemEvent);
            }
        });// end addItemListener

        /************************************************************************/
        // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size
        // add ACTION listener for cpNameComboBox object - PREFIX NAME
        cpNameComboBox.addActionListener(new ActionListener() {
            // when a user choose an item from the combo box, an ActionEvent
            // is generated
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pPrefixNameComboBoxAP(actionEvent);
            }
        });

        // add ACTION listener for pNameTextField object - NAME
        pNameTextField.addActionListener(new ActionListener() {
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pNameTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pNameTextField object - NAME
        pNameTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pNameTextFieldFL(focusEvent);
            }
        });

        // add listener for pSOrEsComboBox
        pSOrEsComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.pSOrEsComboBoxAP(actionEvent);
            }
        });

         // add ACTION listener for pWidthTextField object - WIDTH
        pWidthTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                defaultController.pWidthTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pWidthTextField object - WIDTH
        pWidthTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                defaultController.pWidthTextFieldFL(focusEvent);
            }
        });

         // add ACTION listener for pHeightTextField object - HEIGHT
        pHeightTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                defaultController.pHeightTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pArcHeightTextField object - HEIGHT
        pHeightTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                defaultController.pHeightTextFieldFL(focusEvent);
            }
        });

        // add ACTION listener for pWidthTextField object - ARC WIDTH
        pArcWidthTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                defaultController.pArcWidthTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pArcWidthTextField object -  ARC WIDTH
        pArcWidthTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                defaultController.pArcWidthTextFieldFL(focusEvent);
            }
        });

        // add ACTION listener for pArcHeightTextField object - ARC HEIGHT
        pArcHeightTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                defaultController.pArcHeightTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pArcHeightTextField object - ARC HEIGHT
        pArcHeightTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                defaultController.pArcHeightTextFieldFL(focusEvent);
            }
        });

        // add listener for pFontNameComboBox
        pFontNameComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.pFontNameComboBoxAP(actionEvent);
            }
        });

        // add listener for pFontSizeComboBox
        pFontSizeComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.pFontSizeComboBoxAP(actionEvent);
            }
        });

         // add ACTION listener for pXCoordinateTextField object - XCOORDINATE
        pXCoordinateTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pXCoordinateTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for pXCoordinateTextField object - XCOORDINATE
        pXCoordinateTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pXCoordinateTextFieldFL(focusEvent);
            }
        });

        // add ACTION listener for pYCoordinateTextField object - YCOORDINATE
        pYCoordinateTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                defaultController.pYCoordinateTextFieldAP(actionEvent);
            }
        });

        // add FOCUS listener for cprocessYCoordinateTextField object - YCOORDINATE
        pYCoordinateTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.pYCoordinateTextFieldFL(focusEvent);
            }
        });

        /************************************************************************/
        // PROCESS RELATIONSHIP
        // add listener for fromPRectComboBox
        fromPRectComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.fromPRectComboBoxAP(actionEvent);
            }
        });

        // add listener for toUOWRectComboBox
        toPRectComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.toPRectComboBoxAP(actionEvent);
            }
        });

        /************************************************************************/
        // PROCESS RELATIONSHIP NAME
        // add listener for rpageNameTextField object - Process Relationship Name
        rpageNameTextField.addActionListener(new ActionListener(){
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.processRPAGENameTextFieldAP(actionEvent);
            }
        });// end addActionListener

        // add listener for rpageNameTextField object - UOW Relationship Name
        rpageNameTextField.addFocusListener(new FocusAdapter(){
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void focusLost(FocusEvent focusEvent){
                // delegate the processing of this event to defaultController
                defaultController.processRPAGENameTextFieldFL(focusEvent);
            }
        });// end addActionListener

        // add listener for rpageLabelFontNameComboBox
        rpageLabelFontNameComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.rpageFontNameComboBoxAP(actionEvent);
            }
        });

        // add listener for uowFontSizeComboBox
        rpageLabelFontSizeComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.rpageFontSizeComboBoxAP(actionEvent);
            }
        });
    }

    public void notifyDisplayMessage(String message){
        displayMessage.setText(message);
        displayMessage.setForeground(Color.RED);
        displayMessage.setBackground(Color.LIGHT_GRAY);
    }
/******************************************************************************/
    /**
     * Add Case process property panel
     */
    public void addCPPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        /**********************************************************************/
        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,5
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pGridCheckBox, gridBagConstraints);
        /**********************************************************************/

        /**********************************************************************/
        // put the case process name combo box in location 0,1 - CASE PROCESS PREFIX NAME
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(cpNameComboBox, gridBagConstraints);

        // put the process name text field in location 1,1 - CASE PROCESS NAME TEXT FIELD
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pNameTextField, gridBagConstraints);

        // put the process XCoordinate label in location 2,1 - PROCESS XCoordinate LABEL
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pXCoordinateLabel, gridBagConstraints);

        // put the process XCoordinate text field in location 3,1 - PROCESS XCoordinate TextField
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pXCoordinateTextField, gridBagConstraints);

        // put the process YCoordinate label in location 4,1 - PROCESS YCoordinate Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pYCoordinateLabel, gridBagConstraints);

        // put the process YCoordinate text field in location 5,1 - PROCESS YCoordinate Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pYCoordinateTextField, gridBagConstraints);

        /**********************************************************************/
        // another line
        // put the process width label in location 0,2 - PROCESS Width Lable
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pWidthLabel, gridBagConstraints);

        // put the process width text field in location 1,2 - PROCESS Width Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pWidthTextField, gridBagConstraints);

        // put the process height label in location 2,2 - PROCESS Height Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pHeightLabel, gridBagConstraints);

        // put the process height text field in location 3,2 - PROCESS Height Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pHeightTextField, gridBagConstraints);

        // put the process Arc width label in location 4,2 - PROCESS Width Lable
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcWidthLabel, gridBagConstraints);

        // put the process Arc width text field in location 5,2 - PROCESS Width Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcWidthTextField, gridBagConstraints);

        /**********************************************************************/
        // another line
         // put the process Arc height label in location 0,3 - PROCESS Height Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcHeightLabel, gridBagConstraints);

        // put the process Arc height text field in location 1,3 - PROCESS Height Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcHeightTextField, gridBagConstraints);


        // put the process font name label in location 2,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontNameLabel, gridBagConstraints);

        // put the process font name comboBox in location 3,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontNameComboBox, gridBagConstraints);

        // put the process font size label in location 4,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontSizeLabel, gridBagConstraints);

        // put the uow font size comboBox in location 5,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontSizeComboBox, gridBagConstraints);
        /**********************************************************************/

        // add these labels and text fields to the UOW panel properties
        add(displayMessage);
        add(pGridCheckBox);

        add(cpNameComboBox);
        add(pNameTextField);
        add(pXCoordinateLabel);
        add(pXCoordinateTextField);
        add(pYCoordinateLabel);
        add(pYCoordinateTextField);
        add(pWidthLabel);
        add(pWidthTextField);
        add(pHeightLabel);
        add(pHeightTextField);
        add(pArcWidthLabel);
        add(pArcWidthTextField);
        add(pArcHeightLabel);
        add(pArcHeightTextField);
        add(pFontNameLabel);
        add(pFontNameComboBox);
        add(pFontSizeLabel);
        add(pFontSizeComboBox);

    }

   /**
    * Remove Case process property panel
    */
    public void removeCPPropertyPanel(){

        remove(displayMessage);
        remove(pGridCheckBox);

        remove(cpNameComboBox);
        remove(pNameTextField);
        remove(pXCoordinateLabel);
        remove(pXCoordinateTextField);
        remove(pYCoordinateLabel);
        remove(pYCoordinateTextField);
        remove(pWidthLabel);
        remove(pWidthTextField);
        remove(pHeightLabel);
        remove(pHeightTextField);
        remove(pArcWidthLabel);
        remove(pArcWidthTextField);
        remove(pArcHeightLabel);
        remove(pArcHeightTextField);
        remove(pFontNameLabel);
        remove(pFontNameComboBox);
        remove(pFontSizeLabel);
        remove(pFontSizeComboBox);

        setLayout(null);
    }
/******************************************************************************/
     /**
     * Add Case management process property panel
     */
    public void addCMPPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        /**********************************************************************/
        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,7
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pGridCheckBox, gridBagConstraints);
        /**********************************************************************/

        /**********************************************************************/
        // put the case process name label in location 0,1 - CASE PROCESS MANAGEMENT PREFIX NAME
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(cmpNameLabel, gridBagConstraints);

        // put the process name text field in location 1,1 - CASE MANAGEMENT PROCESS NAME TEXT FIELD
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagLayout.setConstraints(pNameTextField, gridBagConstraints);

        // put the s or es combobox in location 3,1 - CASE MANAGEMENT PROCESS s or es
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pSOrEsComboBox, gridBagConstraints);

        // put the process XCoordinate label in location 4,1 - PROCESS XCoordinate LABEL
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pXCoordinateLabel, gridBagConstraints);

        // put the process XCoordinate text field in location 5,1 - PROCESS XCoordinate TextField
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pXCoordinateTextField, gridBagConstraints);

        // put the process YCoordinate label in location 6,1 - PROCESS YCoordinate Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pYCoordinateLabel, gridBagConstraints);

        // put the process YCoordinate text field in location 7,1 - PROCESS YCoordinate Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pYCoordinateTextField, gridBagConstraints);

        /**********************************************************************/
        // another line
        // put the process width label in location 0,2 - PROCESS Width Lable
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pWidthLabel, gridBagConstraints);

        // put the process width text field in location 1,2 - PROCESS Width Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pWidthTextField, gridBagConstraints);

        // put the process height label in location 2,2 - PROCESS Height Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pHeightLabel, gridBagConstraints);

        // put the process height text field in location 3,2 - PROCESS Height Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pHeightTextField, gridBagConstraints);

        // put the process Arc width label in location 4,2 - PROCESS Width Lable
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcWidthLabel, gridBagConstraints);

        // put the process Arc width text field in location 5,2 - PROCESS Width Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcWidthTextField, gridBagConstraints);

        // put the process Arc height label in location 6,2 - PROCESS Height Label
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcHeightLabel, gridBagConstraints);

        // put the process Arc height text field in location 7,2 - PROCESS Height Text Field
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pArcHeightTextField, gridBagConstraints);

        /**********************************************************************/
        // another line

        // put the process font name label in location 0,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontNameLabel, gridBagConstraints);

        // put the process font name comboBox in location 1,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontNameComboBox, gridBagConstraints);

        // put the process font size label in location 2,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontSizeLabel, gridBagConstraints);

        // put the uow font size comboBox in location 3,3
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pFontSizeComboBox, gridBagConstraints);
        /**********************************************************************/

        // add these labels and text fields to the UOW panel properties
        add(displayMessage);
        add(pGridCheckBox);

        add(cmpNameLabel);
        add(pNameTextField);
        add(pSOrEsComboBox);
        add(pXCoordinateLabel);
        add(pXCoordinateTextField);
        add(pYCoordinateLabel);
        add(pYCoordinateTextField);
        add(pWidthLabel);
        add(pWidthTextField);
        add(pHeightLabel);
        add(pHeightTextField);
        add(pArcWidthLabel);
        add(pArcWidthTextField);
        add(pArcHeightLabel);
        add(pArcHeightTextField);
        add(pFontNameLabel);
        add(pFontNameComboBox);
        add(pFontSizeLabel);
        add(pFontSizeComboBox);
    }

   /**
    * Remove Case process property panel
    */
    public void removeCMPPropertyPanel(){

        remove(displayMessage);
        remove(pGridCheckBox);

        remove(cmpNameLabel);
        remove(pNameTextField);
        remove(pSOrEsComboBox);
        remove(pXCoordinateLabel);
        remove(pXCoordinateTextField);
        remove(pYCoordinateLabel);
        remove(pYCoordinateTextField);
        remove(pWidthLabel);
        remove(pWidthTextField);
        remove(pHeightLabel);
        remove(pHeightTextField);
        remove(pArcWidthLabel);
        remove(pArcWidthTextField);
        remove(pArcHeightLabel);
        remove(pArcHeightTextField);
        remove(pFontNameLabel);
        remove(pFontNameComboBox);
        remove(pFontSizeLabel);
        remove(pFontSizeComboBox);

        setLayout(null);
    }

/******************************************************************************/
   /**
    * Add Process RELATIONSHIP property panel
    */
    public void addProcessRelPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,4
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pGridCheckBox, gridBagConstraints);

         // put the fromPRelationshipNameLabel in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromPRelNameLabel, gridBagConstraints);

        // put the fromPRelationshipNameTextField in location 1, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromPRelNameTextField, gridBagConstraints);

         // put the toPRelationshipNameLabel in location 2,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toPRelNameLabel, gridBagConstraints);

        // put the fromPRelationshipNameTextField in location 3, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toPRelNameTextField, gridBagConstraints);

        // put the fromRectLabel in location 0,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromRectLabel, gridBagConstraints);

        // put the fromPRectComboBox location 1, 2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromPRectComboBox, gridBagConstraints);

        // put the toRectLabel in location 2,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toRectLabel, gridBagConstraints);

        // put the toPRectComboBox in location 3,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toPRectComboBox, gridBagConstraints);

        add(displayMessage);
        add(pGridCheckBox);

        add(fromPRelNameLabel);
        add(fromPRelNameTextField);

        add(toPRelNameLabel);
        add(toPRelNameTextField);

        add(fromRectLabel);
        add(fromPRectComboBox);
        add(toRectLabel);
        add(toPRectComboBox);
    }

    public void removeProcessRelPropertyPanel(){
        remove(displayMessage);
        remove(pGridCheckBox);

        remove(fromPRelNameLabel);
        remove(toPRelNameLabel);

        remove(fromPRelNameTextField);
        remove(toPRelNameTextField);

        remove(fromRectLabel);
        remove(fromPRectComboBox);
        remove(toRectLabel);
        remove(toPRectComboBox);

        setLayout(null);
    }

/******************************************************************************/
  /**
    * Add UOW RELATIONSHIP NAME property panel
    */
    public void addProcessLabelPropertyPanel(){
           // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,5
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(pGridCheckBox, gridBagConstraints);

         // put the rpageNameLabel in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelNameLabel, gridBagConstraints);

        // put the rpageNameTextField in location 1, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageNameTextField, gridBagConstraints);

        // put the rpageFontNameLabel in location 2,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageFontNameLabel, gridBagConstraints);

        // put the uowLabelFontNameComboBox in location 3,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelFontNameComboBox, gridBagConstraints);

        // put the rpageFontSizeLabel in location 4,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageFontSizeLabel, gridBagConstraints);

        // put the uowLabelFontSizeComboBox in location 5,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelFontSizeComboBox, gridBagConstraints);

        add(displayMessage);
        add(pGridCheckBox);

        add(rpageLabelNameLabel);
        add(rpageNameTextField);
        add(rpageFontNameLabel);
        add(rpageLabelFontNameComboBox);
        add(rpageFontSizeLabel);
        add(rpageLabelFontSizeComboBox);
    }

    public void removeProcessLabelPropertyPanel(){
        remove(displayMessage);
        remove(pGridCheckBox);

        remove(rpageLabelNameLabel);
        remove(rpageNameTextField);
        remove(rpageFontNameLabel);
        remove(rpageLabelFontNameComboBox);
        remove(rpageFontSizeLabel);
        remove(rpageLabelFontSizeComboBox);

        setLayout(null);
    }
}
